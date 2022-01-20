Feature: Token
#  //Scenario: Token creation
#    //Given The customerID is "Alice"
#    //When the token is created
#    //Then tokenID is valid

  Scenario: Token creation
    Given The customerID is "Alice"
    And he has 1 tokens already
    When the token is created
    Then tokenID is valid

  Scenario: Token creation 2 tokens
    Given The customerID is "Mary"
#    And he has 0 tokens already
    When the token is created
    Given The customerID is "Yoss"
#    And he has 0 tokens already
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

  Scenario: Token request handling
    When a "TokenCreationRequested" event for a "Joe" is received
    Then the token is created and its id is not null
    And the "TokenProvided" event is sent


  Scenario: Account deletion request handling
    When a TokenCreationRequested event for userId "Florian" is received
    Then the user has 6 tokens
    When a AccountDeleted event for userId "Florian" is received
    Then the all tokens for that user are deleted

