package com.gfl.client.service.scenario;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ScenarioService {
    ResponseEntity<List<ScenarioResult>> getExecutedScenarios(String username);

    ResponseEntity<Void> sendScenarios(String username, List<ScenarioRequest> scenarios);

    ResponseEntity<List<ScenarioRequest>> getScenariosFromQueue(String username);

    ResponseEntity<List<ScenarioRequest>> getScenariosFromQueue(String username, String scenarioName);
}
