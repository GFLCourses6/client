package com.gfl.client.controller;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
import com.gfl.client.service.scenario.RestTemplateScenarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/source")
public class ScenarioSourceController {

    private final RestTemplateScenarioService restTemplateScenarioService;


    public ScenarioSourceController(RestTemplateScenarioService restTemplateScenarioService) {
        this.restTemplateScenarioService = restTemplateScenarioService;
    }

    @PostMapping
    public ResponseEntity<Void> sendScenarios(@RequestBody @Valid List<ScenarioRequest> scenarios){
        return restTemplateScenarioService.sendScenarios(scenarios);
    }

    @GetMapping("/executed")
    public ResponseEntity<List<ScenarioResult>> getExecutedScenarios(@PathVariable("username") String username){
        return restTemplateScenarioService.getExecutedScenarios(username);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueue(@PathVariable("username") String username){
        return restTemplateScenarioService.getScenariosFromQueue(username);
    }

    @GetMapping
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueue(@PathVariable("username")String username,
                                                                @PathVariable("scenarioName")String scenarioName){
        return restTemplateScenarioService.getScenariosFromQueue(username, scenarioName);
    }

}

