Feature: Token
  Scenario: Token creation
    Given The customerID is "Porkolt1"
    When the token is created
    Then tokenID is valid

  Scenario: Token creation, too many tokens
    Given The customerID is "Béla"
    And he has 2 tokens
    When the token is created
    Then the message is "Error: too many strings"

  Scenario: Token creation, added to map
    Given The customerID is "Porkolt2"
    And The customerID2 is "Porkolt2"
    When the tokens are added to tokenList
    Then tokenList size is 2

  Scenario: Token validation
    Given The customerID is "Porkolt"
    And the token is created
    When his token is being checked
    Then the validation is successful

  Scenario: Token deletion
    Given The customerID is "Porkolt"
    And the token is created
    When his token is being deleted
    Then the token is deleted

