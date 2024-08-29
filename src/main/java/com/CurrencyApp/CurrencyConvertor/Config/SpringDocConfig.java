package com.CurrencyApp.CurrencyConvertor.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocConfig {

    @Value("${springdoc.api-docs.title}")
    private String title;

    @Value("${springdoc.api-docs.summary}")
    private String summary;

    @Value("${springdoc.api-docs.terms-of-service}")
    private String termsOfService;

    @Value("${springdoc.api-docs.contact.name}")
    private String contactName;

    @Value("${springdoc.api-docs.contact.email}")
    private String contactEmail;

    @Value("${springdoc.api-docs.license.name}")
    private String licenseName;

    @Value("${springdoc.api-docs.license.url}")
    private String licenseUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .summary(summary)
                        .description("**Maximize Your Returns with Optimized Forex Exchanges**. \n\n *The API offers advanced " +
                                "currency conversion capabilities, including the ability " +
                                "to identify and recommend the most profitable path for forex conversions. " +
                                "By analyzing various exchange rates, the API calculates and provides the path " +
                                "that yields the highest gains, ensuring optimal returns on currency exchanges.*\n\n " +
                                "**Developed By: Parth Shingala.**")

                        .termsOfService(termsOfService)
                        .contact(new Contact()
                                .name(contactName)
                                .url(termsOfService)
                                .email(contactEmail))
                        .license(new License()
                                .name(licenseName)
                                .url(licenseUrl)));
    }
}