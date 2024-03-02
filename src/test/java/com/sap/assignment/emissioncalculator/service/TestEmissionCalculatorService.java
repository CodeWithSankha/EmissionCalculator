package com.sap.assignment.emissioncalculator.service;

import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import com.sap.assignment.emissioncalculator.workflow.DistanceFetcherWorkFlow;
import com.sap.assignment.emissioncalculator.workflow.EmissionCalculatorWorkflow;
import com.sap.assignment.emissioncalculator.workflow.GeoCordFetcherWorkFlow;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@SpringBootTest()
@TestPropertySource(locations="classpath:test.properties")
public class TestEmissionCalculatorService {

    @InjectMocks
    private EmissionCalculatorService service;

    @Mock
    private GeoCordFetcherWorkFlow geoCordFetcherWorkFlow;

    @Mock
    private DistanceFetcherWorkFlow distanceFetcherWorkFlow;
    @Mock
    private EmissionCalculatorWorkflow emissionCalculatorWorkflow;

    @Test
    @SetEnvironmentVariable(key = "ORS_TOKEN", value = "5b3ce3597851110001cf62482fe19359ac854b2cb7ced2aa1a72e6a2")
    public void testService() {
        InternalDataModel model = new InternalDataModel();
        Assert.assertNotNull(service.calculateEmission(model));
    }

}
