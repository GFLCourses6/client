package com.gfl.client.service;

import com.gfl.client.model.ScenarioRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ScenarioService {

    ResponseEntity<Void> sendScenarios(List<ScenarioRequest> scenarios);
}
