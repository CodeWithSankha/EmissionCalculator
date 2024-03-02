package com.sap.assignment.emissioncalculator.controller;


import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import com.sap.assignment.emissioncalculator.models.RequestParameters;
import com.sap.assignment.emissioncalculator.service.EmissionCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

//@Component
public class EmissionCalculatorCmdController implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(EmissionCalculatorCmdController.class);

    @Autowired
    private EmissionCalculatorService service;

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
        InternalDataModel model = new InternalDataModel();
        model.requestParameters = new RequestParameters(
                args.getOptionValues("start").get(0),
                args.getOptionValues("end").get(0),
                args.getOptionValues("transportation-method").get(0));
        service.calculateEmission(model).subscribe(data -> {
            logger.info("Your trip caused {}kg of CO2-equivalent", String.format("%.3f", data.co2emission));
        });
    }
}
