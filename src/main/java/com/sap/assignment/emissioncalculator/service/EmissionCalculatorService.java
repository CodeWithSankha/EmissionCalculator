package com.sap.assignment.emissioncalculator.service;

import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import com.sap.assignment.emissioncalculator.workflow.CoordinateResolverWorkflow;
import com.sap.assignment.emissioncalculator.workflow.GeoCordFetcherWorkFlow;
import com.sap.assignment.emissioncalculator.workflow.DistanceFetcherWorkFlow;
import com.sap.assignment.emissioncalculator.workflow.EmissionCalculatorWorkflow;
import com.sap.assignment.emissioncalculator.workflow.RequestTransferWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class EmissionCalculatorService implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(EmissionCalculatorService.class);

    @Autowired
    private RequestTransferWorkflow requestTransferWorkflow;

    @Autowired
    private GeoCordFetcherWorkFlow geoCordFetcherWorkFlow;

    @Autowired
    private CoordinateResolverWorkflow coordinateResolverWorkflow;
    @Autowired
    private DistanceFetcherWorkFlow distanceFetcherWorkFlow;
    @Autowired
    private EmissionCalculatorWorkflow emissionCalculatorWorkflow;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.getOptionNames().isEmpty()) {
            String msg = "Application must have args";
            logger.error(msg);
            logger.error("./co2-calculator --start \"Los Angeles\" --end \"New York\" --transportation-\n" +
                    "method=medium-diesel-car");
            return;
            // TODO throw new InvalidArgsException(msg);
        }
        System.out.println("# NonOptionArgs: " + args.getNonOptionArgs().size());

        System.out.println("NonOptionArgs:");
        args.getNonOptionArgs().forEach(System.out::println);

        System.out.println("# OptionArgs: " + args.getOptionNames().size());
        System.out.println("OptionArgs:");

        args.getOptionNames().forEach(optionName -> {
            System.out.println(optionName + "=" + args.getOptionValues(optionName));
        });

        Flux<InternalDataModel> model = Flux.just(args)
                .flatMap(requestTransferWorkflow)
                .flatMap(geoCordFetcherWorkFlow);/*
                .map(coordinateResolverWorkflow)
                .map(distanceFetcherWorkFlow)
                .map(emissionCalculatorWorkflow);*/


        model.log()
                .subscribe(data -> {
                    logger.info("Your trip caused {}kg of CO2-equivalent", data.co2emission);
                });
                //.flatMap(emissionCalculatorWorkflow)

        // logger.info("Your trip caused {}kg of CO2-equivalent", model.co2emission);
        /*

                .subscribe(data -> {
                    logger.info("Your trip caused {}kg of CO2-equivalent", data.co2emission);
                });*/
    }
}
