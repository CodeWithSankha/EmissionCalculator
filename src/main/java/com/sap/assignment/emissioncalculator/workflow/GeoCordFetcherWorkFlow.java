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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Function;

import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.API_KEY_TOKEN_NAME;
import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.CITY_NAME_TOKEN_NAME;
import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.GEOCODE_SEARCH_URL;

@Component
public class GeoCordFetcherWorkFlow implements Function<InternalDataModel, Flux<InternalDataModel>> {

    private static final Logger logger = LoggerFactory.getLogger(GeoCordFetcherWorkFlow.class);

    @Value("${OPEN_ROUTE_TOKEN}")
    private String openRouteTokenApi;

    @Autowired
    @Qualifier("geocode_search")
    private WebClient webClient;

    @Autowired
    private JsonMapper jsonMapper;

    @Override
    public Flux<InternalDataModel> apply(InternalDataModel data) {
        Flux<InternalDataModel> startCityFlux = fetchCoordsForCity(data.requestParameters.startCity().apply(data.requestParameters.args()), data);
        Flux<InternalDataModel> endCityFlux = fetchCoordsForCity(data.requestParameters.endCity().apply(data.requestParameters.args()), data);
        return Flux.zip(startCityFlux, endCityFlux).map(Tuple2::getT1);//.flatMap(s -> data);
        //return Flux.just(data);

        // Flux<GeoCodeSearchResponse> startCityFlux = fetchCoordsForCity(data.requestParameters.startCity(), data.requestParameters.args(), data);
        // Flux<GeoCodeSearchResponse> endCityFlux = fetchCoordsForCity(data.requestParameters.endCity(), data.requestParameters.args(), data);
        // return Flux.zip(startCityFlux, endCityFlux).doOnNext(Tuple2::getT1).flatMap(s -> Flux.just(data));
    }

    private Flux<InternalDataModel> fetchCoordsForCity(String cityName, InternalDataModel data) {
        logger.info("Calling: {}", GEOCODE_SEARCH_URL);
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
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                        Mono<GeoCodeSearchResponse> response = clientResponse.bodyToMono(GeoCodeSearchResponse.class);
                        response.subscribeOn(Schedulers.newParallel(cityName))
                                .subscribe(responseBody -> {
                                    try {
                                        logger.info("GEOCODE_SEARCH_URL Response for {}: {}", cityName, jsonMapper.writeValueAsString(responseBody));
                                        data.geoCoordResponses.put(cityName, responseBody);
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                        return clientResponse.bodyToMono(GeoCodeSearchResponse.class);
                    } else {
                        // Turn to error
                        throw new RuntimeException(GEOCODE_SEARCH_URL + " call failed");
                    }
                })
                .flux().map(s -> data);
    }
}
