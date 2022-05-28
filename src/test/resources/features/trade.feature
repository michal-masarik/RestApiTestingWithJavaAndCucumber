@Tradea-api-tests
Feature: Trades
	Tests in this feature group are designed to test trade-controller.   
  Feature is testing services available via a REST API on /api/trades endpoint.
  All available methods are verified at least once.
  Please notice that other controllers are acting in tests as well (usually for preparing test condition,
	while trade-controller comes into play in last verification steps). 
	Therefore these tests are not testing trade-controller in isolation from other services.

	@functional 
  Scenario: Basic trading Buy Sell
    Given one security "WSB" is created
    And user "Diamond" is created
    And user "Paper" is created
    When user "Diamond" puts a "buy" order for security "WSB" with a price of 101 and quantity of 50
    And user "Paper" puts a "sell" order for security "WSB" with a price of 100 and a quantity of 100
    Then a trade occurs with the price of 100 and quantity of 50
	
	@functional
  Scenario: Basic trading Sell Buy
  	Given one security "SEC" is created
    And user "User1" is created
    And user "User2" is created
    When user "User2" puts a "sell" order for security "SEC" with a price of 100 and a quantity of 100
    And user "User1" puts a "buy" order for security "SEC" with a price of 101 and quantity of 50
    Then a trade occurs with the price of 100 and quantity of 50

	@functional
  Scenario: No trades occur
  	Given one security "NTR" is created
    And user "User1" is created
    And user "User2" is created
    When user "User2" puts a "sell" order for security "NTR" with a price of 100 and a quantity of 100
    And user "User1" puts a "buy" order for security "NTR" with a price of 99 and quantity of 50
    Then no trades occur
 
 	@functional   
  Scenario: Trade occurs with minimum sell price possible.
  	Given one security "BTC" is created
  	And user "buyer" is created
    And user "cheapSeller" is created
    And user "expensiveSeller" is created
    When user "cheapSeller" puts a "sell" order for security "BTC" with a price of 103 and quantity of 150
   	And user "expensiveSeller" puts a "sell" order for security "BTC" with a price of 104 and quantity of 150
   	And user "buyer" puts a "buy" order for security "BTC" with a price of 105 and a quantity of 100
   	Then a trade occurs between "buyer" and "cheapSeller"
 
 	@functional  	
  Scenario: Trade occurs with minimum sell price possible. Even when expensive sell order was created firstly.
  	Given one security "ETH" is created
  	And user "buyer" is created
    And user "cheapSeller" is created
    And user "expensiveSeller" is created
    When user "expensiveSeller" puts a "sell" order for security "ETH" with a price of 14 and quantity of 150
    And user "cheapSeller" puts a "sell" order for security "ETH" with a price of 13 and quantity of 150
   	And user "buyer" puts a "buy" order for security "ETH" with a price of 15 and a quantity of 100
   	Then a trade occurs between "buyer" and "cheapSeller"
   
  @functional	
  Scenario: In case sell order is created first, then first relevant buy order is used to create trade.
  	Given one security "ETH" is created
  	And user "seller" is created
    And user "buyer1" is created
    And user "buyer2" is created
    And user "buyer3" is created
    When user "seller" puts a "sell" order for security "ETH" with a price of 10 and quantity of 100
    And user "buyer1" puts a "buy" order for security "ETH" with a price of 9 and a quantity of 100
   	And user "buyer2" puts a "buy" order for security "ETH" with a price of 11 and a quantity of 100
   	And user "buyer3" puts a "buy" order for security "ETH" with a price of 12 and a quantity of 100
   	Then a trade occurs between "buyer2" and "seller"

	@functional   	
  Scenario: Buy order has bigger quantity then sell order.
  	Given one security "toast" is created
  	And user "toast_seller" is created
    And user "toast_buyer" is created
    When user "toast_seller" puts a "sell" order for security "toast" with a price of 10 and quantity of 99
   	And user "toast_buyer" puts a "buy" order for security "toast" with a price of 10 and a quantity of 100
   	Then a trade occurs between "toast_buyer" and "toast_seller" with the price of 10 and quantity of 99  
