package com.gfl.client.service.scenario;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class ScenarioQueueHolder implements QueueHolder {

    private final BlockingQueue<ScenarioRequest> scenarioQueue = new LinkedBlockingQueue<>();

    private final List<ScenarioResult> executedScenarios = new ArrayList<>();

    @Override
    public BlockingQueue<ScenarioRequest> getQueue() {
        return scenarioQueue;
    }

    @Override
    public List<ScenarioRequest> takeScenarioFromQueue() {
        List<ScenarioRequest> scenarioRequests = new ArrayList<>();
        while (!scenarioQueue.isEmpty()) {
            scenarioRequests.add(scenarioQueue.poll());
        }
        return scenarioRequests;
    }

    @Override
    public void addAllScenarioQueue(
            List<ScenarioRequest> scenarios) {
        scenarioQueue.addAll(scenarios);
    }

    @Override
    public void addAllExecutedScenarios(
            List<ScenarioResult> scenarios) {
        executedScenarios.addAll(scenarios);
    }

    @Override
    public List<ScenarioResult> takeExecutedScenarios() {
        List<ScenarioResult> result = new ArrayList<>(executedScenarios);
        executedScenarios.clear();
        return result;
    }
}
