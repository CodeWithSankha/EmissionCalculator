package com.sap.assignment.emissioncalculator.workflow;

import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import com.sap.assignment.emissioncalculator.models.RequestParameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class TestEmissionCalculatorWorkflow {

    @InjectMocks
    private EmissionCalculatorWorkflow workflow;


    @Test
    public void testDistance() {
        InternalDataModel data = new InternalDataModel();
        data.distance = 1000.0f;
        data.requestParameters = new RequestParameters("berlin", "hamburg", "medium-diesel-car");
        workflow.apply(data);
        Assert.assertTrue("Emission", 171.0f == data.co2emission);

    }
}
