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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.API_KEY_TOKEN_NAME;
import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.CITY_NAME_TOKEN_NAME;
import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.GEOCODE_SEARCH_URL;

@Component
public class GeoCordFetcherWorkFlow implements Function<InternalDataModel, Flux<InternalDataModel>> {

    private static final Logger logger = LoggerFactory.getLogger(GeoCordFetcherWorkFlow.class);

    @Value("${ORS_TOKEN}")
    private String openRouteTokenApi;

    @Autowired
    @Qualifier("geocode_search")
    private WebClient webClient;

    @Autowired
    private JsonMapper jsonMapper;

    @Override
    public Flux<InternalDataModel> apply(InternalDataModel data) {
        final List<String> cities = List.of(data.requestParameters.startCity().apply(data.requestParameters.args()), data.requestParameters.endCity().apply(data.requestParameters.args()));
        return Flux.fromIterable(cities)
                .flatMap(this::fetchCoordsForCity)
                .map(response -> {
                    try {
                        logger.info("Response: {} for {}", response.geoCoding.query.cityName, jsonMapper.writeValueAsString(response));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    data.geoCoordResponses.put(response.geoCoding.query.cityName, response);
                    return data;
                });
    }

    private Mono<GeoCodeSearchResponse> fetchCoordsForCity(String cityName) {
        logger.info("Calling: {} for {}", GEOCODE_SEARCH_URL, cityName);
        final Map<String, String> queryParams = ImmutableMap.<String, String>builder()
                .put(API_KEY_TOKEN_NAME, openRouteTokenApi)
                .put(CITY_NAME_TOKEN_NAME, cityName)
                .build();
        return webClient
                .method(HttpMethod.GET)
                .uri(GEOCODE_SEARCH_URL, queryParams)
                .contentType(MediaType.TEXT_PLAIN)
                .accept(MediaType.TEXT_PLAIN)
                .acceptCharset(Charset.defaultCharset())
                .retrieve()
                .bodyToMono(GeoCodeSearchResponse.class);
    }
}
