Feature: Registration
  Scenario: Successful Registration to DTUPay
    When a "AccountRequested" event for an account is received
    Then the "AccountProvided" event is sent
    Then the account gets an account id

  #Scenario: Successful Registration to DTUPay
  #  When a "AccountRequested" event for an account is received
  #  Then the "AccountProvided" event is sent
  #  When a "GetAccountRequested" event for getting an account is received
    # Then the "GetAccountProvided" event is sent
    #Then the account is returned



    ##Given the merchant "Netto" "Nettosen" with CPR "010101-1234" has a bank account with balance 2000
    ##And that the merchant is registered with DTU Pay
    ##When the merchant, "Netto", initiates a payment for 100 kr by the customer, "Gunn"
    ##Then the payment is successful
    ##And the balance of the customer "Gunn" at the bank is 900 kr
    ##And the balance of the merchant "Netto" at the bank is 2100 kr
