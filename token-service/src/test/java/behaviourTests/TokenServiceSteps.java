package behaviourTests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TokenServiceSteps {
//    MessageQueue queue = mock(MessageQueue.class);
//    PaymentService paymentService = new PaymentService(queue);
//    Payment payment;
//    String merchantId, customerId;
//    String merchantBankAccountId, customerBankAccountId;
//    TokenValidationDTO tokenValidationDTO;
//    BankAccountRequestDTO bankAccountRequestDTO;
//    BankService bankService = new BankServiceService().getBankServicePort();
//    BankTransactionService bankTransactionService = new BankTransactionService();
//    List<String> accountIds = new ArrayList<>();
//
//    @Given("a merchant {string} has a bank account with {int} kr")
//    public void merchantHasBank(String merchantId, Integer amount) {
//        this.merchantId = merchantId;
//        User user = new User();
//        user.setCprNumber("270791");
//        user.setFirstName("Bingkun");
//        user.setLastName("Wu");
//        try {
//            this.merchantBankAccountId = bankService.createAccountWithBalance(user, BigDecimal.valueOf(amount));
//            accountIds.add(this.merchantBankAccountId);
//        } catch (BankServiceException_Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Given("a customer {string} has a bank account with {int} kr")
//    public void customerHasBank(String customerId, Integer amount) {
//        this.customerId = customerId;
//        User user = new User();
//        user.setCprNumber("897424");
//        user.setFirstName("Yufan");
//        user.setLastName("Du");
//        try {
//            this.customerBankAccountId = bankService.createAccountWithBalance(user, BigDecimal.valueOf(amount));
//            accountIds.add(this.customerBankAccountId);
//        } catch (BankServiceException_Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @When("a {string} event is received with {int} kr payment amount")
//    public void aEventForPayment(String eventType, Integer amount) {
//        var correlationId = CorrelationId.randomId();
//        payment = new Payment();
//        payment.setCorrelationId(correlationId);
//        payment.setCustomerToken("1234");
//        payment.setMerchantId(merchantId);
//        payment.setAmount(BigDecimal.valueOf(amount));
//        assertNull(payment.getDescription());
//        PaymentDTO paymentDTO = new PaymentDTO();
//        Mapper.mapPaymentToDTO(payment, paymentDTO);
//        paymentService.handlePaymentInitiated(new Event(eventType, new Object[] {paymentDTO}));
//    }
//
//    @Then("the {string} event is sent to validate the token")
//    public void theTokenEventIsSent(String eventType) {
//        tokenValidationDTO = new TokenValidationDTO();
//        Mapper.mapTokenValidationDTO(this.payment, tokenValidationDTO);
//        var event = new Event(eventType, new Object[] {tokenValidationDTO});
//        verify(queue).publish(event);
//    }
//
//    @When("the {string} event is received with non-empty customerId")
//    public void theEventIsReceivedWithCustomerId(String eventType) {
//        tokenValidationDTO.setCustomerId(customerId);
//        paymentService.handleTokenValidated(new Event(eventType, new Object[] {tokenValidationDTO}));
//    }
//
//    @Then("the {string} event is sent to inquire the bankAccountId")
//    public void theEventIsSentToInquiryBankAccount(String eventType) {
//        bankAccountRequestDTO = new BankAccountRequestDTO();
//        Mapper.mapBankAccountRequestDTO(payment, tokenValidationDTO, bankAccountRequestDTO);
//        var event = new Event(eventType, new Object[] {bankAccountRequestDTO});
//        verify(queue).publish(event);
//    }
//
//    @When("the {string} event is received with non-empty bankAccountIds")
//    public void theEventIsReceivedWithBankAccount(String eventType) {
//        bankAccountRequestDTO.setCustomerBankAccount(customerBankAccountId);
//        bankAccountRequestDTO.setMerchantBankAccount(merchantBankAccountId);
//        paymentService.handleBankAccountProvided(new Event(eventType, new Object[] {bankAccountRequestDTO}));
//    }
//
//    @Then("the {string} event is sent and payment completes")
//    public void thePaymentEventIsSentAndNotPending(String eventType) {
//        PaymentDTO paymentDTO = new PaymentDTO();
//        Mapper.mapPaymentToDTO(payment, paymentDTO);
//        var event = new Event(eventType, new Object[] {paymentDTO});
//        verify(queue).publish(event);
//    }
}
