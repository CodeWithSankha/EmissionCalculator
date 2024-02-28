package com.sap.assignment.emissioncalculator.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineParser implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("# NonOptionArgs: " + args.getNonOptionArgs().size());

        System.out.println("NonOptionArgs:");
        args.getNonOptionArgs().forEach(System.out::println);

        System.out.println("# OptionArgs: " + args.getOptionNames().size());
        System.out.println("OptionArgs:");

        args.getOptionNames().forEach(optionName -> {
            System.out.println(optionName + "=" + args.getOptionValues(optionName));
        });
    }
}
