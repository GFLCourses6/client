package com.gfl.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.service.scenario.RestTemplateScenarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "ivan")
class ScenarioSourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestTemplateScenarioService service;

    private final List<ScenarioRequest> empty = new ArrayList<>();

    @BeforeEach
    void setUp() {
        service = mock(RestTemplateScenarioService.class);
    }

    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void getScenariosFromQueueValidUsernameSuccess(List<ScenarioRequest> scenarios) throws Exception {
        String username = "ivan";

        when(service.getScenariosFromQueue(username))
                .thenReturn(new ResponseEntity<>(scenarios, HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders.get("/scenario/queue/{username}", username))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void sendScenarioRequestSuccess(List<ScenarioRequest> scenarios) throws Exception {
        mockMvc.perform(post("/scenario")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scenarios))
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
               .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void getScenariosFromQueueValidUsernameAndScenarioNameSuccess(
            List<ScenarioRequest> scenarios)
            throws Exception {
        String username = "ivan";
        String scenarioName = "Scenario click";
        when(service.getScenariosFromQueue(username, scenarioName))
                .thenReturn(new ResponseEntity<>(scenarios, HttpStatus.OK));

        mockMvc.perform(get("/scenario/queue/{username}/{scenarioName}",
                        username, scenarioName))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @ParameterizedTest
    @ArgumentsSource(ScenarioResultsArgumentsProvider.class)
    void sendScenariosRequestBadRequest(
            List<ScenarioRequest> scenarios) throws Exception {
        mockMvc.perform(post("/scenario")
                                .content(objectMapper.writeValueAsString(scenarios.get(0)))
                                .contentType(APPLICATION_JSON))
               .andExpect(status().is4xxClientError());
    }

    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void sendScenariosValidScenariosSuccess(List<ScenarioRequest> scenarios) throws Exception {
        String json = objectMapper.writeValueAsString(scenarios);
        mockMvc.perform(post("/scenario")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
               .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void sendScenariosWithScenariosAndEmptyStepReturnBadRequest(
            List<ScenarioRequest> scenarios) throws Exception {
        scenarios.forEach(scenario -> scenario.setSteps(new ArrayList<>()));
        String json = objectMapper.writeValueAsString(scenarios);
        MockHttpServletResponse response = postResponse(json);
        assertEquals(400, response.getStatus());
        assertEquals("Validation error(s): At least one step is required; At least one step is required;",
                     response.getContentAsString().trim());
    }

    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void sendScenariosEmptyScenarioListBadRequest(
            List<ScenarioRequest> scenarios) throws Exception {
        String json = objectMapper.writeValueAsString(scenarios);
        MockHttpServletResponse response = postResponse(json);
        assertEquals(200, response.getStatus());
        assertEquals("", response.getContentAsString());
    }

    @Test
    void sendInvalidScenariosWithEmptyListReturnBadRequest() throws Exception {
        String json = objectMapper.writeValueAsString(empty);
        MockHttpServletResponse response = postResponse(json);
        assertEquals(400, response.getStatus());
        assertEquals("Validation error(s): scenarioRequest list can not be empty;",
                     response.getContentAsString().trim());
    }

    @Test
    void sendEmptyScenariosInvalidScenarioRequest() throws Exception {
        empty.add(new ScenarioRequest());
        String json = objectMapper.writeValueAsString(empty);
        MockHttpServletResponse response = postResponse(json);
        assertEquals(400, response.getStatus());
    }

    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void sendScenariosWithScenariosAndEmptyActionsReturnBadRequest(
            List<ScenarioRequest> scenarios) throws Exception {
        scenarios.forEach(scenario -> scenario.getSteps().forEach(step -> step.setAction("")));
        String json = objectMapper.writeValueAsString(scenarios);
        MockHttpServletResponse response = postResponse(json);
        assertEquals(400, response.getStatus());
    }

    private MockHttpServletResponse postResponse(String request)
            throws Exception {
            return mockMvc.perform(post("/scenario")
                                           .content(request)
                                           .with(SecurityMockMvcRequestPostProcessors.jwt())
                                           .contentType(APPLICATION_JSON))
                          .andReturn().getResponse();
    }
}
