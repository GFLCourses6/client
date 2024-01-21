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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("/scenario")
@RequiredArgsConstructor
public class ScenarioSourceController {

    private final RestTemplateScenarioService restTemplateScenarioService;

    private final Logger logger = LoggerFactory.getLogger(ScenarioSourceController.class);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendScenariosRequest(
            @RequestBody @NotEmpty(message = "scenarioRequest list can not be empty")
            List<@Valid ScenarioRequest> scenarios){
        logger.info("sending scenarios, size = {}",  scenarios.size());
        restTemplateScenarioService.sendScenarios(scenarios);
    }

    @GetMapping(value = "/executed/{username}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ScenarioResult>> getExecutedScenarios(
            @NotBlank(message = "username can't be blank")
            @PathVariable("username") String username){
        return restTemplateScenarioService.getExecutedScenarios(username);
    }

    @GetMapping(value = "/queue/{username}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueueByUsername(
            @NotBlank(message = "username can't be blank")
            @PathVariable("username")  String username){
        return restTemplateScenarioService.getScenariosFromQueue(username);
    }

    @GetMapping(value = "/queue/{username}/{scenarioName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueueByUsernameAndScenarioName(
            @NotBlank(message = "username can't be blank")
            @PathVariable("username")String username,
            @NotBlank(message = "scenarioName can't be blank")
            @PathVariable("scenarioName")String scenarioName){
        return restTemplateScenarioService.getScenariosFromQueue(username, scenarioName);
    }
}
