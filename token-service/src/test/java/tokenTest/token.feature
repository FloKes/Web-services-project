Feature: Token
  Scenario: Token creation
    Given The customerID is "Porkolt"
    When the token is created
    Then tokenID is "PorkoltSzaft"

  Scenario: Token creation, hashmap
    Given The customerID is "Porkolt"
    And The customerID2 is "Porkolt2"
    When the tokens are added to tokenList
    Then tokenList size is 2
