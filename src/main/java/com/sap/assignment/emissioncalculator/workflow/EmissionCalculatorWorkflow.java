package com.sap.assignment.emissioncalculator.workflow;

import com.sap.assignment.emissioncalculator.exceptions.InvalidVehicleTypeException;
import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import com.sap.assignment.emissioncalculator.models.VehicleEmissionRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EmissionCalculatorWorkflow implements Function<InternalDataModel, InternalDataModel> {
    private static final Logger logger = LoggerFactory.getLogger(EmissionCalculatorWorkflow.class);

    @Override
    public InternalDataModel apply(InternalDataModel data) {
        final String transportationMode = data.requestParameters.transportationMode();
        try {
            final int emissionRate = VehicleEmissionRate.getEmissionRateByVehicleType(transportationMode);
            final double distance = data.distance;
            data.co2emission = (distance * emissionRate) / 1000;
        } catch (InvalidVehicleTypeException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
