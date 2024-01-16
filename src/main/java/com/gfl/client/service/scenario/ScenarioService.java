package com.gfl.client.service.scenario;

import com.gfl.client.model.ScenarioResult;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ScenarioService {

    ResponseEntity<List<ScenarioResult>> sendScenarios();
}
