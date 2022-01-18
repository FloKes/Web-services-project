Feature: TokenRequestFeature
    Scenario: Request a token
      Given the customer with id "1234" has no token
      When the customer asks for a token
      Then the customer receives a token "4321"


