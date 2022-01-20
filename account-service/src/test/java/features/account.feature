Feature: Registration
  Scenario: Successful Registration to DTUPay
    When a "AccountRequested" event for a customer with name "Josephine", surname "Mellin", cpr "000000-1234", bank account "1234" is received
    And the account gets an account with id "1"
    Then the "AccountProvided" event is sent

  Scenario: Existing account
    When a "AccountRequested" event for a customer with name "Josephine", surname "Mellin", cpr "000000-1234", bank account "1234" is received
    When a "AccountRequested" event for a customer with name "Josephine", surname "Mellin", cpr "000000-1234", bank account "1234" is received
    Then the "AccountExists" event is sent with error message "Account already exists"

  Scenario: Delete account
    When a "AccountRequested" event for a customer with name "Florian", surname "Keste", cpr "000000-5321", bank account "1111" is received
    Then the account gets an account with id "1"
    When the AccountDeletionRequested event with accountId "1" is received
    Then the AccountDeleted event is sent

  Scenario: Account check on non existent account
    When a AccountCheckRequested event with accountId "0" is received
    Then the AccountCheckResultProvided event is sent with 0 as value

  Scenario: Account check on existent account
    When a "AccountRequested" event for a customer with name "Florian", surname "Keste", cpr "000000-5321", bank account "1111" is received
    Then the account gets an account with id "1"
    When a AccountCheckRequested event with accountId "1" is received
    Then the AccountCheckResultProvided event is sent with 1 as value

  # TODO Add scenario to delete non existent account
#  Scenario: Successful Registration to DTUPay
#    When a "AccountRequested" event for an account is received
#    Then the "AccountProvided" event is sent
#    When a "BankAccountRequested" event for getting an account is received
#    Then the "BankAccountProvided" event is sent
#    Then the bank account is returned



    ##Given the merchant "Netto" "Nettosen" with CPR "010101-1234" has a bank account with balance 2000
    ##And that the merchant is registered with DTU Pay
    ##When the merchant, "Netto", initiates a payment for 100 kr by the customer, "Gunn"
    ##Then the payment is successful
    ##And the balance of the customer "Gunn" at the bank is 900 kr
    ##And the balance of the merchant "Netto" at the bank is 2100 kr
