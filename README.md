# Testing of simple Trading application

This is basic test framework for simple trading application.
Focus is on testing web service with REST API and other aspects in Java and Cucumber.

SUT is stand-alone Java Spring Boot application with web REST API. There is model and controller, but no view.

## Required tools and libraries

1. [JDK 17](https://www.oracle.com/java/technologies/downloads/#JDK17)
2. At least [Maven 3.6.3](https://maven.apache.org/download.cgi)

Install both the JDK (define the environment variable `JAVA_HOME` appropriately), then
Maven (define the environment variable `M2_HOME` appropriately).

## Build the Trading application

```shell
git clone https://github.com/maurizio-lattuada/trading.git
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

once tests are run, in console you can also find a link to online published test report eg.:

```
View your Cucumber Report at:                                            
https://reports.cucumber.io/reports/4fd167d8-1217-48c5-9eb6-b4100e0bfdbc          
```

Alternatively if you don't want to build app and run tests yourself, but you want just see my test results, then you can visit [Trading Test Reports](https://reports.cucumber.io/report-collections/e17c7f4f-e651-4d51-bb82-a8d9cfac14da)

## My solution

Firstly I did some reverse engineering of trading application. Then I was studying topics related to my task (Spring Boot applications, Cucumber, API testing etc.).

I implemented very basic framework with JAVA and Cucumber for testing REST API of trading application in best way I was able to do with my current knowledge.
Architecture is simple and it's explained in javadoc comments in code.

I implemented few tests to demonstrate possibility of framework. Other tests can be implemented and some other bugs in trading application can be possibly found, but I focused more on quality of my solution then quantity of tests.

During my test implementation I was also able to find some bug in application logic namely in Trade Controller. You can see failing test.

Based on BDD principles I also created example test for functionality, which is not yet implemented. So obviously this test is failing now as well.

I implemented basic functionality enabling test parameters (e.g. base URL) to be read from a configuration file, as was suggested in potentional improvements.

I did some investigation in direction what would need to be done in terms of better constraints and foreign keys in application repository. In my opinion SUT would need redesign the way how IDs of elements are used. After analysing, that lot of changes would need to be done in SUT, I decided not to implement this, as changing SUT implementation is not fully related to my testing task.

I also implemented few Spring Boot junit tests for UserController.class just for demonstration purposes. I used RestAssured library for this. Please notice, that these junit tests results aren't propagated to Cucumber web report. Tests results are in console, when run locally.



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

## Improvements

1. Unit tests!!!
2. Foreign keys and eventually better constraints on DB schema, so we avoid adding corrupted data in the database (e.g.
   orders related to not existing users/securities...)
3. Introduce test parameters (e.g. base URL) to be read from a configuration file
4. Fix errors notified by SpotBugs (you can check them via `mvn spotbugs:gui`)
5. Fix errors notified by Checkstyle (and eventually tune the settings file). You can check the errors via
    ```shell
   mvn checkstyle:checkstyle
   ```
   then open the HTML report located to `target/site/checkstyle.html`.
