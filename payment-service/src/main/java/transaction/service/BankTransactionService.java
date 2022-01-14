package transaction.service;

import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import java.math.BigDecimal;

public class BankTransactionService {
    private BankService bank;
    public BankTransactionService() {
        this.bank = new BankServiceService().getBankServicePort();
    }

    public String getBankAccount(String accountId) throws BankServiceException_Exception {

        Account account = bank.getAccount(accountId);

        return account.getId();
    }

    public String transferMoney( String clientID, String merchantID, Integer amount) throws BankServiceException_Exception {
        try {
            bank.transferMoneyFromTo( clientID, merchantID, new BigDecimal(amount), "transfer" + String.valueOf(amount) + "from" + clientID + "to" + merchantID);
            return "The" + amount+" has been successfully transferred from: "+clientID+" to: "+ merchantID;
        } catch (BankServiceException_Exception e) {
            throw e;
        }

    }

    public void setBalance(String accountId, Integer amount) throws BankServiceException_Exception {
        Account account = bank.getAccount(accountId);
        account.setBalance(BigDecimal.valueOf(amount));
    }

    public String getBalance(String accountId) throws BankServiceException_Exception {

        Account account = bank.getAccount(accountId);

        return account.getBalance().toString();
    }
}
