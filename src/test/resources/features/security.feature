@Securities-api-tests
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
    
  @response-code     
	Scenario: Asking for a non-existing security responds with code 404 Not found
		Given a random non-existing security
		When we ask for the random security via the "/api/securities"
		Then server responds with code 404
		
	@response-code  	
	Scenario: Creating security successfully responds with code 201 and 
	security is returned in body of response
		When we create new security via the "/api/securities" succesfully
		Then server responds with code 201 
		And security is returned in body of response
		
	@response-code      
	Scenario: Attempt to create security without name responds with code 400 Bad request
		When we create security via the "/api/securities" without name
		Then server responds with code 400
		
	@performance     
	Scenario: creating a new security does not take too much time
		When we create new security via the "/api/securities" succesfully
		Then server responds in 3000 miliseconds
		
	@performance     
	Scenario: getting security does not take too much time
		Given a random non-existing security
		When we ask for the random security via the "/api/securities"
		Then server responds in 3000 miliseconds
        