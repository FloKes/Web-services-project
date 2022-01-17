Feature: Token
  Scenario: Token creation
    Given The customerID is "Porkolt1"
    When the token is created
    Then tokenID is valid

  Scenario: Token creation, too many tokens
    Given The customerID is "Béla"
    And he has 2 tokens already
    When the token is created
    Then the error message is "Too many tokens"

  Scenario: Token validation
    Given The customerID is "Pörkölt"
    And the token is created
    When his token is being checked
    Then the validation is successful

  Scenario: Token deletion
    Given The customerID is "Porkolt"
    And the token is created
    When his token is being deleted
    Then the token is deleted

