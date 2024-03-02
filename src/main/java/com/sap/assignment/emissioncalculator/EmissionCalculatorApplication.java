package com.sap.assignment.emissioncalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class EmissionCalculatorApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(EmissionCalculatorApplication.class)
				.web(WebApplicationType.REACTIVE)
				.run(args);
	}
}
