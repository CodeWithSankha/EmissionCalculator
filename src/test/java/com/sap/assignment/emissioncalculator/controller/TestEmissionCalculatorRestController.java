package com.sap.assignment.emissioncalculator.controller;

import com.sap.assignment.emissioncalculator.service.EmissionCalculatorService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest()
@TestPropertySource(locations="classpath:test.properties")
public class TestEmissionCalculatorRestController {

    @InjectMocks
    private EmissionCalculatorRestController controller;

    @Mock
    private EmissionCalculatorService service;

    @Test
    public void testController() {
        RestRequestParameters parameters = new RestRequestParameters();
        parameters.startCity = "start_city";
        parameters.endCity = "end_city";
        parameters.transportationMode = "transportation_mode";

        Assert.assertNotNull(controller.calculate_emission(parameters));
    }

    @Test
    public void testControllerWoStartCity() {
        RestRequestParameters parameters = new RestRequestParameters();
        parameters.endCity = "end_city";
        parameters.transportationMode = "transportation_mode";
        Assert.assertTrue("EndCity", Objects.requireNonNull(controller.calculate_emission(parameters).getBody()).contains("start_city"));
    }
    @Test
    public void testControllerWoEndCity() {
        RestRequestParameters parameters = new RestRequestParameters();
        parameters.startCity = "start_city";
        parameters.transportationMode = "transportation_mode";
        Assert.assertTrue("EndCity", Objects.requireNonNull(controller.calculate_emission(parameters).getBody()).contains("end_city"));
    }

    @Test
    public void testControllerWoTransportation() {
        RestRequestParameters parameters = new RestRequestParameters();
        parameters.startCity = "start_city";
        parameters.endCity = "end_city";
        Assert.assertTrue("EndCity", Objects.requireNonNull(controller.calculate_emission(parameters).getBody()).contains("transportation_mode"));
    }
}
