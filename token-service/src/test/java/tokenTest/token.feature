Feature: Token
  Scenario: Token creation
    Given The customerID is "Alice"
    When the token is created
    Then tokenID is valid

  Scenario: Token creation 1
    Given The customerID is "Alice2"
    And he has 1 tokens already
    When the token is created
    Then tokenID is valid

  Scenario: Token creation, too many tokens
    Given The customerID is "Bob"
    And he has 2 tokens already
    When the token is created
    Then the error message is "Too many tokens"

  Scenario: Token validation
    Given The customerID is "George"
    And the token is created
    When his token is being checked
    Then the validation is "successful"

  Scenario: Token validation, failed
    Given The customerID is "Elisa"
    When his token is being checked
    Then the validation is "unsuccessful"

  Scenario: Token deletion
    Given The customerID is "Karen"
    And the token is created
    When his token is being deleted
    Then the token is deleted

  Scenario: Token deletion, not found
    Given The customerID is "Leo"
    When his token is being deleted
    Then the token is deleted

