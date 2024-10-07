# RateEdge
## Optimized Forex Conversion Tool - [Live Demo](http://ec2-18-191-188-215.us-east-2.compute.amazonaws.com:8090/v1/swagger-ui/index.html)

## Description:
### Maximize Your Returns with Optimized Forex Exchanges.
The API offers advanced currency conversion capabilities, including the ability to identify and recommend the most profitable path for forex conversions. By analyzing various exchange rates, the API calculates and provides the path that yields the highest gains, ensuring optimal returns on currency exchanges.



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

