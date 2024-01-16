package com.gfl.client.service.scenario;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;

import java.util.List;
import java.util.Queue;

public interface QueueHolder {

    Queue<ScenarioRequest> getQueue();

    List<ScenarioRequest> takeScenarioFromQueue();

    void addAllScenarioQueue(List<ScenarioRequest> scenarios);

    void addAllExecutedScenarios(List<ScenarioResult> scenarios);

    List<ScenarioResult> takeExecutedScenarios();
}
