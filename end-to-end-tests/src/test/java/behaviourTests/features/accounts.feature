#Feature: AccountRequestFeature
#  Scenario: Account Registration
#    Given person with name "Florian" "Kesten" with cpr "000000-1234", bank accountId "56741"
#    When the user is being registered
#    Then the user is registered
#    And has a non empty id
#
#  Scenario: Account Registration Race Condition
#    Given person with name "Florian" "Kesten" with cpr "000000-1234", bank accountId "56741"
#    And second person with name "Bingkun" "Wu" with cpr "000000-5678", bank accountId "45897"
#    When the two accounts are registered at the same time
#    Then the first account has a non empty id
#    And the second account has a non empty id different from the first student
