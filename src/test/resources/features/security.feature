@Securities-api
Feature: Securities
  Tests in this feature group are designed to test security-controller in isolation. 
  Feature is testing services available via a REST API on /api/securities endpoint.
  All available methods are verified at least once.

  @functional
  Scenario: Create security and get security 
    When security "CZK" is created
    Then security "CZK" exists
   
  @functional     
  Scenario: Create two securities and get all securities. 
  Verify that both new securities exist among all securities.
    When security "EUR" is created
    And security "CHF" is created
    Then both securities "EUR" and "CHF" exist
    
  @functional     
  Scenario: Count number of current securities. Create two securities. 
  Verify that only two securities were added.
  	Given known number of securities
    When security "Crude Oil" is created
    And security "Cotton" is created
    Then only 2 security was added
        