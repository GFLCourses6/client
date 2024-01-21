package com.gfl.client.controller;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
import com.gfl.client.service.scenario.RestTemplateScenarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequiredArgsConstructor
@RequestMapping("/scenario")
@Api(value = "Operations pertaining to scenario sources")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid input provided"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Executed scenarios not found"),
        @ApiResponse(responseCode = "405", description = "Method not allowed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
public class ScenarioSourceController {

    private final RestTemplateScenarioService restTemplateScenarioService;

    private final Logger logger = LoggerFactory.getLogger(ScenarioSourceController.class);

    @ApiOperation(value = "Send scenarios request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scenarios sent successfully"),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendScenariosRequest(
            @ApiParam(value = "List of scenario requests", required = true)
            @RequestBody @NotEmpty(message = "scenarioRequest list can not be empty")
            List<@Valid ScenarioRequest> scenarios){
        logger.info("sending scenarios, size = {}",  scenarios.size());
        restTemplateScenarioService.sendScenarios(scenarios);
    }

    @ApiOperation(value = "Get executed scenarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scenarios retrieved successfully")
    })
    @GetMapping(value = "/executed/{username}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ScenarioResult>> getExecutedScenarios(
            @ApiParam(value = "Username for which executed scenarios need to be retrieved", required = true)
            @NotBlank(message = "username can't be blank")
            @PathVariable("username") String username){
        return restTemplateScenarioService.getExecutedScenarios(username);
    }

    @ApiOperation(value = "Get scenarios from queue by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Executed scenarios retrieved successfully")
    })
    @GetMapping(value = "/queue/{username}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueueByUsername(
            @ApiParam(value = "Username for which scenarios need to be retrieved from queue", required = true)
            @NotBlank(message = "username can't be blank")
            @PathVariable("username")  String username){
        return restTemplateScenarioService.getScenariosFromQueue(username);
    }

    @ApiOperation(value = "Get scenarios from queue by username and scenario name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scenarios retrieved successfully from queue")
    })
    @GetMapping(value = "/queue/{username}/{scenarioName}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueueByUsernameAndScenarioName(
            @ApiParam(value = "Username for which scenarios need to be retrieved from queue", required = true)
            @NotBlank(message = "username can't be blank")
            @PathVariable("username")String username,
            @ApiParam(value = "Scenario name for which scenarios need to be retrieved from queue", required = true)
            @NotBlank(message = "scenarioName can't be blank")
            @PathVariable("scenarioName")String scenarioName){
        return restTemplateScenarioService.getScenariosFromQueue(username, scenarioName);
    }
}
