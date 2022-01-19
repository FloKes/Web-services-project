package token.service;

import DTOs.PaymentDTO;
import messaging.Event;
import messaging.MessageQueue;

import static java.util.stream.Collectors.groupingBy;
import java.util.*;

public class ReportService {
    MessageQueue queue;
    ReportRepository repository;

    // Event received
    public static final String PAYMENT_COMPLETED = "PaymentCompleted";

    // Event sent

    public ReportService(MessageQueue q, ReportRepository repository) {
        this.queue = q;
        this.repository = repository;
        this.queue.addHandler(PAYMENT_COMPLETED, this::handlePaymentSucceeded);

    }

    //*************************************************************************************
    //This is new

    public void handlePaymentSucceeded(Event ev) {
        var paymentDTO = ev.getArgument(0, PaymentDTO.class);

    }
}
