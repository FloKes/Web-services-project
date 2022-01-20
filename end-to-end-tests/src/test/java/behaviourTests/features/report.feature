Feature: Request report feature

  Scenario: Successful request of report
    Given A merchant "Micro" "Service" with CPR "112233-2345" has a bank account with balance 400 and is registered to DTU pay
    And a customer "Gunn" "Hentze" with CPR "122892-1234" has a bank account with balance 200 and is registered to DTU pay
    And the customer has requested tokens
    And one successful payment of 100 kr from customer to merchant has happened
    When the customer request a report of the payments
    Then the customer receives a report with 1 payment
