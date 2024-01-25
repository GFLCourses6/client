package com.gfl.client.service.scenario;

import com.gfl.client.model.ScenarioRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class RestTemplateScenarioServiceTest {

    @Autowired
    private RestTemplateScenarioService scenarioService;

    @MockBean
    private RestTemplate restTemplate;

    private String username = "ivan";

    @Test
    void sendScenarios() {
        List<ScenarioRequest> scenarios = Collections.singletonList(new ScenarioRequest());
        scenarioService.sendScenarios(username, scenarios);

        assertEquals(username, scenarios.get(0).getUsername());
        verify(restTemplate).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void getExecutedScenarios() {
        scenarioService.getExecutedScenarios(username);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class));
    }

    @Test
    void getScenariosFromQueueByUsername() {
        scenarioService.getScenariosFromQueue(username);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class));
    }

    @Test
    void getScenariosFromQueueByUsernameAndScenario() {
        scenarioService.getScenariosFromQueue(username, "scenario");
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class));
    }
}
