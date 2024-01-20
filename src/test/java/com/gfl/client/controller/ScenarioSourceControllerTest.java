package com.gfl.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc()
class ScenarioSourceControllerTest {

    @Autowired
    private Validator validator;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestTemplateScenarioService service;
    private ScenarioSourceController controller;

    private final List<ScenarioRequest> empty = new ArrayList<>();

    @BeforeEach
    void setUp() {
        service = mock(RestTemplateScenarioService.class);
        controller = new ScenarioSourceController(service);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setValidator(validator)
                .build();
    }

    @WithMockUser
    @ParameterizedTest
    @ArgumentsSource(ScenarioResultsArgumentsProvider.class)
    void getExecutedScenariosValidUsernameSuccess(List<ScenarioResult> scenarios)
            throws Exception {
        String username = "testUser";
        ResponseEntity<List<ScenarioResult>> response
                = new ResponseEntity<>(scenarios, HttpStatus.OK);
        when(service.getExecutedScenarios(username)).thenReturn(response);
        mockMvc.perform(get("/scenario/executed/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        verify(service, times(1)).getExecutedScenarios(username);
    }

    @WithMockUser
    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void getScenariosFromQueueValidUsernameSuccess(List<ScenarioRequest> scenarios)
            throws Exception {
        String username = "testUser";
        when(service.getScenariosFromQueue(username))
                .thenReturn(new ResponseEntity<>(scenarios, HttpStatus.OK));
        mockMvc.perform(get("/scenario/queue/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username", is(scenarios.get(0).getUsername())))
                .andExpect(jsonPath("$[0].name", is(scenarios.get(0).getName())))
                .andExpect(jsonPath("$[0].site", is(scenarios.get(0).getSite())))
                .andExpect(jsonPath("$[0].steps[0].action",
                                    is(scenarios.get(0).getSteps().get(0).getAction())))
                .andExpect(jsonPath("$[0].steps[0].value",
                                    is(scenarios.get(0).getSteps().get(0).getValue())));
        verify(service, times(1)).getScenariosFromQueue(username);
    }

    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void sendScenarioRequestSuccess(List<ScenarioRequest> scenarios) throws Exception {
        mockMvc.perform(post("/scenario")
                                 .contentType(APPLICATION_JSON)
                                 .content(objectMapper.writeValueAsString(scenarios)))
                .andExpect(status().isOk());
        verify(service, times(1)).sendScenarios(scenarios);
    }

    @WithMockUser
    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void getScenariosFromQueueValidUsernameAndScenarioNameSuccess(List<ScenarioRequest> scenarios)
            throws Exception {
        String username = "testUser";
        String scenarioName = "testScenario";
        when(service.getScenariosFromQueue(username, scenarioName)).thenReturn(
                new ResponseEntity<>(scenarios, HttpStatus.OK));

        mockMvc.perform(get("/scenario/queue/{username}/{scenarioName}", username, scenarioName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username", is(scenarios.get(0).getUsername())))
                .andExpect(jsonPath("$[0].name", is(scenarios.get(0).getName())))
                .andExpect(jsonPath("$[0].site", is(scenarios.get(0).getSite())))
                .andExpect(jsonPath("$[0].steps[0].action",
                                    is(scenarios.get(0).getSteps().get(0).getAction())))
                .andExpect(jsonPath("$[0].steps[0].value",
                                    is(scenarios.get(0).getSteps().get(0).getValue())));

        verify(service, times(1)).getScenariosFromQueue(username, scenarioName);
    }

    @WithMockUser
    @ParameterizedTest
    @ArgumentsSource(ScenarioResultsArgumentsProvider.class)
    void sendScenariosRequestBadRequest(List<ScenarioRequest> scenarios) throws Exception {
        mockMvc.perform(post("/scenario")
                                .content(objectMapper.writeValueAsString(scenarios.get(0)))
                                .contentType(APPLICATION_JSON))
               .andExpect(status().isBadRequest());
    }
    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void sendScenariosValidScenariosSuccess(List<ScenarioRequest> scenarios) throws Exception {
        String json = objectMapper.writeValueAsString(scenarios);
        when(service.sendScenarios(scenarios)).thenReturn(ResponseEntity.ok().build());
        controller.sendScenariosRequest(scenarios);
        mockMvc.perform(post("/scenario").contentType(APPLICATION_JSON).content(json))
               .andExpect(status().isOk());
        verify(service, times(2)).sendScenarios(scenarios);
    }

    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void sendScenariosWithScenariosAndEmptyStepReturnBadRequest(
            List<ScenarioRequest> scenarios) throws Exception {
        scenarios.forEach(scenario -> scenario.setSteps(new ArrayList<>()));
        String json = objectMapper.writeValueAsString(scenarios);
        MockHttpServletResponse response = postResponse(json);
        assertEquals(400, response.getStatus());
        assertEquals("", response.getContentAsString());
    }

    @ParameterizedTest
    @ArgumentsSource(ScenariosArgumentsProvider.class)
    void sendScenariosEmptyScenarioListBadRequest(
            List<ScenarioRequest> scenarios) throws Exception {
        String json = objectMapper.writeValueAsString(scenarios);
        MockHttpServletResponse response = postResponse(json);
        assertEquals(400, response.getStatus());
        assertEquals("", response.getContentAsString());
        //TODO scenarioRequest list can not be empty
    }

    @Test
    void sendInvalidScenariosWithEmptyList() throws Exception {
        String json = objectMapper.writeValueAsString(empty);
        MockHttpServletResponse response = postResponse(json);
        assertEquals(400, response.getStatus());
        assertEquals("", response.getContentAsString());
    }

    @Test
    void sendEmptyScenariosInvalidScenarioRequest() throws Exception {
        empty.add(new ScenarioRequest());
        String json = objectMapper.writeValueAsString(empty);
        MockHttpServletResponse response = postResponse(json);
        assertEquals(400, response.getStatus());
    }

    @Test
    void sendScenariosWithEmptyScenarioReturnBadRequest() throws Exception {
        String expectedResult = "";
        MockHttpServletResponse response = postResponse(expectedResult);
        assertEquals(400, response.getStatus());
        assertEquals(expectedResult, response.getContentAsString());
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
            String content = objectMapper.writeValueAsString(request);
            return mockMvc.perform(post("/scenario").content(content).contentType(APPLICATION_JSON))
                          .andReturn().getResponse();
    }
}
