package com.gfl.client.controller;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
import com.gfl.client.service.scenario.RestTemplateScenarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/scenario")
public class ScenarioSourceController implements ScenarioController {

    private final RestTemplateScenarioService restTemplateScenarioService;

    private final Logger logger = LoggerFactory.getLogger(ScenarioSourceController.class);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendScenariosRequest(
            @RequestBody @NotEmpty(message = "scenarioRequest list can not be empty")
            List<@Valid ScenarioRequest> scenarios) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("sending {}'s scenarios, size = {}", username, scenarios.size());
        return restTemplateScenarioService.sendScenarios(username, scenarios);
    }

    @GetMapping(value = "/executed/{username}")
    public ResponseEntity<List<ScenarioResult>> getExecutedScenarios(
            @NotBlank(message = "username can't be blank")
            @PathVariable String username) {
        return restTemplateScenarioService.getExecutedScenarios(username);
    }

    @GetMapping(value = "/queue/{username}")
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueueByUsername(
            @NotBlank(message = "username can't be blank")
            @PathVariable String username) {
        return restTemplateScenarioService.getScenariosFromQueue(username);
    }

    @GetMapping(value = "/queue/{username}/{scenarioName}")
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueueByUsernameAndScenarioName(
            @NotBlank(message = "username can't be blank")
            @PathVariable String username,
            @NotBlank(message = "scenarioName can't be blank")
            @PathVariable String scenarioName) {
        return restTemplateScenarioService.getScenariosFromQueue(username, scenarioName);
    }
}
