package com.sap.assignment.emissioncalculator.models;

import org.springframework.boot.ApplicationArguments;

import java.util.function.Function;

public record RequestParameters(String startCity,
                                String endCity,
                                String transportationMode
) {
/*    public Function<ApplicationArguments, String> startCity() {
        return (args) -> args.getOptionValues("start").get(0);
    }

    public Function<ApplicationArguments, String> endCity() {
        return (args) -> args.getOptionValues("end").get(0);
    }

    public Function<ApplicationArguments, String> transportationMode() {
        return (args) -> args.getOptionValues("transportation-method").get(0);
    }*/
}
