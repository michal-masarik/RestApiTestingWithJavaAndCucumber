@orders-api-tests
Feature: Orders
  Tests in this feature group are designed to test order-controller.
  Feature is testing services available via a REST API on /api/orders endpoint.
  All available methods are verified at least once.  
	Please notice that other controllers are acting in this group of tests as well (usually for 
	preparing test condition, while order-controller comes into play in last verification steps). 
	Therefore these tests are not testing trade-controller in isolation from other services.  
  
  @functional
  Scenario: Create order and get order. Verify that all order details are correct. 
    Given user "Ondra" is created
    And security "CZK" is created
    When user "Ondra" puts a "buy" order for security "CZK" with a price of 25 and quantity of 1000
    Then a "buy" order from user "Ondra" for security "CZK" with a price of 25 and quantity of 1000 exists
      
  @functional
  Scenario: Create two orders by one user. Verify that both new orders exist. Verify all order details are correct. 
    Given user "Standa" is created
    And security "CZK" is created
    When user "Standa" puts a "buy" order for security "CZK" with a price of 25 and quantity of 1000
    And user "Standa" puts a "sell" order for security "CZK" with a price of 30 and quantity of 1000
    Then a "buy" order from user "Standa" for security "CZK" with a price of 25 and quantity of 1000 exists
    And a "sell" order from user "Standa" for security "CZK" with a price of 30 and quantity of 1000 exists

  @functional
  Scenario: Create two orders by two different users. Verify that both new orders exist. Verify all order details are correct.
    Given user "Dominik" is created
    And user "Jane" is created
    And security "EUR" is created
    When user "Dominik" puts a "buy" order for security "EUR" with a price of 25 and quantity of 100
    And user "Jane" puts a "sell" order for security "EUR" with a price of 30 and quantity of 250
    Then a "buy" order from user "Dominik" for security "EUR" with a price of 25 and quantity of 100 exists
    And a "sell" order from user "Jane" for security "EUR" with a price of 30 and quantity of 250 exists
  
  @functional  
  Scenario: Count number of current orders. Create a new order. Verify that only one order was added.
  	Given known number of orders
  	And user "James" is created
    And security "EUR" is created
    When user "James" puts a "buy" order for security "EUR" with a price of 25 and quantity of 1000
    Then only 1 order was added
    
  @response-code     
	Scenario: Asking for a non-existing order responds with code 404 Not found
		Given a random non-existing order
		When we ask for the random order via the "/api/orders"
		Then server responds with code 404
		
	@response-code  	
	Scenario: Creating order successfully responds with code 201 and order is returned in body of response
		Given random user and a random security
		When we create new order via the "/api/orders" succesfully
		Then server responds with code 201 
		And order is returned in body of response
		
	@response-code      
	Scenario: Attempt to create invalid order responds with code 400 Bad request
		Given random user and a random security
		When we create invalid order via the "/api/orders" without quantity and price 
		Then server responds with code 400
		
	@performance     
	Scenario: creating a new order does not take too much time
		Given random user and a random security
		When we create new order via the "/api/orders" succesfully
		Then server responds in 3000 miliseconds
		
	@performance     
	Scenario: getting order does not take too much time
		Given a random non-existing order
		When we ask for the random order via the "/api/orders"
		Then server responds in 3000 miliseconds  
    