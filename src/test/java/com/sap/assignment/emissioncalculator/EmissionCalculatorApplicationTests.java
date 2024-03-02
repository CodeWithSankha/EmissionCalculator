package com.sap.assignment.emissioncalculator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"ORS_TOKEN = foo"})
class EmissionCalculatorApplicationTests {

	@Test
	void contextLoads() {
	}

}
