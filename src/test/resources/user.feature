@tag
Feature: Users
  Tests in this feature group are designed to test user-controller in isolation. 
  Feature is testing services available via a REST API on /api/users endpoint.
  All available methods are verified at least once.

  @tag1
  Scenario: Create user and get user.  
    When user "Michal" is created
    Then user "Michal" exists
       
  @tag2
  Scenario: Create two users and get all users. Verify that both new users exist among all users.
    When user "Michal" is created
    And user "Tom" is created
    Then both users "Michal" and "Tom" exist
      
  @tag3  
  Scenario: Count number of current users. Create user and get user. Verify that only one user was added.
  	Given known number of users
    When user "Peter" is created
    Then user "Peter" exists 
    And only 1 user was added
    
  @tag4
  Scenario: Server returns 404: user not found, when asked for non-existing user.
    When /api/users/id is asked for non-existing user id
    Then server returns user not found message
    