package com.example.javatraineetechtask;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JavaTraineeTechTaskApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(JavaTraineeTechTaskApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("http://localhost:9080/swagger-ui/index.html");
        System.out.println("http://localhost:9080/h2-console");
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("EIS API")
                        .description("Employee Information System sample application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                        .description("SpringShop Wiki Documentation")
                        .contact(new Contact().email("test@test.com").url("http://fullstackcode.dev")))
                ;
    }

}
