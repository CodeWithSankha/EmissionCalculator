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
    public InternalDataModel apply(InternalDataModel internalDataModel) {
        final String vehicleType = internalDataModel.requestParameters.vehicleType().apply(internalDataModel.requestParameters.args());
        try {
            final int emissionRate = VehicleEmissionRate.getEmissionRateByVehicleType(vehicleType);
            final double distance = internalDataModel.distance;
            internalDataModel.co2emission = (distance * emissionRate);
        } catch (InvalidVehicleTypeException e) {
            throw new RuntimeException(e);
        }

        return internalDataModel;
    }
}
