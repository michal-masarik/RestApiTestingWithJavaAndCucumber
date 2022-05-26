
@tag
Feature: Not implemented functionality yet.
Tests in this feature group are now expected to be failing.
This is because in Behavior driven development (BDD) we often want to implement 
tests before we start implementing feature, similarly like in Test Driven Development (TDD).

Lets assume that we want implement this core functionality in next iteration, so let's write a test already now.

  Scenario: Single buy order can be satisfied by creating multiple trades based on minimum price.
  	Given one security "Cucumber" is created
  	And user "first_farmer" is created
    And user "second_farmer" is created
    And user "merchant" is created
    When user "first_farmer" puts a "sell" order for security "Cucumber" with a price of 10 and quantity of 100
    And user "second_farmer" puts a "sell" order for security "Cucumber" with a price of 11 and a quantity of 1000
   	And user "merchant" puts a "buy" order for security "Cucumber" with a price of 12 and a quantity of 120
   	Then a trade occurs between "merchant" and "first_farmer" with the price of 10 and quantity of 100
   	And a trade occurs between "merchant" and "second_farmer" with the price of 11 and quantity of 20

