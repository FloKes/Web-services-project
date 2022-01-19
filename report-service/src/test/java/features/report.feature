Feature: Report

  Scenario: Add payment to report request handling
    Given a payment with paymentId "0", customerId "1", merchantId "2", amount 100, and description "test"
    When a "PaymentCompletedForReport" event is received for the payment
    Then the payment is added to the merchant report
    And the payment is added to the customer report
    And the payment is added to the manager report

