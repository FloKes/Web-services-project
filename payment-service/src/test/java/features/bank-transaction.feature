Feature: Transaction
  #To test the bank service and get it up and running

  Scenario: Successful payment
    Given the "customer" "Florian" "Kesten" with CPR "000000-1234" has a bank account with balance 1000
    And that the "customer" is registered with DTU Pay
    Given the "merchant" "Fakta" "Faktorial" with CPR "111111-5672" has a bank account with balance 2000
    And that the "merchant" is registered with DTU Pay
    When the merchant initiates a payment for 100 kr by the customer
    Then the balance of the "customer" at the bank is 900 kr
    And the balance of the "merchant" at the bank is 2100 kr


#  Scenario: Create new DTU pay customer
#    Given the customer "Gunn" "Hentze" with CPR "010100-1534" has a bank account with balance 1000
#    When that the customer is registered with DTU Pay
#    Then the registration succeeds

#  Scenario: Create new DTU Pay merchant
#    Given the merchant "Netto" "NettoLonger" with CPR "010110-1534" has a bank account with balance 1000
#    When that the merchant is registered with DTU Pay
#    Then the registration succeeds