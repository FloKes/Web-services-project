Feature: Token
  Scenario: Token creation
    Given The customerID is "Porkolt1"
    When the token is created
    Then tokenID is valid

  Scenario: Token creation, hashmap
    Given The customerID is "Porkolt2"
    And The customerID2 is "Porkolt2"
    When the tokens are added to tokenList
    Given The customerID is "Porkolt2"
    When the token is created
    Then tokenList size is 3
