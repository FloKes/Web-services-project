#Feature: TokenRequestFeature
#    Scenario: Customer with no tokens request a token
#      Given the customer with id "Florian" has no tokens
#      When the customer asks for a token
#      Then the customer receives 6 tokens
#
#  Scenario: Customer with 6 tokens request a token
#    Given the customer with id "43211" has no tokens
#    When the customer asks for a token
#    Then the customer receives 6 tokens
#    Given the customer with id "43211" has 6 tokens
#    When the customer asks again for a token
#    Then the customer receives 0 tokens response
#
#
