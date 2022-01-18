Feature: Payment Processing

  Scenario: Successful Payment with registration
    Given merchant with name "Soft" "Micro" with CPR "783472-4235" has a bank account with 1000 kr
    And customer with name "Bingkun" "Wu" with CPR "123456-2234" has a bank account with 100 kr
    When the two accounts are registering at the same time
    Then the customer and merchant has different id
    When the customer "Bingkun" "Wu" has no tokens
    And the customer "Bingkun" "Wu" asks for a token
    Then the customer "Bingkun" "Wu" receives 6 tokens
    When the merchant "Soft" "Micro" initializes a payment with the customer "Bingkun" "Wu" of 100 kr to the DTUPay
    Then the payment is successful



