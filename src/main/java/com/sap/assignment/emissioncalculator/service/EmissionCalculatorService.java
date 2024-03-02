package com.sap.assignment.emissioncalculator.service;

import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import com.sap.assignment.emissioncalculator.workflow.CoordinateResolverWorkflow;
import com.sap.assignment.emissioncalculator.workflow.DistanceFetcherWorkFlow;
import com.sap.assignment.emissioncalculator.workflow.EmissionCalculatorWorkflow;
import com.sap.assignment.emissioncalculator.workflow.GeoCordFetcherWorkFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Component
public class EmissionCalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(EmissionCalculatorService.class);

    @Autowired
    private GeoCordFetcherWorkFlow geoCordFetcherWorkFlow;

    @Autowired
    private CoordinateResolverWorkflow coordinateResolverWorkflow;
    @Autowired
    private DistanceFetcherWorkFlow distanceFetcherWorkFlow;
    @Autowired
    private EmissionCalculatorWorkflow emissionCalculatorWorkflow;

    public Flux<InternalDataModel> calculateEmission(InternalDataModel dataModel) {
        return Flux.just(dataModel)
                .flatMap(geoCordFetcherWorkFlow)
                .flatMap(coordinateResolverWorkflow)
                .flatMap(distanceFetcherWorkFlow)
                .map(emissionCalculatorWorkflow);
    }
}
