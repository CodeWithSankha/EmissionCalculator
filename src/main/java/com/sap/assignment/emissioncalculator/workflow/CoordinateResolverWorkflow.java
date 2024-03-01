package com.sap.assignment.emissioncalculator.workflow;

import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Component
public class CoordinateResolverWorkflow implements Function<InternalDataModel, Flux<InternalDataModel>> {

    private static final Logger logger = LoggerFactory.getLogger(CoordinateResolverWorkflow.class);

    @Value("${coord.resolve.preference}")
    private String coordResolvePreference;

    @Autowired
    private CoordResolverFactory coordResolverFactory;

    @Override
    public Flux<InternalDataModel> apply(InternalDataModel model) {
        return Flux.just(model)
                .map(data -> {
                    logger.info("coordResolvePreference: {}", coordResolvePreference);
                    try {
                        CoordResolverFactory.CoordResolver coordResolver = coordResolverFactory.getCoordResolver(coordResolvePreference);
                        coordResolver.resolveCoordinates(data);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return data;
                });
    }
}
