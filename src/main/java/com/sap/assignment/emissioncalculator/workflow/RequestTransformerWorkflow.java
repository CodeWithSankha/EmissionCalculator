package com.sap.assignment.emissioncalculator.workflow;

import com.sap.assignment.emissioncalculator.models.InternalDataModel;
import com.sap.assignment.emissioncalculator.models.RequestParameters;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Component
public class RequestTransformerWorkflow implements Function<ApplicationArguments, Flux<InternalDataModel>> {
    @Override
    public Flux<InternalDataModel> apply(ApplicationArguments args) {
        InternalDataModel internalDataModel = new InternalDataModel();
        internalDataModel.requestParameters = new RequestParameters(args);
        return Flux.just(internalDataModel);
    }
}
