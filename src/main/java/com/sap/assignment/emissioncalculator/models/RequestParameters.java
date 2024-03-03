package com.sap.assignment.emissioncalculator.models;

public record RequestParameters(String startCity,
                                String endCity,
                                String transportationMode
) {
}
