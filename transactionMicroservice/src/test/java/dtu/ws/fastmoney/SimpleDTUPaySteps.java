//package dtu.ws.fastmoney;
//
//import io.cucumber.java.After;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//
//import static org.junit.Assert.assertEquals;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class SimpleDTUPaySteps {
//
//    BankService bank = new BankServiceService().getBankServicePort();
//    HashMap<String, User> accountsById = new HashMap<>();
//    HashMap<String, String> accountsByName = new HashMap<>();
//    List<String> accountIds = new ArrayList<>();
//    BusinessLogic bl = new BusinessLogic();
//
//    @Given("the customer {string} {string} with CPR {string} has a bank account with balance {int}")
//    public void createCustomerAccount(String firstName, String lastName, String CPR, int balance){
//
//        /*User user = new User();
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setCprNumber(CPR);
//        try {
//            String accountId = bank.createAccountWithBalance(user, BigDecimal.valueOf(balance));
//            accountIds.add(accountId);
//            accountsById.put(accountId, user);
//            accountsByName.put(firstName, accountId);
//        }
//        catch (BankServiceException_Exception e) {
//            //TODO: handle exception
//        }*/
//
//    }
//
//    @Given("that the customer is registered with DTU Pay")
//    public void customerIsRegistered(){
//        //TODO: check if customer is registered
//    }
//
//    @Given("the merchant {string} {string} with CPR {string} has a bank account with balance {int}")
//    public void createMerchantAccount(String firstName, String lastName, String CPR, int balance){
//        User user = new User();
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setCprNumber(CPR);
//        try {
//
//            String accountId = bank.createAccountWithBalance(user, BigDecimal.valueOf(balance));
//            accountIds.add(accountId);
//            accountsById.put(accountId, user);
//            accountsByName.put(firstName, accountId);
//        }
//        catch (BankServiceException_Exception e) {
//            //TODO: handle exception
//        }
//    }
//
//    @Given("that the merchant is registered with DTU Pay")
//    public void merchantIsRegistered(){
//        //TODO: check if merchant is registered
//    }
//
//    @When("the merchant, {string}, initiates a payment for {int} kr by the customer, {string}")
//    public void merchantInitiatesPayment(String merchant, int payment, String customer) {
//        try {
//            bank.transferMoneyFromTo(accountsByName.get(customer),accountsByName.get(merchant),BigDecimal.valueOf(payment), customer + " pays " + merchant + " " + payment + "kr.");
//            String merchantAccountId = accountsByName.get(merchant);
//            Account merchantAccount = bank.getAccount(merchantAccountId);
//            int merchantCurrentBalance = merchantAccount.getBalance().intValue();
//            bank.getAccount(merchantAccountId).setBalance(BigDecimal.valueOf(merchantCurrentBalance + payment));
//            String customerAccountId = accountsByName.get(customer);
//            Account customerAccount = bank.getAccount(customerAccountId);
//            int customerCurrentBalance = customerAccount.getBalance().intValue();
//            bank.getAccount(customerAccountId).setBalance(BigDecimal.valueOf(customerCurrentBalance - payment));
//        } catch (BankServiceException_Exception e) {
//            //TODO: handle exception
//        }
//    }
//
//    @Then("the payment is successful")
//    public void paymentSuccessful(){
//
//    }
//
//    @Then("the balance of the customer {string} at the bank is {int} kr")
//    public void customerBalance(String customer, int balance) {
//
//        try {
//            Account account = bank.getAccount(accountsByName.get(customer));
//            assertEquals(balance, account.getBalance().intValue());
//        } catch (BankServiceException_Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Then("the balance of the merchant {string} at the bank is {int} kr")
//    public void merchantBalance(String merchant, int balance) {
//        try {
//            Account account = bank.getAccount(accountsByName.get(merchant));
//            assertEquals(balance, account.getBalance().intValue());
//        } catch (BankServiceException_Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @After
//    public void removeAccounts() {
//        for (String accountId : accountIds) {
//            try {
//                bank.retireAccount(accountId);
//            }
//            catch (BankServiceException_Exception e) {
//                //TODO: handle exception
//            }
//        }
//        /*
//        for (Map.Entry account : accounts.entrySet()) {
//            try {
//                bank.retireAccount((String)account.getKey());
//            }
//            catch (BankServiceException_Exception e) {
//                //TODO: handle exception
//            }
//        }*/
//    }
//}
//
///*
//@Given("that the customer's bank account has balance {int}")
//public void customerWithBalance(String name, int balance) {
//    bank.
//
//}
//@Given("that the merchant's bank account has balance {int}")
//public void merchantWithBalance(int balance){
//
//}
//*/
///*
//Feature: Payment
//        Scenario: Successful Payment
//        Given a customer with a bank account with balance 1000
//        And that the customer is registered with DTU Pay
//        Given a merchant with a bank account with balance 2000
//        And that the merchant is registered with DTU Pay
//        When the merchant initiates a payment for 100 kr by the customer
//        Then the payment is successful
//        And the balance of the custoemr at the bank is 900 kr
//        And the balance of the merchant at the bank is 2100 kr
//*/
