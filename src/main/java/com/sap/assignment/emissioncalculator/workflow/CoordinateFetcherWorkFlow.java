package com.sap.assignment.emissioncalculator.workflow;

import com.sap.assignment.emissioncalculator.models.Coordinates;
import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CoordinateFetcherWorkFlow implements Function<InternalDataModel, InternalDataModel> {

    // TODO Should call to external API to fetch coords
    @Override
    public InternalDataModel apply(InternalDataModel internalDataModel) {
        internalDataModel.startCityCoord = getCoordsFor(internalDataModel.requestParameters.startCity(), internalDataModel.requestParameters.args());
        return internalDataModel;
    }

    private Coordinates getCoordsFor(Function<ApplicationArguments, String> cityName, ApplicationArguments args) {
        if (cityName.apply(args).equals("Los Angeles")) {
            return new Coordinates(cityName.apply(args), new double[]{0.01f, 0002f});
        }
        if (cityName.apply(args).equals("New York")) {
            return new Coordinates(cityName.apply(args), new double[]{0.01f, 0002f});
        }
        return null;
    }


}
