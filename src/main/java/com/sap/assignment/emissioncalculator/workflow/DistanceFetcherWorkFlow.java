package com.sap.assignment.emissioncalculator.workflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import lombok.Builder;
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
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.API_TOKEN_HEADER_TYPE;

@Component
public class DistanceFetcherWorkFlow implements Function<InternalDataModel, Flux<InternalDataModel>> {

    private static final Logger logger = LoggerFactory.getLogger(DistanceFetcherWorkFlow.class);

    @Value("${ORS_TOKEN}")
    private String openRouteTokenApi;

    @Autowired
    @Qualifier("matrix_v2_profile")
    private WebClient webClient;

    @Autowired
    private JsonMapper jsonMapper;

    @Override
    public Flux<InternalDataModel> apply(InternalDataModel internalDataModel) {
        return Flux.just(internalDataModel)
                .flatMap(this::fetchDistanceBetweenCities)
                .map(response -> {
                    try {
                        logger.info("MATRIX_V2 Response: {}", jsonMapper.writeValueAsString(response));
                        internalDataModel.distance = 42.00f;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return internalDataModel;
                });
    }

    private Mono<String> fetchDistanceBetweenCities(InternalDataModel internalDataModel) {
        String payload = null;
        MatrixRequestV2 requestV2 = new MatrixRequestV2();
        requestV2.locations = internalDataModel.cityCoords.values();
        requestV2.metrics = List.of("distance");
        requestV2.units = "km"; // In Kilometer
        try {
            payload = jsonMapper.writeValueAsString(requestV2);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try {
            logger.info("MATRIX_V2 Request: {}", jsonMapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return webClient
                .method(HttpMethod.POST)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(Charset.defaultCharset())
                .header(API_TOKEN_HEADER_TYPE, openRouteTokenApi)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class);
    }

    static class MatrixRequestV2 {
        @JsonProperty("locations")
        public Collection<List<Double>> locations;

        @JsonProperty("metrics")
        public List<String> metrics;

        @JsonProperty("units")
        public String units;
    }

}
