package com.sap.assignment.emissioncalculator.workflow;

import com.sap.assignment.emissioncalculator.enums.CoordResolverEnum;
import com.sap.assignment.emissioncalculator.exceptions.InvalidCityNameException;
import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.inject.Inject;


@Configuration
public class CoordResolverFactory {

    @Inject
    private FirstCoordResolver firstCoordResolver;


    public CoordResolver getCoordResolver(String resolverType) throws RuntimeException {
        CoordResolverEnum resolverEnum;
        try {
            resolverEnum = Enum.valueOf(CoordResolverEnum.class, resolverType.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid Resolver Type:" + resolverType);
        }
        switch (resolverEnum) {
            case FETCH_FIRST_COORD:
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
            if (!internalDataModel.geoCoordResponses.containsKey(cityName)) {
                throw new InvalidCityNameException("Unable to resolve coord for cityName: " + cityName);
            }
            if (internalDataModel.geoCoordResponses.get(cityName).features == null || internalDataModel.geoCoordResponses.get(cityName).features.isEmpty()) {
                throw new InvalidCityNameException("City doesn't have a valid coordinate, cityName: " + cityName);
            }
            internalDataModel.cityCoords.put(cityName, internalDataModel.geoCoordResponses.get(cityName).features.get(0).geometry.coordinates);
            logger.info("City Coords {}", internalDataModel.cityCoords.get(cityName));

        }
    }
}
