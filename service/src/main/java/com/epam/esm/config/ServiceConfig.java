package com.epam.esm.config;

import com.epam.esm.validator.TagValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.epam.esm")
public class ServiceConfig {

    @Bean
    public TagValidator tagValidator() {
        return new TagValidator();
    }
}
