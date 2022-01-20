package report.service;

import DTOs.MerchantReportDTO;
import DTOs.PaymentReportDTO;
import DTOs.ReportDTO;
import domain.MerchantPayment;
import domain.Payment;
import mappers.Mapper;
import messaging.Event;
import messaging.MessageQueue;

import java.util.List;

public class ReportService {
    MessageQueue queue;
    ReportRepository repository;

    // Event received
    public static final String PAYMENT_COMPLETED = "PaymentCompletedForReport";
    public static final String CUSTOMER_REPORT_REQUESTED = "CustomerReportRequested";
    public static final String MERCHANT_REPORT_REQUESTED = "MerchantReportRequested";
    public static final String MANAGER_REPORT_REQUESTED = "ManagerReportRequested";

    // Event sent
    public static final String CUSTOMER_REPORT_PROVIDED = "CustomerReportProvided";
    public static final String MERCHANT_REPORT_PROVIDED = "MerchantReportProvided";
    public static final String MANAGER_REPORT_PROVIDED = "ManagerReportProvided";

    public ReportService(MessageQueue q, ReportRepository repository) {
        this.queue = q;
        this.repository = repository;
        this.queue.addHandler(PAYMENT_COMPLETED, this::handlePaymentSucceeded);
        this.queue.addHandler(CUSTOMER_REPORT_REQUESTED, this::handleCustomerReportRequested);
        this.queue.addHandler(MERCHANT_REPORT_REQUESTED, this::handleMerchantReportRequested);
        this.queue.addHandler(MANAGER_REPORT_REQUESTED, this::handleManagerReportRequested);

    }

    public void handlePaymentSucceeded(Event ev) {
        var paymentReportDTO = ev.getArgument(0, PaymentReportDTO.class);
        Payment payment = new Payment();
        MerchantPayment merchantPayment = new MerchantPayment();
        Mapper.PaymentReportDTOtoPaymentMapper(paymentReportDTO, payment);
        Mapper.PaymentReportDTOtoMerchantPaymentMapper(paymentReportDTO, merchantPayment);
        repository.addPaymentToCustomerList(payment.getCustomerId(), payment);
        repository.addPaymentToMerchantList(payment.getMerchantId(), merchantPayment);
        repository.addPaymentToManagerList(payment);
    }

    public void handleCustomerReportRequested(Event ev) {
        var customerId = ev.getArgument(0, String.class);
        List<Payment> customerReports = repository.getCustomerReportById(customerId);
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportList(customerReports);
        Event event = new Event(CUSTOMER_REPORT_PROVIDED, new Object[] { reportDTO });
        queue.publish(event);
    }

    public void handleMerchantReportRequested(Event ev) {
        var merchantId = ev.getArgument(0, String.class);
        List<MerchantPayment> merchantReports = repository.getMerchantReportById(merchantId);
        MerchantReportDTO reportDTO = new MerchantReportDTO();
        reportDTO.setMerchantReportList(merchantReports);
        Event event = new Event(MERCHANT_REPORT_PROVIDED, new Object[] { reportDTO });
        queue.publish(event);

    }

    public void handleManagerReportRequested(Event ev) {
        var correlationId = ev.getArgument(0, CorrelationId.class);
        List<Payment> managerReports = repository.getManagerReport();
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportList(managerReports);
        Event event = new Event(MANAGER_REPORT_PROVIDED, new Object[] { reportDTO });
        queue.publish(event);
    }
}
