package com.gfl.client.controller;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
import com.gfl.client.service.scenario.RestTemplateScenarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/scenario")
public class ScenarioSourceController {

    private final RestTemplateScenarioService restTemplateScenarioService;

    private final Logger logger = LoggerFactory.getLogger(ScenarioSourceController.class);


    public ScenarioSourceController(RestTemplateScenarioService restTemplateScenarioService) {
        this.restTemplateScenarioService = restTemplateScenarioService;
    }

    @PostMapping
    public void sendScenarios(@RequestBody @NotEmpty(message = "scenarioRequest list can`not be empty")
                                  List<@Valid ScenarioRequest> scenarios){
        logger.info("sending scenarios, size = {}",  scenarios.size());
        restTemplateScenarioService.sendScenarios(scenarios);
    }

    @GetMapping("/executed/{username}")
    public ResponseEntity<List<ScenarioResult>> getExecutedScenarios(
            @NotBlank(message = "username can't be blank")
            @PathVariable("username") String username){
        return restTemplateScenarioService.getExecutedScenarios(username);
    }

    @GetMapping("/queue/{username}")
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueue(
            @NotBlank(message = "username can't be blank")
            @PathVariable("username")  String username){
        return restTemplateScenarioService.getScenariosFromQueue(username);
    }

    @GetMapping("/queue/{username}/{scenarioName}")
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueue(
            @NotBlank(message = "username can't be blank")
            @PathVariable("username")String username,
            @NotBlank(message = "scenarioName can't be blank")
            @PathVariable("scenarioName")String scenarioName){
        return restTemplateScenarioService.getScenariosFromQueue(username, scenarioName);
    }

}

