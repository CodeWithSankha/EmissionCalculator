package com.sap.assignment.emissioncalculator.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
public class GeoCodeSearchResponse {

    @JsonProperty("geocoding")
    public GeoCoding geoCoding;

    @JsonProperty("features")
    public List<Feature> features;

    @ToString
    public static class GeoCoding {
        @JsonProperty("query")
        public Query query;
    }

    @ToString
    public static class Query {
        @JsonProperty("text")
        public String cityName;
    }

    @ToString
    public static class Feature {
        @JsonProperty("geometry")
        public Geometry geometry;
        @JsonProperty("properties")
        public Map<String, String> properties;
    }

    @ToString
    public static class Geometry {
        @JsonProperty("coordinates")
        public List<Double> coordinates;
    }
}
