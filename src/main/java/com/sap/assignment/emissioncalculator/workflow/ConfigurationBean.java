package com.sap.assignment.emissioncalculator.workflow;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import javax.inject.Inject;

@Configuration
public class ConfigurationBean {

    final public static String API_TOKEN_HEADER_TYPE = "Authorization";

    final public static String GEOCODE_SEARCH_URL = "https://api.openrouteservice.org/geocode/search";

    final public static String MATRIX_V2_PROFILE_URL = "https://api.openrouteservice.org/v2/matrix/driving-car";

    @Bean
    public JsonMapper jsonMapper() {
        return new JsonMapper();
    }

    @Bean("matrix_v2_profile")
    public WebClient webClient() {
        return WebClient.create(MATRIX_V2_PROFILE_URL);
    }
}
