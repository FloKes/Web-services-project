Feature: DTUPay
#  Scenario: Successful Payment
#    Given a customer with id "cid1"
#    And a merchant with id "mid1"
#    When the merchant initiates a payment for 10 kr by the customer
#    Then the payment is successful
#
#  Scenario: List of payments
#    Given a successful payment of 10 kr from customer "cid1" to merchant "mid1"
#    When the manager asks for a list of payments
#    Then the list contains a payments where customer "cid1" paid 10 kr to merchant "mid1"

#  Scenario: Customer is not known
#    Given a customer with id "cid2"
#    And a merchant with id "mid1"
#    When the merchant initiates a payment for 10 kr by the customer
#    Then the payment is not successful
#    And an error message is returned saying "customer with id cid2 is unknown"

  Scenario: Create new DTU pay customer
    Given the customer "Gunn" "Hentze" with CPR "010100-1534" has a bank account with balance 1000
    When that the customer is registered with DTU Pay
    Then the registration succeeds

  Scenario: Create new DTU Pay merchant
    Given the merchant "Netto" "NettoLonger" with CPR "010110-1534" has a bank account with balance 1000
    When that the merchant is registered with DTU Pay
    Then the registration succeeds

  Scenario: Create new DTU pay customer
    Given the customer "Bingkun" "Wu" with CPR "123456-1234" does not have a bank account
    When that the customer is registered with DTU Pay
    Then the registration fails with "User does not have a bank account"

  Scenario: Successful balance
    Given the customer "Gunn" "Hentze" with CPR "000000-1534" has a bank account with balance 1000
    And that the customer is registered with DTU Pay
    Given the merchant "Netto" "Nettosen" with CPR "111111-1234" has a bank account with balance 2000
    And that the merchant is registered with DTU Pay
    Then the balance of the customer at the bank is 1000 kr
    And the balance of the merchant at the bank is 2000 kr

  Scenario: Successful payment
    Given the customer "Gunn" "Hentze" with CPR "000000-1534" has a bank account with balance 1000
    And that the customer is registered with DTU Pay
    Given the merchant "Netto" "Nettosen" with CPR "111111-1234" has a bank account with balance 2000
    And that the merchant is registered with DTU Pay
    When the merchant initiates a payment for 100 kr by the customer
    Then the payment is successful
    And the balance of the customer at the bank is 900 kr
    And the balance of the merchant at the bank is 2100 kr

  Scenario: Unsuccessful payment
    Given the customer "Gunn" "Hentze" with CPR "000000-1534" has a bank account with balance 100
    And that the customer is registered with DTU Pay
    Given the merchant "Netto" "Nettosen" with CPR "111111-1234" has a bank account with balance 2000
    And that the merchant is registered with DTU Pay
    When the merchant initiates a payment for 120 kr by the customer
    Then the payment is unsuccessful with error "Debtor balance will be negative"
    And the balance of the customer at the bank is 100 kr
    And the balance of the merchant at the bank is 2000 kr

