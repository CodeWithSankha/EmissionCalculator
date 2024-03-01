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


    public CoordResolver getCoordResolver(String resolverType) throws Exception {
        switch (resolverType) {
            case "fetch_first_coord":
                return firstCoordResolver;
            default:
                throw new Exception("Undefined coord resolver type:" + resolverType);
        }
    }

    public interface CoordResolver {
        void resolveCoordinates(InternalDataModel internalDataModel);
    }

    @Component
    static class FirstCoordResolver implements CoordResolver {

        private static final Logger logger = LoggerFactory.getLogger(FirstCoordResolver.class);

        @Override
        public void resolveCoordinates(InternalDataModel internalDataModel) {
            String startCityName = internalDataModel.requestParameters.startCity().apply(internalDataModel.requestParameters.args());
            internalDataModel.cityCoords.put(startCityName, internalDataModel.geoCoordResponses.get(startCityName).features.get(0).geometry.coordinates);

            String endCityName = internalDataModel.requestParameters.endCity().apply(internalDataModel.requestParameters.args());
            internalDataModel.cityCoords.put(endCityName, internalDataModel.geoCoordResponses.get(endCityName).features.get(0).geometry.coordinates);
            logger.info("City Coords {}", internalDataModel.cityCoords);
        }
    }
}
