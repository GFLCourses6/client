package com.gfl.client.controller;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.StepRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ScenariosArgumentsProvider
        implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(Arguments.of(Arrays.asList(new ScenarioRequest("user",
                                                                        "Scenario click",
                                                                        "https://google.com",
                                                                        List.of(new StepRequest(
                                                                                "click",
                                                                                "button"))),
                                                    new ScenarioRequest(
                                                            "anotherUser",
                                                            "Scenario sleep",
                                                            "https://github.com",
                                                            List.of(new StepRequest(
                                                                    "input",
                                                                    "text"))))));
    }
}