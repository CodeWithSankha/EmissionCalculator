package com.sap.assignment.emissioncalculator.workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.collect.ImmutableMap;
import com.sap.assignment.emissioncalculator.models.Coordinates;
import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.inject.Inject;
import javax.inject.Named;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.API_KEY_TOKEN_NAME;
import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.CITY_NAME_TOKEN_NAME;
import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.GEOCODE_SEARCH_URL;

@Component
public class CoordinateFetcherWorkFlow implements Function<InternalDataModel, InternalDataModel> {

    private static final Logger logger = LoggerFactory.getLogger(DistanceFetcherWorkFlow.class);

    @Value("${OPEN_ROUTE_TOKEN}")
    private String openRouteTokenApi;

    @Autowired
    @Qualifier("geocode_search")
    private WebClient webClient;

    @Autowired
    private JsonMapper jsonMapper;

    @Override
    public InternalDataModel apply(InternalDataModel internalDataModel) {
        internalDataModel.startCityCoord = getCoordsFor(internalDataModel.requestParameters.startCity(), internalDataModel.requestParameters.args());
        internalDataModel.endCityCoord = getCoordsFor(internalDataModel.requestParameters.endCity(), internalDataModel.requestParameters.args());
        return internalDataModel;
    }

    private Coordinates getCoordsForV1(Function<ApplicationArguments, String> cityName, ApplicationArguments args) {
        if (cityName.apply(args).equals("Hamburg")) {
            return new Coordinates(cityName.apply(args), new double[]{9.70093, 48.477473});
        }
        if (cityName.apply(args).equals("Berlin")) {
            return new Coordinates(cityName.apply(args), new double[]{9.207916, 49.153868});
        }
        return null;
    }

    private Coordinates getCoordsFor(Function<ApplicationArguments, String> cityName, ApplicationArguments args) {
        logger.info("Calling: {}", GEOCODE_SEARCH_URL);

        // Response response = client.target("https://api.openrouteservice.org/geocode/search?api_key=5b3ce3597851110001cf62482fe19359ac854b2cb7ced2aa1a72e6a2&text=Namibian%20Brewery")
        // final public static String GEOCODE_SEARCH_URL = "https://api.openrouteservice.org/geocode/search?api_key={API_KEY}&text={CITY}";
        final Map<String, String> queryParams = ImmutableMap.<String, String>builder()
                .put(API_KEY_TOKEN_NAME, openRouteTokenApi)
                .put(CITY_NAME_TOKEN_NAME, cityName.apply(args))
                .build();

        String response = webClient
                .method(HttpMethod.GET)
                .uri(GEOCODE_SEARCH_URL, queryParams)
                .contentType(MediaType.TEXT_PLAIN)
                .accept(MediaType.TEXT_PLAIN)
                .acceptCharset(Charset.defaultCharset())
                //.exchangeToFlux() // TODO use it for response handler
                .retrieve()
                .bodyToFlux(String.class)
                .blockFirst();
        logger.info("GEOCODE_SEARCH_URL Response: {}", response);
        return new Coordinates(cityName.apply(args), new double[]{9.207916, 49.153868});

    }
}
