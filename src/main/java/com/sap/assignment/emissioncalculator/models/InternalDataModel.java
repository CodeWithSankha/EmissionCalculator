package com.sap.assignment.emissioncalculator.models;

import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Function;

public class InternalDataModel {
    public RequestParameters requestParameters;
    public Flux<GeoCodeSearchResponse> startCityGeoCoordResponse;
    public Flux<GeoCodeSearchResponse> endCityCoordResponse;

    public List<Double> startCityCoord;
    public List<Double> endCityCoord;

    public double distance;
    public double co2emission;
}
