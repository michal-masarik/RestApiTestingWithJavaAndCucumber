# Simple Trading Application

This is a simple trading application.

Being a first draft, it uses an in-memory H2 database. Neither the schema, nor the data are explicitly created.

## Required tools and libraries

1. At least [JDK 11](https://www.oracle.com/java/technologies/javase-downloads.html#JDK11)
2. [Maven 3.6.3](https://maven.apache.org/download.cgi)

Install both the JDK (define the environment variable `JAVA_HOME` appropriately), then
Maven (define the environment variable `M2_HOME` appropriately).

## Build the code

Execute in a shell

```shell
git clone https://github.com/maurizio-lattuada/trading.git
cd trading
mvn -U clean verify -DskipTests
```

Note: here tests are skipped, since they are executed later, see the Chapter
"Test the code".

In this way, a new jar file will be created in the `target` folder, e.g.
`target/trading-0.0.1-SNAPSHOT.jar`.

## Run the code

```shell
java -jar target/trading-0.0.1-SNAPSHOT.jar
```

## Test the code

Once you launched the main application (see the previous Chapter), you can test it as

```shell
mvn test
```

Note that here the whole application has been structured to have it first running
(see the previous Chapter), then tested via Cucumber. In this way, you can eventually verify manually the database
content.

Optionally, you can remove the comment to annotation `@SpringBootTest` in file
[`CucumberTest.java`](src/test/java/name/lattuada/trading/tests/CucumberTest.java), so you can run the test without
explicitly having the application running.

In this way, by invoking:

```shell
mvn -U clean verify
```

you can build and test directly the code.

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
