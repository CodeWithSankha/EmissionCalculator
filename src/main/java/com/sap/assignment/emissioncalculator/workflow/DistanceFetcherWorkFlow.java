package com.sap.assignment.emissioncalculator.workflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sap.assignment.emissioncalculator.models.Coordinates;
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

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.API_TOKEN_HEADER_TYPE;

@Component
public class DistanceFetcherWorkFlow implements Function<InternalDataModel, InternalDataModel> {

    private static final Logger logger = LoggerFactory.getLogger(DistanceFetcherWorkFlow.class);

    @Value("${OPEN_ROUTE_TOKEN}")
    private String openRouteTokenApi;

    @Autowired
    @Qualifier("matrix_v2_profile")
    private WebClient webClient;

    @Autowired
    private JsonMapper jsonMapper;

    @Override
    public InternalDataModel apply(InternalDataModel internalDataModel) {
        internalDataModel.distance = getDistanceBetween(internalDataModel.startCityCoord, internalDataModel.endCityCoord);
        return internalDataModel;
    }

    private double getDistanceBetween(Coordinates startCityCoord, Coordinates endCityCoord) {
        String payload = null;
        MatrixRequestV2 requestV2 = new MatrixRequestV2();
        requestV2.locations = Arrays.asList(startCityCoord.coords(), endCityCoord.coords());
        try {
            payload = jsonMapper.writeValueAsString(requestV2);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        logger.info("V2_MATRIX_PROFILE Request: {}", payload);

        // Entity<String> payload = Entity.json({"locations":[[9.70093,48.477473],[9.207916,49.153868],[37.573242,55.801281],[115.663757,38.106467]]});
        String response = webClient
                .method(HttpMethod.POST)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(Charset.defaultCharset())
                .header(API_TOKEN_HEADER_TYPE, openRouteTokenApi)
                .bodyValue(payload).retrieve()
                .bodyToFlux(String.class)
                .blockFirst();
        logger.info("V2_MATRIX_PROFILE Request: {}", response);
        return 4000.0;
    }

    static class MatrixRequestV2 {
        @JsonProperty("locations")
        public List<double[]> locations;
    }

}
