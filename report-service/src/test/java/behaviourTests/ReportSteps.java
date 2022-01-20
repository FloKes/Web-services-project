package behaviourTests;

import DTOs.MerchantReportDTO;
import DTOs.PaymentReportDTO;
import DTOs.ReportDTO;
import domain.CorrelationId;
import domain.MerchantPayment;
import domain.Payment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mappers.Mapper;
import messaging.Event;
import messaging.MessageQueue;
import report.service.ReportRepository;
import report.service.ReportService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReportSteps {
    private MessageQueue queue = mock(MessageQueue.class);
    private ReportRepository repository = new ReportRepository();
    private ReportService service = new ReportService(queue, repository);
    PaymentReportDTO payment;
    CorrelationId correlationId;

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
    public void anEventIsReceivedForThePayment(String event) {
        service.handlePaymentSucceeded(new Event(event, new Object[]{ payment}));
    }


    @Then("the payment is added to the merchant report")
    public void thePaymentIsAddedToTheMerchantReport() {
        List<MerchantPayment> merchantPayments = repository.getMerchantReportById(payment.getMerchantId());
        System.out.println(merchantPayments.size());
        MerchantPayment payment = merchantPayments.get(merchantPayments.size()-1);
        MerchantPayment expectedPayment = new MerchantPayment();
        Mapper.PaymentReportDTOtoMerchantPaymentMapper(this.payment, expectedPayment);
        System.out.println(payment);
        assertEquals(expectedPayment, payment);
    }

    @Then("the payment is added to the customer report")
    public void thePaymentIsAddedToTheCustomerReport() throws Exception {
        List<Payment> customerPayments = repository.getCustomerReportById(payment.getCustomerId());
        Payment payment = customerPayments.get(customerPayments.size()-1);
        Payment expectedPayment = new Payment();
        Mapper.PaymentReportDTOtoPaymentMapper(this.payment, expectedPayment);
        assertEquals(expectedPayment, payment);
    }


    @Then("the payment is added to the manager report")
    public void thePaymentIsAddedToTheManagerReport() {
        List<Payment> managerPayments = repository.getManagerReport();
        Payment payment = managerPayments.get(managerPayments.size()-1);
        Payment expectedPayment = new Payment();
        Mapper.PaymentReportDTOtoPaymentMapper(this.payment, expectedPayment);
        assertEquals(expectedPayment, payment);
    }


    @When("a {string} event is received for the customer report")
    public void anEventIsReceivedForTheCustomerReport(String eventName) {
        service.handleCustomerReportRequested(new Event(eventName, new Object[]{ payment.getCustomerId()}));
    }

    @Then("the {string} event is sent to customer")
    public void theEventIsSentToCustomer(String eventName) throws Exception {
        List<Payment> customerPayments = repository.getCustomerReportById(payment.getCustomerId());
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportList(customerPayments);
        Event event = new Event(eventName, new Object[] { reportDTO });
        verify(queue).publish(event);
    }


    @When("a {string} event is received for the merchant report")
    public void anEventIsReceivedForTheMerchantReport(String eventName) {
        service.handleMerchantReportRequested(new Event(eventName, new Object[]{ payment.getMerchantId()}));

    }

    @Then("the {string} event is sent to merchant")
    public void theEventIsSentToMerchant(String eventName) {
        List<MerchantPayment> merchantPayments = repository.getMerchantReportById(payment.getMerchantId());
        MerchantReportDTO reportDTO = new MerchantReportDTO();
        reportDTO.setMerchantReportList(merchantPayments);
        Event event = new Event(eventName, new Object[] { reportDTO });
        verify(queue).publish(event);

    }

    @When("a {string} event is received for the manager report")
    public void anEventIsReceivedForTheManagerReport(String eventName) {
        correlationId = CorrelationId.randomId();
        service.handleManagerReportRequested(new Event(eventName, new Object[]{ correlationId}));
    }

    @Then("the {string} event is sent to manager")
    public void theEventIsSentToManager(String eventName) {
        List<Payment> managerPayments = repository.getManagerReport();
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportList(managerPayments);
        Event event = new Event(eventName, new Object[] { reportDTO });
        verify(queue).publish(event);
    }


    @When("a {string} event is received for a customer with no payments")
    public void anEventReceivedForCustomerWithNoPayments(String eventName) {
        service.handleCustomerReportRequested(new Event(eventName, new Object[] {"testCustomer"}));
    }


    @Then("the {string} event is sent to customer with no payments")
    public void anEventProvidedForCustomerWithNoPayments(String eventName) {
        Event event = (new Event(eventName, new Object[] {"testCustomer"}));
        verify(queue).publish(event);
    }
}
