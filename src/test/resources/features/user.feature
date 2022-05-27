@Users-api
Feature: Users
  Tests in this feature group are designed to test user-controller in isolation. 
  Feature is testing services available via a REST API on /api/users endpoint.
  All available methods are verified at least once.

  @api-facade 
  Scenario: Create user and get user.  
    When user "Michal" is created
    Then user "Michal" exists
       
  @api-facade 
  Scenario: Create two users and get all users. Verify that both new users exist among all users.
    When user "Michal" is created
    And user "Tom" is created
    Then both users "Michal" and "Tom" exist
      
  @api-facade 
  Scenario: Count number of current users. Create user and get user. Verify that only one user was added.
  	Given known number of users
    When user "Peter" is created
    Then user "Peter" exists 
    And only 1 user was added
  
  #steps definitions of these methods below are implemented with rest-assured
  @rest-assured      
	Scenario: Asking for a non-existing user responds with code 404 User not found
		Given a random non-existing user
		When we ask for the random user via the "/api/users"
		Then server responds with code 404
		
	@rest-assured  	
	Scenario: Creating user successfully responds with code 200 and user in body
		When we create new user via the "/api/users" succesfully
		Then server responds with code 201 
		And user is returned in body of response
		
	@rest-assured      
	Scenario: Attempt to create user without password responds with code 400 Bad request
		When we create new user via the "/api/users" without password
		Then server responds with code 400
