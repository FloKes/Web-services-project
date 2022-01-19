package token.service;

import DTOs.PaymentReportDTO;
import domain.Payment;
import mappers.Mapper;
import messaging.Event;
import messaging.MessageQueue;

import static java.util.stream.Collectors.groupingBy;

public class ReportService {
    MessageQueue queue;
    ReportRepository repository;

    // Event received
    public static final String PAYMENT_COMPLETED = "PaymentCompletedForReport";
    public static final String CUSTOMER_REPORT_REQUESTED = "CustomerReportRequested";
    public static final String MERCHANT_REPORT_REQUESTED = "MerchantReportRequested";
    public static final String MANAGER_REPORT_REQUESTED = "ManagerReportRequested";

    // Event sent
    public static final String CUSTOMER_REPORT_PROVIDED = "CustomerReportRequested";
    public static final String MERCHANT_REPORT_PROVIDED = "MerchantReportRequested";
    public static final String MANAGER_REPORT_PROVIDED = "ManagerReportRequested";

    public ReportService(MessageQueue q, ReportRepository repository) {
        this.queue = q;
        this.repository = repository;
        this.queue.addHandler(PAYMENT_COMPLETED, this::handlePaymentSucceeded);
        this.queue.addHandler(CUSTOMER_REPORT_REQUESTED, this::handleCustomerReportRequested);
        this.queue.addHandler(MERCHANT_REPORT_REQUESTED, this::handleMerchentReportRequested);
        this.queue.addHandler(MANAGER_REPORT_REQUESTED, this::handleManagerReportRequested);

    }

    public void handlePaymentSucceeded(Event ev) {
        var paymentReportDTO = ev.getArgument(0, PaymentReportDTO.class);
        Payment payment = new Payment();
        Mapper.PaymentReportDTOtoPaymentMapper(paymentReportDTO, payment);
        repository.addPaymentToCustomerList(payment.getCustomerId(), payment);
        repository.addPaymentToMerchantList(payment.getCustomerId(), payment);
        repository.addPaymentToManagerList(payment);
    }

    public void handleCustomerReportRequested(Event ev) {
        var customerId = ev.getArgument(0, String.class);

    }

    public void handleMerchentReportRequested(Event ev) {
        var merchantId = ev.getArgument(0, String.class);

    }

    public void handleManagerReportRequested(Event ev) {
        var correlationId = ev.getArgument(0, String.class);

    }
}
