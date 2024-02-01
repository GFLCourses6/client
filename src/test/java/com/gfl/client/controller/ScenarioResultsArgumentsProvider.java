package com.gfl.client.controller;

import com.gfl.client.model.ExecutionStatus;
import com.gfl.client.model.ScenarioResult;
import com.gfl.client.model.Step;
import com.gfl.client.model.StepResult;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

public class ScenarioResultsArgumentsProvider
        implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        Step step = new Step("click", "button");
        StepResult stepResult = new StepResult(1L,
                                               "action",
                                               "value",
                                               "Execution successful",
                                               ExecutionStatus.SUCCESS,
                                               Instant.now());
        return Stream.of(Arguments.of(Arrays.asList(new ScenarioResult(1L,
                                                                       "Scenario click",
                                                                       "https://google.com",
                                                                       "user",
                                                                       "proxy",
                                                                       null,
                                                                       singletonList(
                                                                               stepResult)),
                                                    new ScenarioResult(1L,
                                                                       "Scenario sleep",
                                                                       "https://google.com",
                                                                       "admin",
                                                                       "proxy",
                                                            null,
                                                                       singletonList(
                                                                               stepResult)))));
    }
}
