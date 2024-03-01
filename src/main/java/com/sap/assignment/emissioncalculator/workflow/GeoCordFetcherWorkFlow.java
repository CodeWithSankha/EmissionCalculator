package com.sap.assignment.emissioncalculator.workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.collect.ImmutableMap;
import com.sap.assignment.emissioncalculator.models.GeoCodeSearchResponse;
import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Function;

import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.API_KEY_TOKEN_NAME;
import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.CITY_NAME_TOKEN_NAME;
import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.GEOCODE_SEARCH_URL;

@Component
public class GeoCordFetcherWorkFlow implements Function<InternalDataModel, InternalDataModel> {

    private static final Logger logger = LoggerFactory.getLogger(GeoCordFetcherWorkFlow.class);

    @Value("${OPEN_ROUTE_TOKEN}")
    private String openRouteTokenApi;

    @Autowired
    @Qualifier("geocode_search")
    private WebClient webClient;

    @Autowired
    private JsonMapper jsonMapper;

    @Override
    public InternalDataModel apply(InternalDataModel internalDataModel) {
        internalDataModel.startCityGeoCoordResponse = fetchCoordsForCity(internalDataModel.requestParameters.startCity(), internalDataModel.requestParameters.args());
        internalDataModel.endCityCoordResponse = fetchCoordsForCity(internalDataModel.requestParameters.endCity(), internalDataModel.requestParameters.args());
        return internalDataModel;
    }

    private Flux<GeoCodeSearchResponse> fetchCoordsForCity(Function<ApplicationArguments, String> cityName, ApplicationArguments args) {
        logger.info("Calling: {}", GEOCODE_SEARCH_URL);

        final Map<String, String> queryParams = ImmutableMap.<String, String>builder()
                .put(API_KEY_TOKEN_NAME, openRouteTokenApi)
                .put(CITY_NAME_TOKEN_NAME, cityName.apply(args))
                .build();

        return webClient
                .method(HttpMethod.GET)
                .uri(GEOCODE_SEARCH_URL, queryParams)
                .contentType(MediaType.TEXT_PLAIN)
                .accept(MediaType.TEXT_PLAIN)
                .acceptCharset(Charset.defaultCharset())
                .exchangeToFlux(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                        Flux<GeoCodeSearchResponse> response = clientResponse.bodyToFlux(GeoCodeSearchResponse.class);
                        response.subscribe(s -> {
                            try {
                                logger.info("GEOCODE_SEARCH_URL Response: {}", jsonMapper.writeValueAsString(s));
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        return clientResponse.bodyToFlux(GeoCodeSearchResponse.class);
                    } else {
                        // Turn to error
                        throw new RuntimeException(GEOCODE_SEARCH_URL + " call failed");
                    }
                });
    }
}
