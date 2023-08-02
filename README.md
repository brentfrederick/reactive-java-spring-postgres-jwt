# Reactive, Non-blocking Java Spring + PostgreSQL backend, JWT-authenticated REST endpoints

## Description

Implementation of a Java and [Spring Reactive](https://spring.io/reactive) stateless backend with REST APIs, *which is
fully non-blocking and supports reactive streams back pressure*. The service provides endpoints to create users,
authenticate users, create/retrieve chat threads, and create/retrieve thread replies. Authentication and authorization
provided via stateless JSON Web Tokens (JWT). The service deploys on the [Netty](https://netty.io/) NIO framework.

This project uses the following technologies:

- [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html) for reactive REST APIs
- [Spring Security OAuth2 for Webflux](https://docs.spring.io/spring-security/reference/reactive/oauth2/index.html) for
  reactive JWT / OAuth2 Resource Server support
- [Spring Data R2DBC](https://docs.spring.io/spring-data/r2dbc/docs/current/reference/html/) for reactive database
  connectivity
- [R2DBC](https://r2dbc.io/) Non-blocking PostgreSQL Driver (unlike JDBC which blocks!)
- [FlywayDB](https://flywaydb.org/) for database migrations (uses JDBC)
- [Netty](https://netty.io/) Asynchronous, event-driven network application framework

## Getting Started

### Dependencies

1. Java 19
2. [PostgreSQL]("https://www.postgresql.org/")
2. [Gradle]("https://gradle.org/install/")

### Run the server

1. Generate RSA keys for signing and validating the JWTs: `./gradlew createKeys`
2. Update [application-local.yml](src%2Fmain%2Fresources%2Fapplication-local.yml) to point to your local PostgreSQL
   database
3. Start the server (port 8080): `./start.sh`

### Query the server

A Postman import file is included to test the API
endpoints: [reactive-java-spring-postgres-jwt.postman_collection.json](reactive-java-spring-postgres-jwt.postman_collection.json)

Be sure to update the Authorization tab on the User API with the response from the token endpoint.

## Sample console output

Notably, the server starts in ~2 seconds on modest hardware.

```
~/workspace/reactive-java-spring-postgres » ./start.sh                                                                                                                                              130 ↵ brent@Mac-mini
Running ./gradlew bootRun

> Task :bootRun
2023-08-01 18:23:50.066 -  INFO 74734 --- [main] .b.ReactiveJavaSpringPostgresApplication : Starting ReactiveJavaSpringPostgresApplication using Java 19.0.2
2023-08-01 18:23:50.068 - DEBUG 74734 --- [main] .b.ReactiveJavaSpringPostgresApplication : Running with Spring Boot v3.0.6, Spring v6.0.8
2023-08-01 18:23:50.068 -  INFO 74734 --- [main] .b.ReactiveJavaSpringPostgresApplication : The following 1 profile is active: "local"
2023-08-01 18:23:50.453 -  INFO 74734 --- [main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data R2DBC repositories in DEFAULT mode.
2023-08-01 18:23:50.554 -  INFO 74734 --- [main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 98 ms. Found 3 R2DBC repository interfaces.
2023-08-01 18:23:51.578 -  INFO 74734 --- [main] o.f.c.internal.license.VersionPrinter    : Flyway Community Edition 9.17.0 by Redgate
2023-08-01 18:23:51.578 -  INFO 74734 --- [main] o.f.c.internal.license.VersionPrinter    : See release notes here: https://rd.gt/416ObMi
2023-08-01 18:23:51.578 -  INFO 74734 --- [main] o.f.c.internal.license.VersionPrinter    :
2023-08-01 18:23:51.652 -  INFO 74734 --- [main] o.f.c.i.database.base.BaseDatabaseType   : Database: jdbc:postgresql://localhost:5432/brentcodesdb (PostgreSQL 15.2)
2023-08-01 18:23:51.674 -  INFO 74734 --- [main] o.f.core.internal.command.DbValidate     : Successfully validated 1 migration (execution time 00:00.012s)
2023-08-01 18:23:51.685 -  INFO 74734 --- [main] o.f.core.internal.command.DbMigrate      : Current version of schema "public": 0001
2023-08-01 18:23:51.685 -  INFO 74734 --- [main] o.f.core.internal.command.DbMigrate      : Schema "public" is up to date. No migration necessary.
2023-08-01 18:23:51.814 -  INFO 74734 --- [main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port 8080
2023-08-01 18:23:51.820 -  INFO 74734 --- [main] .b.ReactiveJavaSpringPostgresApplication : Started ReactiveJavaSpringPostgresApplication in 2.137 seconds (process running for 2.358)
```

## Author

[Brent Frederick](https://www.linkedin.com/in/brentfrederick/)

[@brentcodes](https://twitter.com/brentcodes)

## License

This project is licensed under the MIT License - see the LICENSE.md file for details