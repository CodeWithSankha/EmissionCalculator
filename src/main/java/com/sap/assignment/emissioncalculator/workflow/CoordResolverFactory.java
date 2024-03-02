package com.sap.assignment.emissioncalculator.workflow;

import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.function.Consumer;

@Configuration
public class CoordResolverFactory {

    @Inject
    private FirstCoordResolver firstCoordResolver;


    public CoordResolver getCoordResolver(String resolverType) throws RuntimeException {
        switch (resolverType) {
            case "fetch_first_coord":
                return firstCoordResolver;
            default:
                throw new RuntimeException("Undefined coord resolver type:" + resolverType);
        }
    }

    public interface CoordResolver {
        void resolveCoordinates(InternalDataModel internalDataModel, String cityName);
    }

    @Component
    static class FirstCoordResolver implements CoordResolver {

        private static final Logger logger = LoggerFactory.getLogger(FirstCoordResolver.class);

        @Override
        public void resolveCoordinates(InternalDataModel internalDataModel, String cityName) {
            internalDataModel.cityCoords.put(cityName, internalDataModel.geoCoordResponses.get(cityName).features.get(0).geometry.coordinates);
            logger.info("City Coords {}", internalDataModel.cityCoords.get(cityName));
        }
    }
}
