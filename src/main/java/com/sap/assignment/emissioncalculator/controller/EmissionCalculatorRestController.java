package com.sap.assignment.emissioncalculator.controller;


import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import com.sap.assignment.emissioncalculator.models.RequestParameters;
import com.sap.assignment.emissioncalculator.service.EmissionCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class EmissionCalculatorRestController {

    @Autowired
    private EmissionCalculatorService service;

    @PostMapping("/calculate_emission")
    public ResponseEntity<String> calculate_emission(@RequestBody RestRequestParameters body) {

        try {
            InternalDataModel model = new InternalDataModel();
            model.requestParameters = new RequestParameters(body.startCity, body.endCity, body.transportationMode);
            service.calculateEmission(model).log().blockFirst();
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(String.format("Your trip caused %.3fkg of CO2-equivalent", model.co2emission));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request Malformed:" + ex.getMessage());
        }
    }
}
