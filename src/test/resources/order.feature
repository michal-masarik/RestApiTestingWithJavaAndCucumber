@tag
Feature: Orders
  Tests in this feature group are designed to test order-controller.
  Feature is testing services available via a REST API on /api/orders endpoint.
  All available methods are verified at least once.  
	Please notice that other controllers are acting in this group of tests as well (usually for 
	preparing test condition, while order-controller comes into play in last verification steps). 
	Therefore these tests are not testing trade-controller in isolation from other services.  
  
  @tag1
  Scenario: Create order and get order. Verify that all order details are correct. 
    Given user "Ondra" is created
    And security "CZK" is created
    When user "Ondra" puts a "buy" order for security "CZK" with a price of 25 and quantity of 1000
    Then a "buy" order from user "Ondra" for security "CZK" with a price of 25 and quantity of 1000 exists
      
  @tag2
  Scenario: Create two orders by one user. Verify that both new orders exist. Verify all order details are correct. 
    Given user "Standa" is created
    And security "CZK" is created
    When user "Standa" puts a "buy" order for security "CZK" with a price of 25 and quantity of 1000
    And user "Standa" puts a "sell" order for security "CZK" with a price of 30 and quantity of 1000
    Then a "buy" order from user "Standa" for security "CZK" with a price of 25 and quantity of 1000 exists
    And a "sell" order from user "Standa" for security "CZK" with a price of 30 and quantity of 1000 exists

  @tag3
  Scenario: Create two orders by two different users. Verify that both new orders exist. Verify all order details are correct.
    Given user "Dominik" is created
    And user "Jane" is created
    And security "EUR" is created
    When user "Dominik" puts a "buy" order for security "EUR" with a price of 25 and quantity of 100
    And user "Jane" puts a "sell" order for security "EUR" with a price of 30 and quantity of 250
    Then a "buy" order from user "Dominik" for security "EUR" with a price of 25 and quantity of 100 exists
    And a "sell" order from user "Jane" for security "EUR" with a price of 30 and quantity of 250 exists
  
  @tag4  
  Scenario: Count number of current orders. Create a new order. Verify that only one order was added.
  	Given known number of orders
  	And user "James" is created
    And security "EUR" is created
    When user "James" puts a "buy" order for security "EUR" with a price of 25 and quantity of 1000
    Then only 1 order was added
    

