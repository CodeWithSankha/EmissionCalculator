package com.sap.assignment.emissioncalculator.models;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public record Coordinates(String cityName, double[] coords) {
    public List<Double> toList() {
        return Arrays.asList(coords[0], coords[1]);
    }
}
