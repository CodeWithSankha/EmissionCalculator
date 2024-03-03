package com.sap.assignment.emissioncalculator.exceptions;

public class InvalidCityNameException extends RuntimeException {
    public InvalidCityNameException(String msg) {
        super(msg);
    }
}
