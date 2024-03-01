package com.sap.assignment.emissioncalculator.workflow;

import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.function.Consumer;

@Configuration
public class CoordResolverFactory {

    @Inject
    private FirstCoordResolver firstCoordResolver;


    public CoordResolver getCoordResolver(String resolverType) throws Exception {
        switch (resolverType) {
            case "fetch_first_coord":
                return firstCoordResolver;
            default:
                throw new Exception("Undefined coord resolver type:" + resolverType);
        }
    }

    public interface CoordResolver {
        Consumer<InternalDataModel> resolveCoordinates(InternalDataModel internalDataModel);
    }

    @Component
    static class FirstCoordResolver implements CoordResolver {

        @Override
        public Consumer<InternalDataModel> resolveCoordinates(InternalDataModel internalDataModel) {
            return (data) -> {
                String startCityName = internalDataModel.requestParameters.startCity().apply(internalDataModel.requestParameters.args());
                internalDataModel.cityCoords.put(startCityName, internalDataModel.geoCoordResponses.get(startCityName).features.get(0).geometry.coordinates);

                String endCityName = internalDataModel.requestParameters.endCity().apply(internalDataModel.requestParameters.args());
                internalDataModel.cityCoords.put(endCityName, internalDataModel.geoCoordResponses.get(endCityName).features.get(0).geometry.coordinates);
            };
        }
    }
}
