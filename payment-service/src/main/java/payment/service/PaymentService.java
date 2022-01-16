package payment.service;

import domain.Payment;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
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
        this.queue.addHandler("BankAccountReceived", this::handleBankAccountReceived);
        pendingPayments = new HashMap<>();
        bankTransactionService = new BankTransactionService();
    }

    public void handlePaymentInitiated(Event e) { // publish a "TokenValidationRequested" event
        Payment payment = e.getArgument(0, Payment.class);
        pendingPayments.put(payment.getCorrelationId(), payment);
        Event event = new Event("TokenValidationRequested", new Object[] {payment.getCorrelationId(), payment.getCustomerToken()});
        queue.publish(event);
    }

    public void handleTokenValidated(Event e) {
        String correlationId = e.getArgument(0, String.class); // find out the corresponding pending payment
        String customerId = e.getArgument(1, String.class);
        Payment payment = pendingPayments.get(correlationId);
        Event event = new Event("BankAccountRequested", new Object[] {payment.getCorrelationId(), customerId, payment.getMerchantId()});
        queue.publish(event);
    }

    public void handleBankAccountReceived(Event e) {
        String correlationId = e.getArgument(0, String.class); // find out the corresponding pending payment
        String customerBankAccountId = e.getArgument(1, String.class);
        String merchantBankAccountId = e.getArgument(2, String.class);
        Payment payment = pendingPayments.get(correlationId);
        try { // conduct the bank transaction
            bankTransactionService.transferMoney(customerBankAccountId, merchantBankAccountId, payment.getAmount());
            pendingPayments.remove(correlationId);
            Event event = new Event("PaymentCompleted", new Object[] {payment});
            queue.publish(event);
        } catch (BankServiceException_Exception err) {
            pendingPayments.remove(correlationId);
            Event event = new Event("PaymentBankError", new Object[] {err.getMessage()});
            queue.publish(event);
        }
    }
}
