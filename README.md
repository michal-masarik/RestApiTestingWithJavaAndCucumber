# REST API Testing with Java, REST Assured and Cucumber

This is a proof of concept of test framework for simple trading application.
Focus is on testing web services with REST API and other aspects in Java, REST Assured and Cucumber.

SUT is stand-alone Java Spring Boot application with web REST API. There is model and controller, but no view. SUT has dedicated DTO and Entity layers. 
H2 in-memory database is used for storing data and managed by Hibernate JPA framework.

## Motivation

I got task to implement REST API testing framework for existing trivial trading application during application process for role of Test Automation Engineer in a Swiss Exchange company. 

## My solution

I implemented very basic framework with JAVA, REST Assured and Cucumber for testing REST APIs of trading application in best way I was able to do with my current knowledge of used technology.

Architecture is simple and it's explained in javadoc documentation in code.

I implemented medium amount of tests to demonstrate possibility of framework. More tests can always be implemented and some other bugs in trading application can be possibly found, but I haven't focused on quantity of tests.

During my test implementation I was also able to find some bug in application logic namely in Trade Controller. There are therefore failing tests.

I analyzed existing business logic in SUT and based on BDD principles I also created example test for functionality, which is not yet implemented. These tests are obviously failing now as well.

I implemented basic functionality enabling test parameters (e.g. base URL) to be read from a configuration file, as was suggested as potential bonus point for solution.

I did some investigation in direction what would need to be done in terms of better constraints and foreign keys in application repository. In my opinion SUT would need redesign the way how IDs of entities are used. After analysing, that quite lot of changes would need to be done in SUT, I decided not to implement this, as changing SUT implementation is not fully related to my testing task.

I also implemented few Spring Boot jUnit tests for UserController.class just for demonstration purposes. I used RestAssured library for this. Please notice, that these jUnit tests results aren't propagated to Cucumber web report. Tests results are just shown in console, when run locally.


## Required tools and libraries

1. [JDK 17](https://www.oracle.com/java/technologies/downloads/#JDK17)
2. At least [Maven 3.6.3](https://maven.apache.org/download.cgi)

Install both the JDK (define the environment variable `JAVA_HOME` appropriately), then
Maven (define the environment variable `M2_HOME` appropriately).

## Build the Trading application

```shell
cd trading
mvn -U clean verify -DskipTests
```

In this way, a new jar file will be created in the `target` folder, e.g.
`target/trading-0.0.2-SNAPSHOT.jar`.

## Run the Trading application

```shell
java -jar target/trading-0.0.2-SNAPSHOT.jar
```

## Run the tests

Once trading application is running, you can run all tests by

```shell
mvn test
```

By this you will get cucumber styled test report with all logging just in your console.

## Test Reports

After test run, in console you can find a link to online test report eg.:

```
View your Cucumber Report at:                                            
https://reports.cucumber.io/reports/71d5018b-d043-4aad-bf26-ae254bbb63e1         
```

Alternatively (if you don't want to build app and run tests yourself) you can check my last [Trading Test Reports](https://reports.cucumber.io/report-collections/e17c7f4f-e651-4d51-bb82-a8d9cfac14da)

## Verify database content

When the application is running, you can check the database content with a
browser: [H2 Console](http://localhost:8080/h2-console/).

Here the settings to access the H2 console (values referenced
in [`application.properties`](src/main/resources/application.properties)):

* JDBC URL: `jdbc:h2:mem:trading` (see entry `spring.datasource.url` )
* Password: `password` (see entry `spring.datasource.password`)

## Debugging

* SQL statements are not logged by default. To turn them on, set the entry `spring.jpa.show-sql`
  to `true` in file [`application.properties`](src/main/resources/application.properties).
* H2 console is active by default. To turn it off, remove the entry `spring.h2.console.enabled`
  from [`application.properties`](src/main/resources/application.properties).

## API

Once the application is started, you can check all the available APIs and models by using
the [Swagger interface](http://localhost:8080/swagger-ui/).

## Improvements for SUT application

1. Unit tests
2. Foreign keys and eventually better constraints on DB schema, so we avoid adding corrupted data in the database (e.g.
   orders related to not existing users/securities...)
3. Introduce test parameters (e.g. base URL) to be read from a configuration file
4. Fix errors notified by SpotBugs (you can check them via `mvn spotbugs:gui`)
5. Fix errors notified by Checkstyle (and eventually tune the settings file). You can check the errors via
    ```shell
   mvn checkstyle:checkstyle
   ```
   then open the HTML report located to `target/site/checkstyle.html`.
