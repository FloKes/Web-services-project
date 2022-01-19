package dtuPayApi.service.services;

import dtuPayApi.service.CorrelationId;
import dtuPayApi.service.dtos.AccountDTO;
import dtuPayApi.service.dtos.PaymentDTO;
import messaging.Event;
import messaging.MessageQueue;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class AccountService {
    public static final String ACCOUNT_REQUESTED = "AccountRequested";
    public static final String ACCOUNT_PROVIDED = "AccountProvided";
    public static final String ACCOUNT_EXISTS = "AccountExists";
    private MessageQueue queue;
    private Map<CorrelationId, CompletableFuture<AccountDTO>> pendingAccountRequests = new ConcurrentHashMap<>();

    public AccountService(MessageQueue q) {
        queue = q;
        queue.addHandler(ACCOUNT_PROVIDED, this::handleAccountProvided);
        queue.addHandler(ACCOUNT_EXISTS, this::handleAccountExists);
    }

    public AccountDTO requestAccount(AccountDTO accountDTO) {
        var correlationId = CorrelationId.randomId();
        pendingAccountRequests.put(correlationId,new CompletableFuture<>());
        Event event = new Event(ACCOUNT_REQUESTED, new Object[] { accountDTO, correlationId });
        queue.publish(event);
        return pendingAccountRequests.get(correlationId).join();
    }

    public void handleAccountProvided(Event e) {
        var accountDTO = e.getArgument(0, AccountDTO.class);
        var correlationId = e.getArgument(1, CorrelationId.class);
        System.out.println(accountDTO);
        pendingAccountRequests.get(correlationId).complete(accountDTO);
    }

    public void handleAccountExists(Event e) {
        var accountDTO = e.getArgument(0, AccountDTO.class);
        var correlationId = e.getArgument(1, CorrelationId.class);
        System.out.println(accountDTO);
        pendingAccountRequests.get(correlationId).complete(accountDTO);
    }
}
