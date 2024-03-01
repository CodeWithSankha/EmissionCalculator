package com.sap.assignment.emissioncalculator.service;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationBean {

    @Bean
    public JsonMapper jsonMapper() {
        return new JsonMapper();
    }
}
