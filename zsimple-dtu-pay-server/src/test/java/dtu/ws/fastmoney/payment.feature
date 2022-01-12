Feature: Payment
  Scenario: Successful Payment
    Given the customer "Gunn" "Hentze" with CPR "010100-1234" has a bank account with balance 1000
    And that the customer is registered with DTU Pay
    Given the merchant "Netto" "Nettosen" with CPR "010101-1234" has a bank account with balance 2000
    And that the merchant is registered with DTU Pay
    When the merchant, "Netto", initiates a payment for 100 kr by the customer, "Gunn"
    Then the payment is successful
    And the balance of the customer "Gunn" at the bank is 900 kr
    And the balance of the merchant "Netto" at the bank is 2100 kr
