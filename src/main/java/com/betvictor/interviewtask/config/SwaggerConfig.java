package com.betvictor.interviewtask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.betvictor.interviewtask.controller"))
                //  .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Kamil Politowski",  "https://www.linkedin.com/in/kamil-politowski-84647185/", "kamil.politowski@gmail.com");

        return new ApiInfo(
                "betVictor test",
                "REST Application for an interview task to great betVictor company",
                "0.9",
                "Terms of service", contact,
                "", "", Collections.emptyList());
    }
}
