package com.sap.assignment.emissioncalculator.models;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class InternalDataModel {
    public RequestParameters requestParameters;

    public Map<String, GeoCodeSearchResponse> geoCoordResponses = new HashMap<>();

    public Map<String, List<Double>> cityCoords;

    public double distance;
    public double co2emission;
}
