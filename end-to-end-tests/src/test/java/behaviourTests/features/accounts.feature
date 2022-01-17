Feature: AccountRequestFeature
  Scenario: Account Registration
    Given person with name "Florian" "Kesten" with cpr "000000-1234", bank accountId "56741"
    When the user is being registered
    Then the user is registered
    And has a non empty id