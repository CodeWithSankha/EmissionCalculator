package com.sap.assignment.emissioncalculator.enums;

public enum CoordResolverEnum {
    FETCH_FIRST_COORD("fetch_first_coord");


    public String getVal() {
        return this.val;
    }

    private final String val;

    CoordResolverEnum(String val) {
        this.val = val;
    }

}
