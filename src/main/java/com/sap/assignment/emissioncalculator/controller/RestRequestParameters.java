package com.sap.assignment.emissioncalculator.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestRequestParameters {
    @JsonProperty("start_city")
    public String startCity;
    @JsonProperty("end_city")
    public String endCity;

    @JsonProperty("transportation_mode")
    public String transportationMode;
}
