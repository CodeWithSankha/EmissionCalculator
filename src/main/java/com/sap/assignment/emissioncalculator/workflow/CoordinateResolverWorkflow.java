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

    @Value("${coordinate.resolver.preference}")
    private String coordResolverPreference;

    @Autowired
    private CoordResolverFactory coordResolverFactory;

    @Override
    public Flux<InternalDataModel> apply(InternalDataModel data) {
        return Flux.just(data).map(s -> {
            try {
                logger.info("coordResolverPreference: {}", coordResolverPreference);
                CoordResolverFactory.CoordResolver coordResolver = coordResolverFactory.getCoordResolver(coordResolverPreference);
                coordResolver.resolveCoordinates(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return data;
        });
    }
}
