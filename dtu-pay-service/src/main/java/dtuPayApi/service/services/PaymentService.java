package dtuPayApi.service.services;

import dtuPayApi.service.CorrelationId;
import dtuPayApi.service.Token;
import dtuPayApi.service.dtos.PaymentDTO;
import messaging.Event;
import messaging.MessageQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentService {

    public static final String PAYMENT_COMPLETED = "PaymentCompleted";
    public static final String PAYMENT_INITIATED = "PaymentInitiated";
    private MessageQueue queue;
    private Map<CorrelationId, CompletableFuture<PaymentDTO>> pendingPayments = new ConcurrentHashMap<>(); // payments waiting for token validation, <correlationID, payment>
    public PaymentService(MessageQueue q) {
        queue = q;
        queue.addHandler(PAYMENT_COMPLETED, this::handlePaymentCompleted);
    }

    public PaymentDTO addPayment(PaymentDTO paymentDTO) {
        var correlationId = CorrelationId.randomId();
        pendingPayments.put(correlationId, new CompletableFuture<>());
        paymentDTO.setCorrelationId(correlationId);
        Event event = new Event(PAYMENT_INITIATED, new Object[] {paymentDTO});
        queue.publish(event);
        return pendingPayments.get(correlationId).join();
    }

    public void handlePaymentCompleted(Event e) {
        PaymentDTO paymentDTO = e.getArgument(0, PaymentDTO.class);
        pendingPayments.get(paymentDTO.getCorrelationId()).complete(paymentDTO);
    }
}