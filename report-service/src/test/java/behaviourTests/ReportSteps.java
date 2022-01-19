package behaviourTests;

import DTOs.PaymentReportDTO;
import domain.Payment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import token.service.ReportRepository;
import token.service.ReportService;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;

public class ReportSteps {

    private MessageQueue queue = mock(MessageQueue.class);
    private ReportRepository mockRepository = mock(ReportRepository.class);
    private ReportRepository repository = new ReportRepository();
    private ReportService service = new ReportService(queue, repository);
    private ReportService service2 = new ReportService(queue, mockRepository);
    PaymentReportDTO payment;

    @Given("a payment with paymentId {string}, customerId {string}, merchantId {string}, amount {int}, and description {string}")
    public void aPaymentWithPaymentIdCustomerIdMerchantIdAmountAndDescription(String paymentId, String customerId, String merchantId, int amount, String description) {
        payment = new PaymentReportDTO();
        payment.setPaymentId(paymentId);
        payment.setMerchantId(merchantId);
        payment.setCustomerId(customerId);
        payment.setAmount(BigDecimal.valueOf(amount));
        payment.setDescription(description);
    }
    @When("a {string} event is received for the payment")
    public void aEventIsReceivedForThePayment(String event) {
        service.handlePaymentSucceeded(new Event(event, new Object[]{ payment}));
    }
    @Then("the payment is added to the merchant report")
    public void thePaymentIsAddedToTheMerchantReport() {

    }
    @Then("the payment is added to the customer report")
    public void thePaymentIsAddedToTheCustomerReport() {

    }
    @Then("the payment is added to the manager report")
    public void thePaymentIsAddedToTheManagerReport() {

    }
}
