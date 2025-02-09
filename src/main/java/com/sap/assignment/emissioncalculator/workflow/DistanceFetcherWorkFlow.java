package com.sap.assignment.emissioncalculator.workflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import lombok.Builder;
import lombok.ToString;
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
import reactor.util.retry.Retry;

import java.nio.charset.Charset;
import java.time.Duration;
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

    private final int MATRIX_V2_MAX_RETRY = 3;

    private final int RETRY_DELAY_IN_SECS = 1;

    @Override
    public Flux<InternalDataModel> apply(InternalDataModel internalDataModel) {
        return Flux.just(internalDataModel)
                .flatMap(this::fetchDistanceBetweenCities)
                .map(response -> {
                    try {
                        logger.info("MATRIX_V2 Response: {}", jsonMapper.writeValueAsString(response));
                        internalDataModel.distance = response.distances.get(0).get(1);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return internalDataModel;
                });
    }

    private Mono<MatrixResponseV2> fetchDistanceBetweenCities(InternalDataModel internalDataModel) {
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
                .bodyToMono(MatrixResponseV2.class)
                .retryWhen(Retry.fixedDelay(MATRIX_V2_MAX_RETRY, Duration.ofSeconds(RETRY_DELAY_IN_SECS)));
    }

    @ToString
    static class MatrixRequestV2 {
        @JsonProperty("locations")
        public Collection<List<Double>> locations;

        @JsonProperty("metrics")
        public List<String> metrics;

        @JsonProperty("units")
        public String units;
    }

    @ToString
    static class MatrixResponseV2 {
        @JsonProperty("distances")
        public List<List<Double>> distances;

        @JsonProperty("destinations")
        public List<LocationData> destinations;

        @JsonProperty("sources")
        public List<LocationData> sources;

        @ToString
        public static class LocationData {
            @JsonProperty("location")
            public List<Double> location;

            @JsonProperty("snapped_distance")
            public Double snappedDistance;
        }
    }

}
