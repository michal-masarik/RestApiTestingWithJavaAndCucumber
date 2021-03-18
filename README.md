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

Optionally, you can remove the comment to annotation `@SpringBootTest` in file
`CucumberTest.java`, so you can run the test without explicitly having the application running.

In this way, by invoking

```shell
mvn -U clean verify
```

you can build and test directly the code.

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

All the APIs discussed in the following Chapters receive and return data in JSON format.

### Users

These APIs can be found in `/api/users`, where we have:

* `GET /api/users`: returns a list of users (potentially empty)
* `GET /api/users/{id}`: returns a specific user having a given identifier
* `POST /api/users`: creates a new user, e.g.:
  ```json
  { "username": "something", "password": "my_password" }
  ```
  The password is then hashed to SHA-256.

### Securities

These APIs can be found in `/api/securities`, where we have:

* `GET /api/securities`: returns a list of securities (potentially empty)
* `GET /api/securities/{id}`: returns a specific security having a given identifier
* `POST /api/securities`: creates a new security, e.g.:
  ```json
  { "name": "ABC" }
  ```

### Orders

These APIs can be found in `/api/orders`, where we have:

* `GET /api/orders`: returns a list of orders (potentially empty)
* `GET /api/orders/{id}`: returns a specific order having a given identifier
* `POST /api/orders`: creates a new order, e.g.:
  ```json
  {
    "userId": "a7e02ce2-3b28-4ade-9fce-f39234e3df57",
    "securityId": "c079d2cd-29e3-4d20-b696-4eab84c119f5",
    "type": "BUY",
    "price": 101.3,
    "quantity": 50
  }
  ```
  `type` can be:
    * `BUY`
    * `SELL`
      If there is an existing matching order, a new trade is automatically created.

### Trades

These APIs can bve found in `/api/trades`, where we have:

* `GET /api/trades`: returns a list of trades (potentially empty)
* `GET /api/trades/{id}`: returns a specific trade having a given identifier
* `GET /api/trades/orderBuyId/{orderBuyId}/orderSellId/{orderSellId}`: returns a trade having a given buy oder
  identifier (`orderBuyId`) and a sell order identifier (`orderSellId`)

## Improvements

1. Unit tests!!!
2. Swagger: API documentation and usage
3. Foreign keys and eventually better constraints on DB schema, so we avoid adding corrupted data in the database (e.g.
   orders related to not existing users/securities...)
4. Introduce test parameters (e.g. base URL) to be read from a configuration file
