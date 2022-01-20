Feature: Report

  Scenario: Add payment to report request handling
    Given a payment with paymentId "0", customerId "1", merchantId "2", amount 100, and description "test"
    When a "PaymentCompletedForReport" event is received for the payment
    Then the payment is added to the merchant report
    And the payment is added to the customer report
    And the payment is added to the manager report


  Scenario: Add payment to report request handling
    Given a payment with paymentId "0", customerId "1", merchantId "2", amount 100, and description "test"
    When a "PaymentCompletedForReport" event is received for the payment
    Then the payment is added to the merchant report
    And the payment is added to the customer report
    And the payment is added to the manager report
    When a "CustomerReportRequested" event is received for the customer report
    Then the "CustomerReportProvided" event is sent to customer
    When a "MerchantReportRequested" event is received for the merchant report
    Then the "MerchantReportProvided" event is sent to merchant
    When a "ManagerReportRequested" event is received for the manager report
    Then the "ManagerReportProvided" event is sent to manager



