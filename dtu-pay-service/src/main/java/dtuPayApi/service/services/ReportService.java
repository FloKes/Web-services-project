package dtuPayApi.service.services;

import dtuPayApi.service.CorrelationId;
import dtuPayApi.service.dtos.ReportDTO;
import messaging.Event;
import messaging.MessageQueue;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ReportService {

    public static final String CUSTOMER_REPORT_REQUESTED = "CustomerReportRequested";
    public static final String CUSTOMER_REPORT_PROVIDED = "CustomerReportProvided";
    private MessageQueue queue;
    private Map<CorrelationId, CompletableFuture<ReportDTO>> pendingCustomerReportRequest = new ConcurrentHashMap<>();

    public ReportService(MessageQueue q) {
        queue = q;
        queue.addHandler(CUSTOMER_REPORT_PROVIDED, this::handleCustomerReportProvided);
    }

    public ReportDTO requestCustomerReport(String customerAccountId) {
        var correlationId = CorrelationId.randomId();
        pendingCustomerReportRequest.put(correlationId,new CompletableFuture<>());
        Event event = new Event(CUSTOMER_REPORT_REQUESTED, new Object[] { customerAccountId, correlationId });
        queue.publish(event);
        return pendingCustomerReportRequest.get(correlationId).join();
    }

    public void handleCustomerReportProvided(Event e) {
        var receivedReports = e.getArgument(0, ReportDTO.class);
        var correlationId = e.getArgument(1, CorrelationId.class);
        System.out.println(receivedReports);
        pendingCustomerReportRequest.get(correlationId).complete(receivedReports);
    }
}
