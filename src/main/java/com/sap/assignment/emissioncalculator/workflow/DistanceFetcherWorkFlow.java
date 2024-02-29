package com.sap.assignment.emissioncalculator.workflow;

import com.sap.assignment.emissioncalculator.models.Coordinates;
import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DistanceFetcherWorkFlow implements Function<InternalDataModel, InternalDataModel> {

    // TODO Should call to external API to fetch coords
    @Override
    public InternalDataModel apply(InternalDataModel internalDataModel) {
        internalDataModel.distance = getDistanceBetween(internalDataModel.startCityCoord, internalDataModel.endCityCoord);
        return internalDataModel;
    }

    private double getDistanceBetween(Coordinates startCityCoord, Coordinates endCityCoord) {
        return 4000.0;
    }

}
