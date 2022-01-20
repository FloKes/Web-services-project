Feature: Request report feature

  Scenario: Successful customer request of report
    Given A merchant "Micro" "Service" with CPR "112233-2345" has a bank account with balance 400 and is registered to DTU pay
    And a customer "Gunn" "Hentze" with CPR "122892-1234" has a bank account with balance 200 and is registered to DTU pay
    And the customer has requested tokens
    And one successful payment of 100 kr from customer to merchant has happened
    When the customer request a report of the payments
    Then the customer receives a report with 1 payment

  Scenario: Successful merchant request of report
    Given A merchant "Service" "Micro" with CPR "332211-2345" has a bank account with balance 400 and is registered to DTU pay
    And a customer "Hentze" "Gunn" with CPR "435216-1234" has a bank account with balance 200 and is registered to DTU pay
    And the customer has requested tokens
    And one successful payment of 100 kr from customer to merchant has happened
    When the merchant request a report of the payments
    Then the merchant receives a report with 1 payment

  Scenario: Successful manager request of report
    When the manager request a report of the payments
    Then the manager receives a report with payments
