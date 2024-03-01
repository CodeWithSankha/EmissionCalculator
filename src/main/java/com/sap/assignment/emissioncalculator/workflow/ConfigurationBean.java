package com.sap.assignment.emissioncalculator.workflow;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import javax.inject.Named;

@Configuration
public class ConfigurationBean {

    final public static String API_TOKEN_HEADER_TYPE = "Authorization";


    final public static String API_KEY_TOKEN_NAME = "API_KEY";
    final public static String CITY_NAME_TOKEN_NAME = "CITY";

    final public static String GEOCODE_SEARCH_URL = String.format("https://api.openrouteservice.org/geocode/search?api_key={%s}&text={%s}", API_KEY_TOKEN_NAME, CITY_NAME_TOKEN_NAME);

    final public static String MATRIX_V2_PROFILE_URL = "https://api.openrouteservice.org/v2/matrix/driving-car";

    @Bean
    public JsonMapper jsonMapper() {
        return new JsonMapper();
    }

    @Bean(name="matrix_v2_profile")
    public WebClient webClientMatrixV2Profile() {
        return WebClient.create(MATRIX_V2_PROFILE_URL);
    }

    @Bean(name="geocode_search")
    public WebClient webClientGeoCodeSearch() {
        return WebClient.create();
    }
}
