package com.sap.assignment.emissioncalculator.workflow;

import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

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
        void resolveCoord(InternalDataModel internalDataModel);
    }

    @Component
    static class FirstCoordResolver implements CoordResolver {

        @Override
        public void resolveCoord(InternalDataModel internalDataModel) {
            internalDataModel.startCityGeoCoordResponse
                    .subscribe(s -> {
                        internalDataModel.startCityCoord = s.features.get(0).geometry.coordinates;
                    });
            internalDataModel.startCityGeoCoordResponse
                    .subscribe(s -> {
                        internalDataModel.endCityCoord = s.features.get(0).geometry.coordinates;
                    });
        }
    }
}
