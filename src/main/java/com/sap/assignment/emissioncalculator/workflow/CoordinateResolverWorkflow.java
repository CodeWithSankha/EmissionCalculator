package com.sap.assignment.emissioncalculator.workflow;

import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CoordinateResolverWorkflow implements Function<InternalDataModel, InternalDataModel> {

    private static final Logger logger = LoggerFactory.getLogger(CoordinateResolverWorkflow.class);

    @Value("${coord.resolve.preference}")
    private String coordResolvePreference;

    @Autowired
    private CoordResolverFactory coordResolverFactory;

    @Override
    public InternalDataModel apply(InternalDataModel internalDataModel) {
        logger.info("coordResolvePreference: {}", coordResolvePreference);
        try {
            CoordResolverFactory.CoordResolver coordResolver = coordResolverFactory.getCoordResolver(coordResolvePreference);
            coordResolver.resolveCoord(internalDataModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return internalDataModel;
    }
}
