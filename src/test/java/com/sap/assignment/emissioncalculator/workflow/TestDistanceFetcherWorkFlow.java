package com.sap.assignment.emissioncalculator.workflow;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sap.assignment.emissioncalculator.models.GeoCodeSearchResponse;
import com.sap.assignment.emissioncalculator.models.InternalDataModel;
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

import static com.sap.assignment.emissioncalculator.workflow.ConfigurationBean.GEOCODE_SEARCH_URL;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class TestDistanceFetcherWorkFlow {

    @InjectMocks
    private DistanceFetcherWorkFlow workFlow;

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
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Mockito.when(webClient.method(HttpMethod.GET)).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.uri(GEOCODE_SEARCH_URL, requestBodyUriSpec)).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.contentType(MediaType.TEXT_PLAIN)).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.accept(MediaType.TEXT_PLAIN)).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.acceptCharset(Charset.defaultCharset())).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    public void testDistanceFetcher() {

        Path resourceDirectory = Paths.get("src", "test", "resources", "data");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        GeoCodeSearchResponse response = null;
        try {
            response = jsonMapper.readValue(new File(absolutePath + "/geocode_hamburg.json"), GeoCodeSearchResponse.class);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        final InternalDataModel internalDataModel = new InternalDataModel();
        internalDataModel.geoCoordResponses.put(SRC_CITY_NAME, response);
        internalDataModel.geoCoordResponses.put(DST_CITY_NAME, response);
        StepVerifier.create(workFlow.apply(internalDataModel)).expectNext(internalDataModel).expectComplete().verify();
    }
}
