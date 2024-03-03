package com.sap.assignment.emissioncalculator.workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sap.assignment.emissioncalculator.exceptions.InvalidCityNameException;
import com.sap.assignment.emissioncalculator.models.GeoCodeSearchResponse;
import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class TestFirstCoordResolver {

    @InjectMocks
    private CoordResolverFactory.FirstCoordResolver resolver;

    private JsonMapper jsonMapper = new JsonMapper();

    private String CITY_NAME = "hamburg";

    @Before
    public void init() throws JsonProcessingException {
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    public void testFirstCoordResolver() {
        Path resourceDirectory = Paths.get("src", "test", "resources", "data");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        GeoCodeSearchResponse response = null;
        try {
            response = jsonMapper.readValue(new File(absolutePath + "/geocode_hamburg.json"), GeoCodeSearchResponse.class);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        final InternalDataModel internalDataModel = new InternalDataModel();
        internalDataModel.geoCoordResponses.put(CITY_NAME, response);

        resolver.resolveCoordinates(internalDataModel, CITY_NAME);

        Assert.assertNotNull(internalDataModel.cityCoords.get(CITY_NAME));
    }

    @Test(expected = InvalidCityNameException.class)
    public void testFirstCoordResolverWithInvalidCityName() {
        Path resourceDirectory = Paths.get("src", "test", "resources", "data");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        GeoCodeSearchResponse response = null;
        try {
            response = jsonMapper.readValue(new File(absolutePath + "/geocode_empty_coords.json"), GeoCodeSearchResponse.class);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        final InternalDataModel internalDataModel = new InternalDataModel();
        internalDataModel.geoCoordResponses.put("invalid", response);
        resolver.resolveCoordinates(internalDataModel, CITY_NAME);
    }

    @Test(expected = InvalidCityNameException.class)
    public void testFirstCoordResolverWithEmptyCoords() {
        Path resourceDirectory = Paths.get("src", "test", "resources", "data");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        GeoCodeSearchResponse response = null;
        try {
            response = jsonMapper.readValue(new File(absolutePath + "/geocode_hamburg.json"), GeoCodeSearchResponse.class);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        response.features.clear();
        final InternalDataModel internalDataModel = new InternalDataModel();
        internalDataModel.geoCoordResponses.put("invalid", response);
        resolver.resolveCoordinates(internalDataModel, CITY_NAME);
    }
}
