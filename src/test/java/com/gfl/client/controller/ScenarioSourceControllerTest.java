package com.gfl.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.service.ScenarioService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
class ScenarioSourceControllerTest {

    private final MockMvc mockMvc;
    private final ScenarioService scenarioService = mock(ScenarioService.class);
    private final ScenarioSourceController controller = new ScenarioSourceController(scenarioService);

    public ScenarioSourceControllerTest() {
        this.mockMvc = standaloneSetup(controller).build();
    }

    @Test
    void createScenariosValidScenariosReturns200() {
        List<ScenarioRequest> validScenarios = Collections.singletonList(
                new ScenarioRequest("Test", "http://example.com", Collections.emptyList()));
        when(scenarioService.sendListHttp(validScenarios)).thenReturn(HttpStatus.OK.value());
        ResponseEntity<Integer> response = controller.createScenarios(validScenarios);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.OK.value(), response.getBody());
    }

    @Test
    void createScenariosInvalidScenariosReturns400() {
        List<ScenarioRequest> invalidScenarios = Collections.singletonList(new ScenarioRequest());
        when(scenarioService.sendListHttp(invalidScenarios)).thenReturn(HttpStatus.BAD_REQUEST.value());
        ResponseEntity<Integer> response = controller.createScenarios(invalidScenarios);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody());
    }

    @Test
    void createScenariosValidRequestReturns200() throws Exception {
        List<ScenarioRequest> validScenarioRequests = Collections.singletonList(
                new ScenarioRequest("Test", "http://example.com", Collections.emptyList()));
        when(scenarioService.sendListHttp(validScenarioRequests)).thenReturn(200);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/scenario")
                .content(asJsonString(validScenarioRequests))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").value(200));
        verify(scenarioService, times(1)).sendListHttp(validScenarioRequests);
    }


    @Test
    void createScenariosInvalidRequestReturns400() throws Exception {
        List<ScenarioRequest> invalidScenarioRequests = Collections.singletonList(new ScenarioRequest());
        when(scenarioService.sendListHttp(invalidScenarioRequests)).thenReturn(400);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/scenario")
                .content(asJsonString(invalidScenarioRequests))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").value(400));
        verify(scenarioService, times(1)).sendListHttp(invalidScenarioRequests);
    }

    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON string", e);
        }
    }
}