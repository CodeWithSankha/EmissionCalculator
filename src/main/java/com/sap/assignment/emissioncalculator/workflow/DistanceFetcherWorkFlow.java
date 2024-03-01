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
import reactor.core.publisher.Flux;

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
        internalDataModel.distance = fetchDistance(internalDataModel.startCityCoord, internalDataModel.endCityCoord);
        return internalDataModel;
    }

    private double fetchDistance(List<Double> startCityCoord, List<Double> endCityCoord) {
        String payload = null;
        MatrixRequestV2 requestV2 = new MatrixRequestV2();
        requestV2.locations = Arrays.asList(startCityCoord, endCityCoord);
        try {
            payload = jsonMapper.writeValueAsString(requestV2);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        logger.info("MATRIX_V2_PROFILE Request: {}", payload);
        String response = webClient
                .method(HttpMethod.POST)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(Charset.defaultCharset())
                .header(API_TOKEN_HEADER_TYPE, openRouteTokenApi)
                .bodyValue(payload).retrieve()
                .bodyToFlux(String.class)
                .blockFirst();
        logger.info("MATRIX_V2_PROFILE Request: {}", response);
        return 42.000f;
    }

    static class MatrixRequestV2 {
        @JsonProperty("locations")
        public List<Double> locations;
    }

}
