package payment.service;

import domain.CorrelationId;
import dtos.BankAccountRequestDTO;
import dtos.PaymentDTO;
import dtos.TokenValidationDTO;
import domain.Payment;
import dtu.ws.fastmoney.BankServiceException_Exception;
import mappers.Mapper;
import messaging.Event;
import messaging.MessageQueue;
import transaction.service.BankTransactionService;

import java.util.HashMap;


public class PaymentService {
    public static final String PAYMENT_COMPLETED = "PaymentCompleted";
    public static final String PAYMENT_INITIATED = "PaymentInitiated";
    public static final String BANK_ACCOUNT_PROVIDED = "BankAccountProvided";
    public static final String TOKEN_VALIDATED = "TokenValidated";


    private MessageQueue queue;
    private HashMap<CorrelationId, Payment> pendingPayments; // payments waiting for token validation, <correlationID, payment>
    private BankTransactionService bankTransactionService;

    public PaymentService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler(PAYMENT_INITIATED, this::handlePaymentInitiated);
        this.queue.addHandler(TOKEN_VALIDATED, this::handleTokenValidated);
        this.queue.addHandler("TokenInvalid", this::handleTokenInvalid);
        this.queue.addHandler(BANK_ACCOUNT_PROVIDED, this::handleBankAccountProvided);
        pendingPayments = new HashMap<>();
        bankTransactionService = new BankTransactionService();
    }

    public void handlePaymentInitiated(Event e) { // publish a "TokenValidationRequested" event
        // We have a PaymentDTO so we don't create dependencies between our Domain and communication
        PaymentDTO paymentDTO = e.getArgument(0, PaymentDTO.class);
        Payment payment = new Payment();
        Mapper.mapPaymentDTOToPayment(paymentDTO, payment);
        payment.setCorrelationId(paymentDTO.getCorrelationId());
        pendingPayments.put(payment.getCorrelationId(), payment);
        TokenValidationDTO tokenValidationDTO = new TokenValidationDTO();
        Mapper.mapTokenValidationDTO(payment, tokenValidationDTO);
        Event event = new Event("TokenValidationRequested", new Object[] {tokenValidationDTO});
        queue.publish(event);
    }

    public void handleTokenValidated(Event e) {
        TokenValidationDTO tokenValidationDTO = e.getArgument(0, TokenValidationDTO.class);
        Payment payment = pendingPayments.get(tokenValidationDTO.getCorrelationId());
        BankAccountRequestDTO bankAccountRequestDTO = new BankAccountRequestDTO();
        Mapper.mapBankAccountRequestDTO(payment, tokenValidationDTO, bankAccountRequestDTO);
        Event event = new Event("BankAccountRequested", new Object[] {bankAccountRequestDTO});
        queue.publish(event);
    }

    public void handleTokenInvalid(Event e) {
        TokenValidationDTO tokenValidationDTO = e.getArgument(0, TokenValidationDTO.class);
        Payment payment = pendingPayments.remove(tokenValidationDTO.getCorrelationId());
        Event event = new Event("TokenInvalid", new Object[] {tokenValidationDTO});
        queue.publish(event);
    }

    public void handleBankAccountProvided(Event e) {
        BankAccountRequestDTO bankAccountRequestDTO = e.getArgument(0, BankAccountRequestDTO.class);
        Payment payment = pendingPayments.get(bankAccountRequestDTO.getCorrelationId());
        try { // conduct the bank transaction
            bankTransactionService.transferMoney(
                    bankAccountRequestDTO.getCustomerBankAccount(), bankAccountRequestDTO.getMerchantBankAccount(), payment.getAmount());
            pendingPayments.remove(bankAccountRequestDTO.getCorrelationId());
            PaymentDTO paymentDTO = new PaymentDTO();
            Mapper.mapPaymentToDTO(payment, paymentDTO);
            Event event = new Event(PAYMENT_COMPLETED, new Object[] {paymentDTO});
            queue.publish(event);
        } catch (BankServiceException_Exception err) {
            pendingPayments.remove(bankAccountRequestDTO.getCorrelationId());
            Event event = new Event("PaymentBankError", new Object[] {err.getMessage()});
            queue.publish(event);
        }
    }
}
