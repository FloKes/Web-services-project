package account.service;

import java.io.Serializable;

public class Account implements Serializable {
    private static final long serialVersionUID = 9024242488284806610L;
    private String firstname;
    private String lastname;
    private String cpr;
    private String accountId;
    private String bankAccount;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Account)) {
            return false;
        }
        var c = (Account) o;
        return firstname != null && firstname.equals(c.getFirstname()) &&
                lastname != null && lastname.equals(c.getLastname()) &&
                cpr != null && cpr.equals(c.getCpr()) &&
                accountId != null && accountId.equals(c.getAccountId()) &&
                bankAccount != null && bankAccount.equals(c.getBankAccount()) ||
                firstname == null && c.getFirstname() == null &&
                        lastname == null && c.getLastname() == null &&
                        cpr == null && c.getCpr() == null &&
                        accountId == null && c.getAccountId() == null &&
                        bankAccount == null && c.getBankAccount() == null;
    }

    @Override
    public int hashCode() {
        return firstname == null ? 0 : firstname.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Account:  %s", firstname);
    }
}
