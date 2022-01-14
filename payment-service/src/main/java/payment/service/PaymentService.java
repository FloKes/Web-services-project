package payment.service;

import domain.Payment;
import dtu.ws.fastmoney.BankService;
import messaging.Event;
import messaging.MessageQueue;
import transaction.service.BankTransactionService;

import java.util.HashMap;

public class PaymentService {
    MessageQueue queue;
    HashMap<String, Payment> pendingPayments; //<customerToken, payment>
    BankTransactionService bankTransactionService;

    public PaymentService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("PaymentInitiated", this::handlePaymentInitiated);
        this.queue.addHandler("TokenValidated", this::handleTokenValidated);
        pendingPayments = new HashMap<>();
        bankTransactionService = new BankTransactionService();
    }

    public void handlePaymentInitiated(Event e) {
        // publish a "TokenValidationRequested" event
        Payment payment = e.getArgument(0, Payment.class);
        String customerToken = payment.getCustomerToken();
        if (pendingPayments.containsKey(customerToken)) { // token is in the pending payments
               // return error, close payment
            return; //todo
        }
        else {
            pendingPayments.put(customerToken, payment);
        }
        Event event = new Event("TokenValidationRequested", new Object[] {customerToken});
        queue.publish(event);

    }

    public void handleTokenValidated(Event e) {
        String customerToken = e.getArgument(0, String.class);
        // find out the corresponding pending payment
        Payment payment = pendingPayments.get(customerToken);
        // conduct the bank transaction

        // remove the payment from pending
        pendingPayments.remove(customerToken);
        // record the completed payment

        // publish "PaymentCompleted" event
        String description = "";
        Event event = new Event("PaymentCompleted", new Object[] {description});
        queue.publish(event);
    }
}
