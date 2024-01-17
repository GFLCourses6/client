package com.gfl.client.service.scenario;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ScenarioService {
    List<ScenarioResult> getExecutedScenarios(String username);

    List<ScenarioRequest> getScenariosFromQueue(String username);

    ResponseEntity<Void> sendScenarios(List<ScenarioRequest> requests);
}
