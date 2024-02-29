package com.sap.assignment.emissioncalculator.workflow;

import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import com.sap.assignment.emissioncalculator.models.RequestParameters;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RequestTransferWorkflow implements Function<ApplicationArguments, InternalDataModel> {
    @Override
    public InternalDataModel apply(ApplicationArguments args) {
        InternalDataModel internalDataModel = new InternalDataModel();
        internalDataModel.requestParameters = new RequestParameters(args);
        return internalDataModel;
    }
}
