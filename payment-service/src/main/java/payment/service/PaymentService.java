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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class PaymentService {
    // Events received
    public static final String PAYMENT_INITIATED = "PaymentInitiated";
    public static final String BANK_ACCOUNT_PROVIDED = "BankAccountProvided";
    public static final String TOKEN_VALIDATED = "TokenValidated";
    public static final String TOKEN_INVALID = "TokenInvalid";

    // Events sent
    public static final String PAYMENT_COMPLETED = "PaymentCompleted";
    public static final String TOKEN_VALIDATION_REQUESTED = "TokenValidationRequested";
    public static final String BANK_ACCOUNT_REQUESTED = "BankAccountRequested";
    public static final String PAYMENT_BANK_ERROR = "PaymentBankError";


    private MessageQueue queue;
    private Map<CorrelationId, Payment> pendingPayments; // payments waiting for token validation, <correlationId, payment>
    private BankTransactionService bankTransactionService;
    static int id = 0;

    public PaymentService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler(PAYMENT_INITIATED, this::handlePaymentInitiated);
        this.queue.addHandler(TOKEN_VALIDATED, this::handleTokenValidated);
        this.queue.addHandler(TOKEN_INVALID, this::handleTokenInvalid);
        this.queue.addHandler(BANK_ACCOUNT_PROVIDED, this::handleBankAccountProvided);
        pendingPayments = new ConcurrentHashMap<>();
        bankTransactionService = new BankTransactionService();
    }

    // methods
    private String nextId() {
        id++;
        return Integer.toString(id);
    }

    // handlers

    public void handlePaymentInitiated(Event e) { // publish a "TokenValidationRequested" event
        // We have a PaymentDTO so we don't create dependencies between our Domain and communication
        PaymentDTO paymentDTO = e.getArgument(0, PaymentDTO.class);
        CorrelationId correlationId = e.getArgument(1, CorrelationId.class);
        Payment payment = new Payment();
        Mapper.mapPaymentDTOToPayment(paymentDTO, payment);
        payment.setPaymentId(nextId());
        pendingPayments.put(correlationId, payment);
        TokenValidationDTO tokenValidationDTO = new TokenValidationDTO();
        Mapper.mapTokenValidationDTO(payment, tokenValidationDTO);
        Event event = new Event(TOKEN_VALIDATION_REQUESTED, new Object[] {tokenValidationDTO, correlationId});
        queue.publish(event);
    }

    public void handleTokenValidated(Event e) {
        TokenValidationDTO tokenValidationDTO = e.getArgument(0, TokenValidationDTO.class);
        CorrelationId correlationId = e.getArgument(1, CorrelationId.class);
        Payment payment = pendingPayments.get(correlationId);
        BankAccountRequestDTO bankAccountRequestDTO = new BankAccountRequestDTO();
        Mapper.mapBankAccountRequestDTO(payment, tokenValidationDTO, bankAccountRequestDTO);
        Event event = new Event(BANK_ACCOUNT_REQUESTED, new Object[] {bankAccountRequestDTO, correlationId});
        queue.publish(event);
    }

    public void handleTokenInvalid(Event e) {
        TokenValidationDTO tokenValidationDTO = e.getArgument(0, TokenValidationDTO.class);
        CorrelationId correlationId = e.getArgument(1, CorrelationId.class);
        pendingPayments.remove(correlationId);
        Event event = new Event(TOKEN_INVALID, new Object[] {tokenValidationDTO, correlationId});
        queue.publish(event);
    }

    public void handleBankAccountProvided(Event e) {
        BankAccountRequestDTO bankAccountRequestDTO = e.getArgument(0, BankAccountRequestDTO.class);
        CorrelationId correlationId = e.getArgument(1, CorrelationId.class);
        Payment payment = pendingPayments.get(correlationId);
        try { // conduct the bank transaction
            bankTransactionService.transferMoney(
                    bankAccountRequestDTO.getCustomerBankAccount(), bankAccountRequestDTO.getMerchantBankAccount(), payment.getAmount());
            pendingPayments.remove(correlationId);
            PaymentDTO paymentDTO = new PaymentDTO();
            Mapper.mapPaymentToDTO(payment, paymentDTO);
            Event event = new Event(PAYMENT_COMPLETED, new Object[] {paymentDTO, correlationId});
            queue.publish(event);
        } catch (BankServiceException_Exception err) {
            pendingPayments.remove(correlationId);
            Event event = new Event(PAYMENT_BANK_ERROR, new Object[] {err.getMessage(), correlationId});
            queue.publish(event);
        }
    }
}
