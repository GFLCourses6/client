package com.gfl.client.controller;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
import com.gfl.client.service.scenario.RestTemplateScenarioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
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
    public ResponseEntity<Void> sendScenariosRequest(@RequestBody List<ScenarioRequest> scenarios) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("sending {}'s scenarios, size = {}", username, scenarios.size());
        return restTemplateScenarioService.sendScenarios(username, scenarios);
    }

    @PreAuthorize("@SecurityAccessHandler.authHasName(#username)")
    @GetMapping(value = "/executed/{username}")
    public ResponseEntity<List<ScenarioResult>> getExecutedScenarios(
            @PathVariable("username") String username) {
        return restTemplateScenarioService.getExecutedScenarios(username);
    }

    @PreAuthorize("@SecurityAccessHandler.authHasName(#username)")
    @GetMapping(value = "/queue/{username}")
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueueByUsername(
            @P("username") @PathVariable("username") String username) {
        return restTemplateScenarioService.getScenariosFromQueue(username);
    }

    @PreAuthorize("@SecurityAccessHandler.authHasName(#username)")
    @GetMapping(value = "/queue/{username}/{scenarioName}")
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueueByUsernameAndScenarioName(
            @P("username") @PathVariable("username") String username,
            @PathVariable("scenarioName") String scenarioName) {
        return restTemplateScenarioService.getScenariosFromQueue(username, scenarioName);
    }
}
