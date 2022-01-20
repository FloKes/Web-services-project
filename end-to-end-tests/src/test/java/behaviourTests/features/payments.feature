Feature: Payment Processing

  Scenario: Successful Payment with registration
    Given merchant with name "Soft" "Micro" with CPR "783472-1111" has a bank account with 1000 kr
    And customer with name "Bingkun" "Wu" with CPR "123456-2222" has a bank account with 100 kr
    When the two accounts are registering at the same time
    Then the customer and merchant has different id
    When the customer has no tokens
    And the customer asks for a token
    Then the customer receives 6 tokens
    When the merchant "Soft" "Micro" initializes a payment with the customer "Bingkun" "Wu" of 100 kr to the DTUPay
    Then the payment is successful
    And the customer has 0 kr in the bank
    And the merchant has 1100 in bank

  Scenario: Unsucessful payment when insufficient funds
    Given merchant with name "Mirko" "Soft" with CPR "000000-1111" has a bank account with 1000 kr
    And customer with name "Florian" "Kesten" with CPR "000000-2222" has a bank account with 5 kr
    When the two accounts are registering at the same time
    Then the customer and merchant has different id
    When the customer has no tokens
    And the customer asks for a token
    Then the customer receives 6 tokens
    When the merchant "Mirko" "Soft" initializes a payment with the customer "Florian" "Kesten" of 100 kr to the DTUPay
    Then the payment is unsuccessful with error "Debtor balance will be negative"
    And the customer has 5 kr in the bank
    And the merchant has 1000 in bank

  Scenario: 6 Successful Payments, 7th unsuccessful
    Given merchant with name "Softer" "Microer" with CPR "783472-3333" has a bank account with 1000 kr
    And customer with name "Yoss" "Wu" with CPR "123456-4444" has a bank account with 100000 kr
    When the two accounts are registering at the same time
    Then the customer and merchant has different id
    When the customer has no tokens
    And the customer asks for a token
    Then the customer receives 6 tokens
    When the merchant "Softer" "Microer" initializes a payment with the customer "Yoss" "Wu" of 100 kr to the DTUPay
    Then the payment is successful
    When the merchant "Softer" "Microer" initializes a payment with the customer "Yoss" "Wu" of 100 kr to the DTUPay
    Then the payment is successful
    When the merchant "Softer" "Microer" initializes a payment with the customer "Yoss" "Wu" of 100 kr to the DTUPay
    Then the payment is successful
    When the merchant "Softer" "Microer" initializes a payment with the customer "Yoss" "Wu" of 100 kr to the DTUPay
    Then the payment is successful
    When the merchant "Softer" "Microer" initializes a payment with the customer "Yoss" "Wu" of 100 kr to the DTUPay
    Then the payment is successful
    When the merchant "Softer" "Microer" initializes a payment with the customer "Yoss" "Wu" of 100 kr to the DTUPay
    Then the payment is successful
    When the merchant "Softer" "Microer" initializes a payment with the customer "Yoss" "Wu" of 100 kr to the DTUPay
    Then the payment is unsuccessful

  Scenario: Unsuccessful Payment, invalid token
    Given merchant with name "Soft" "Micro" with CPR "783472-4235" has a bank account with 1000 kr
    And customer with name "Bingkun" "Wu" with CPR "123456-2234" has a bank account with 100 kr
    When the two accounts are registering at the same time
    Then the customer and merchant has different id
    When the customer "Bingkun" "Wu" has invalid tokens
    When the merchant "Soft" "Micro" initializes a payment with the customer "Bingkun" "Wu" of 100 kr to the DTUPay
    Then the payment is unsuccessful with error "Token invalid"

  Scenario: Unsuccessful Payment, wrong customer bank account
    Given merchant with name "Soft" "Micro" with CPR "783472-4235" has a bank account with 1000 kr
    And customer with name "Bingkun" "Wu" with CPR "123456-2234" has registered with wrong bank account
    When the two accounts are registering at the same time
    Then the customer and merchant has different id
    When the customer has no tokens
    And the customer asks for a token
    Then the customer receives 6 tokens
    When the merchant "Soft" "Micro" initializes a payment with the customer "Bingkun" "Wu" of 100 kr to the DTUPay
    Then the payment is unsuccessful with error "Debtor account does not exist"

  Scenario: Unsuccessful Payment, wrong merchant bank account
    Given merchant with name "Soft" "Micro" with CPR "783472-4235" has registered with wrong bank account
    And customer with name "Bingkun" "Wu" with CPR "123456-2234" has a bank account with 100 kr
    When the two accounts are registering at the same time
    Then the customer and merchant has different id
    When the customer has no tokens
    And the customer asks for a token
    Then the customer receives 6 tokens
    When the merchant "Soft" "Micro" initializes a payment with the customer "Bingkun" "Wu" of 100 kr to the DTUPay
    Then the payment is unsuccessful with error "Creditor account does not exist"

    Scenario: Unsucessful payment, invalid merchant
    Given merchant with name "Mirko" "Soft" with CPR "000000-1111" is not registered with DTUPay
    And customer with name "Florian" "Kesten" with CPR "000000-2222" has a bank account with 100 kr
    When the customer registers
    When the customer has no tokens
    And the customer asks for a token
    Then the customer receives 6 tokens
    When the merchant initializes a payment with the customer of 10 kr to DTUPay
    Then the payment is unsuccessful with error "Merchant not found"

#  Scenario: Unsucessful payment, invalid merchant
#    Given merchant with name "Mirko" "Soft" with CPR "000000-1111" has is not registered with DTUPay
#    And customer with name "Florian" "Kesten" with CPR "000000-2222" has a bank account with 100 kr
#    When the customer registers
#    When the customer "Florian" "Kesten" has no tokens
#    And the customer "Florian" "Kesten" asks for a token
#    Then the customer "Florian" "Kesten" receives 6 tokens
##    When the merchant initializes a payment with the customer of 10 kr to DTUPay
#    When the invalid merchant "Donald" "Trump" with CPR "423424-4324" initializes a payment with the customer "Florian" "Kesten" of 10 kr to the DTUPay
#    Then the payment is unsuccessful with error "Merchant not found"
