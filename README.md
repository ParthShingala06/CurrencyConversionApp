# RateEdge
## Optimized Forex Conversion Tool

## Requirements
For building and running the application you need:

- [JDK 18](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally
Building the maven jar:
```agsl
clean install -DskipTests
```

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.CurrencyApp.CurrencyConvertor.CurrencyConvertorApplication` class from your IDE.
Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```agsl
mvn spring-boot:run
```

