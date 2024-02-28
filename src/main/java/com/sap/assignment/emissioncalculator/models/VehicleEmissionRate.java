package com.sap.assignment.emissioncalculator.models;

import com.google.common.collect.ImmutableMap;
import com.sap.assignment.emissioncalculator.exceptions.InvalidVehicleTypeException;
import io.micrometer.common.util.StringUtils;

public abstract class VehicleEmissionRate {

    // Emission in grams per KM
    private static final ImmutableMap<String, Integer> emissionRateMap = ImmutableMap.<String, Integer>builder()
            // SMALL CARS
            .put("small-diesel-car", 142)
            .put("small-petrol-car", 145)
            .put("small-plugin-hybrid-car", 73)
            .put("small-electric-car", 50)

            // Medium cars
            .put("medium-diesel-car", 171)
            .put("medium-petrol-car", 192)
            .put("medium-plugin-hybrid-car", 110)
            .put("medium-electric-car", 58)

            // Large cars
            .put("large-diesel-car", 209)
            .put("large-petrol-car", 282)
            .put("large-plugin-hybrid-car", 126)
            .put("large-electric-car", 73)

            // Bus
            .put("bus", 27)

            // Train
            .put("train", 6)
            .build();

    public static int getEmissionRateByVehicleType(final String vehicleType) throws InvalidVehicleTypeException {
        if (StringUtils.isEmpty(vehicleType)) {
            throw new InvalidVehicleTypeException("Vehicle Type Can't be null");
        }
        if (emissionRateMap.containsKey(vehicleType)) {
            throw new InvalidVehicleTypeException("Invalid Vehicle Type :" + vehicleType);
        }
        return emissionRateMap.get(vehicleType);
    }
}
