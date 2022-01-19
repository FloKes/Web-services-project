package account.service;

import account.service.domain.Account;
import account.service.dtos.AccountDTO;
import account.service.dtos.BankAccountRequestDTO;
import account.service.dtos.Mapper;
import io.cucumber.java.an.E;
import messaging.Event;
import messaging.MessageQueue;

public class AccountService {
    public static final String ACCOUNT_REQUESTED = "AccountRequested";
    public static final String ACCOUNT_PROVIDED = "AccountProvided";
    public static final String ACCOUNT_EXISTS = "AccountExists";

    public static final String BANK_ACCOUNT_REQUESTED = "BankAccountRequested";
    public static final String BANK_ACCOUNT_PROVIDED = "BankAccountProvided";
    AccountManager accountManager = AccountManager.getInstance();
    MessageQueue queue;

    public AccountService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler(ACCOUNT_REQUESTED, this::handleAccountRequested);
        this.queue.addHandler(BANK_ACCOUNT_REQUESTED, this::handleGetBankAccountRequested);
    }

    public void handleAccountRequested(Event ev){
        var account = new Account();
        var accountDTOReceived = ev.getArgument(0, AccountDTO.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        Mapper.mapAccountDTOToAccount(accountDTOReceived, account);
//        System.out.println(account);
        AccountDTO accountDTO = new AccountDTO();
        Mapper.mapAccountToDTO(account, accountDTO);
        try {
            var accountId = accountManager.createAccount(account);
            accountDTO.setAccountId(accountId);
            System.out.println("Accoutnservice: " + accountDTO);
            Event event = new Event(ACCOUNT_PROVIDED, new Object[] { accountDTO, correlationId });
            queue.publish(event);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            accountDTO.setErrorMessage(e.getMessage());
            Event event = new Event(ACCOUNT_EXISTS, new Object[] {accountDTO, correlationId});
            queue.publish(event);
        }
    }

    public void handleGetBankAccountRequested(Event ev) {
        BankAccountRequestDTO bankAccountRequestDTO = ev.getArgument(0, BankAccountRequestDTO.class);
        CorrelationId correlationId = ev.getArgument(1, CorrelationId.class);
        Account customerAccount = accountManager.getAccount(bankAccountRequestDTO.getCustomerId());
        Account merchantAccount = accountManager.getAccount(bankAccountRequestDTO.getMerchantId());
        bankAccountRequestDTO.setCustomerBankAccount(customerAccount.getBankAccount());
        bankAccountRequestDTO.setMerchantBankAccount(merchantAccount.getBankAccount());
        Event event = new Event(BANK_ACCOUNT_PROVIDED, new Object[] {bankAccountRequestDTO, correlationId});
        queue.publish(event);
    }
}



