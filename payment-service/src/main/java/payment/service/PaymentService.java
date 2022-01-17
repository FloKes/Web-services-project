package payment.service;

import DTOs.BankAccountRequestDTO;
import DTOs.PaymentDTO;
import DTOs.TokenValidationDTO;
import domain.Payment;
import dtu.ws.fastmoney.BankServiceException_Exception;
import mappers.Mapper;
import messaging.Event;
import messaging.MessageQueue;
import transaction.service.BankTransactionService;

import java.util.HashMap;


public class PaymentService {
    private MessageQueue queue;
    private HashMap<String, Payment> pendingPayments; // payments waiting for token validation, <correlationID, payment>
    private BankTransactionService bankTransactionService;

    public PaymentService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("PaymentInitiated", this::handlePaymentInitiated);
        this.queue.addHandler("TokenValidated", this::handleTokenValidated);
        this.queue.addHandler("TokenInvalid", this::handleTokenInvalid);
        this.queue.addHandler("BankAccountReceived", this::handleBankAccountReceived);
        pendingPayments = new HashMap<>();
        bankTransactionService = new BankTransactionService();
    }

    public void handlePaymentInitiated(Event e) { // publish a "TokenValidationRequested" event
        // We have a PaymentDTO so we don't create dependencies between our Domain and communication
        PaymentDTO paymentDTO = e.getArgument(0, PaymentDTO.class);
        Payment payment = new Payment();
        Mapper.mapPaymentDTOToPayment(paymentDTO, payment);
        payment.setCorrelationId(String.valueOf(pendingPayments.size() + 1));
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

    public void handleBankAccountReceived(Event e) {
        BankAccountRequestDTO bankAccountRequestDTO = e.getArgument(0, BankAccountRequestDTO.class);
        Payment payment = pendingPayments.get(bankAccountRequestDTO.getCorrelationId());
        try { // conduct the bank transaction
            bankTransactionService.transferMoney(
                    bankAccountRequestDTO.getCustomerBankAccount(), bankAccountRequestDTO.getMerchantBankAccount(), payment.getAmount());
            pendingPayments.remove(bankAccountRequestDTO.getCorrelationId());
            PaymentDTO paymentDTO = new PaymentDTO();
            Mapper.mapPaymentToDTO(payment, paymentDTO);
            Event event = new Event("PaymentCompleted", new Object[] {paymentDTO});
            queue.publish(event);
        } catch (BankServiceException_Exception err) {
            pendingPayments.remove(bankAccountRequestDTO.getCorrelationId());
            Event event = new Event("PaymentBankError", new Object[] {err.getMessage()});
            queue.publish(event);
        }
    }
}
