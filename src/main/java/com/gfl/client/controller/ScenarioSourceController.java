package com.gfl.client.controller;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.service.ScenarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/scenario")
public class ScenarioSourceController {

    private final ScenarioService service;

    public ScenarioSourceController(ScenarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Integer> createScenarios(
            @RequestBody @Valid List<ScenarioRequest> scenarios) {
        int statusCode = service.sendListHttp(scenarios);
        return ResponseEntity.status(statusCode).body(statusCode);
    }
}
