# Simple Trading Application

This is a simple trading application.

Being a first draft, it uses an in-memory H2 database. Neither the schema, nor the data are created.

## Required tools and libraries

1. At least JDK 11
2. Maven 3.6.3

Install both the JDK (define the environment variable `JAVA_HOME` appropriately and set it in your `PATH` one), then
Maven (define the environment variable `M2_HOME` appropriately).

## Build the code

Execute in a shell

```shell
git pull https://github.com/maurizio-lattuada/trading.git
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

Once you launched the main application (see previous Chapter), you can test it as

```shell
mvn test
```

Note that here the whole application has been structured to have it first running
(see previous Chapter), then tested it. In this way, you can eventually verify manually the database content.

## Verify database content

When the application is running, you can check the database content with a browser: http://localhost:8080/h2-console/

* JDBC URL: `jdbc:h2:mem:trading`
* Password: `password` (see entry `spring.datasource.password` in `application.properties`)

## Debugging

* SQL statements are not logged by default. To turn them on, set the entry `spring.jpa.show-sql`
  to `true` in file `application.properties`.
* H2 console is active by default. To turn it off, remove the entry `spring.h2.console.enabled`
  from `application.properties`.

## API

Once the application is started, you can check all the available APIs and models by using
the [Swagger interface](http://localhost:8080/swagger-ui/).

## Improvements

1. Unit tests!!!
2. Foreign keys and eventually better constraints on DB schema, so we avoid adding corrupted data in the database (e.g.
   orders related to not existing users/securities...)
3. Introduce test parameters (e.g. base URL) to be read from a configuration file
4. Fix errors notified by SpotBugs
