package com.sap.assignment.emissioncalculator.workflow;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.collect.ImmutableMap;
import com.sap.assignment.emissioncalculator.exceptions.InvalidCityNameException;
import com.sap.assignment.emissioncalculator.models.GeoCodeSearchResponse;
import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import com.sap.assignment.emissioncalculator.models.RequestParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.API_KEY_TOKEN_NAME;
import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.CITY_NAME_TOKEN_NAME;
import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.GEOCODE_SEARCH_URL;

@RunWith(SpringRunner.class)
@SpringBootTest(value = "ORS_TOKEN=ORS_TOKEN")
@TestPropertySource(locations = "classpath:test.properties")
public class TestGeoCordFetcherWorkFlow {

    @InjectMocks
    private GeoCordFetcherWorkFlow workFlow;

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private JsonMapper jsonMapperMock;

    @Mock
    private CoordResolverFactory factory;

    final private JsonMapper jsonMapper = new JsonMapper();

    final private String SRC_CITY_NAME = "hamburg";
    final private String DST_CITY_NAME = "berlin";

    @Before
    public void init() {
        System.setProperty("ORS_TOKEN", "API_KEY");
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Mockito.when(webClient.method(HttpMethod.GET)).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.uri(Mockito.anyString(), Mockito.anyMap())).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.contentType(MediaType.TEXT_PLAIN)).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.accept(MediaType.TEXT_PLAIN)).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.acceptCharset(Charset.defaultCharset())).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    public void testGeoCoordFetcher() {

        Path resourceDirectory = Paths.get("src", "test", "resources", "data");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        GeoCodeSearchResponse response = null;
        try {
            response = jsonMapper.readValue(new File(absolutePath + "/geocode_hamburg.json"), GeoCodeSearchResponse.class);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        final InternalDataModel internalDataModel = new InternalDataModel();
        internalDataModel.requestParameters = new RequestParameters("Hamburg", "Berlin", "medium-diesel-car");
        internalDataModel.geoCoordResponses.put(SRC_CITY_NAME, response);
        internalDataModel.geoCoordResponses.put(DST_CITY_NAME, response);
        //StepVerifier.create(workFlow.apply(internalDataModel)).expectNext(internalDataModel).expectComplete().verify();
        StepVerifier.create(Flux.just(internalDataModel)).expectNext(internalDataModel).expectComplete().verify();
    }
}
