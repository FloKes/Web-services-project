Feature: TokenRequestFeature

#    Scenario: Customer with no tokens request a token
#      Given the customer with id "Florian" has no tokens
#      When the customer asks for a token
#      Then the customer receives 6 tokens

  Scenario: Registered Customer with 6 tokens request a token
    Given person with name "Flo" "Ki" with cpr "000000-5555", bank accountId "56741" is registered
    When the customer asks for 6 tokens
    Then the customer receives 6 tokens
    When the customer asks again for 6 token
    Then the customer receives 0 tokens response


  Scenario: Registered customer asks for a token
    Given person with name "Flo" "Ki" with cpr "000000-5555", bank accountId "56741" is registered
    When the customer asks for 6 tokens
    Then the customer receives 6 tokens

  Scenario: Unregistered customer asks for tokens
    Given person with id "56741" is not registered
    When the customer asks for 6 tokens
    Then the customer receives 0 tokens

  Scenario: Account Deletion deletes tokens
    Given person with name "Flo" "Ki" with cpr "000000-5555", bank accountId "56741" is registered
    When the customer asks for 6 tokens
    Then the customer receives 6 tokens
    When account is deleted
    And the customer asks again for 6 tokens
    Then the customer receives 0 tokens response


