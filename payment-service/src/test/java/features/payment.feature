Feature: Payment service feature

  Scenario: Payment Conducted
    Given a merchant "mid1" has a bank account with 100 kr
    And a customer "cid1" has a bank account with 100 kr
    When a "PaymentInitiated" event is received with 100 kr payment amount
    Then the "TokenValidationRequested" event is sent to validate the token
    When the "TokenValidated" event is received with non-empty customerId
    Then the "BankAccountRequested" event is sent to inquire the bankAccountId
    When the "BankAccountReceived" event is received with non-empty bankAccountIds
    Then the "PaymentCompleted" event is sent and payment completes
    And the balance of merchant "mid" at the bank is 200 kr
    And the balance of customer "cid" at the bank is 0 kr

    Scenario: