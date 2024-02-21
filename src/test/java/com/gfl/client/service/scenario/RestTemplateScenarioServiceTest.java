package com.gfl.client.service.scenario;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
import com.gfl.client.security.RsaManager;
import com.gfl.client.service.proxy.queue.DefaultAsyncProxyTaskProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@MockBean({DefaultAsyncProxyTaskProcessor.class})
class RestTemplateScenarioServiceTest {

    @Autowired
    private RestTemplateScenarioService restTemplateScenarioService;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private RsaManager rsaManager;

    @Test
    public void testSendScenariosWithGivenUsernameAndScenariosAndReturnResponseEntity() {
        String username = "testUsername";
        List<ScenarioRequest> scenarios = Collections.singletonList(new ScenarioRequest());
        ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Void.class))).thenReturn(expectedResponse);
        when(rsaManager.encrypt(anyString())).thenReturn("encryptedToken");

        ResponseEntity<Void> actualResponse = restTemplateScenarioService.sendScenarios(username, scenarios);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).postForEntity(anyString(), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    public void testGetExecutedScenariosWithGivenUsernameAndReturnResponseEntity() {
        String username = "testUsername";
        ResponseEntity<List<ScenarioResult>> expectedResponse = ResponseEntity.ok().build();
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(expectedResponse);
        when(rsaManager.encrypt(anyString())).thenReturn("encryptedToken");

        ResponseEntity<List<ScenarioResult>> actualResponse = restTemplateScenarioService.getExecutedScenarios(username);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class));
    }

    @Test
    public void testGetScenariosFromQueueWithGivenUsernameAndReturnResponseEntity() {
        String username = "testUsername";
        ResponseEntity<List<ScenarioRequest>> expectedResponse = ResponseEntity.ok().build();
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(expectedResponse);
        when(rsaManager.encrypt(anyString())).thenReturn("encryptedToken");

        ResponseEntity<List<ScenarioRequest>> actualResponse = restTemplateScenarioService.getScenariosFromQueue(username);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class));
    }

    @Test
    public void testGetScenariosFromQueueWhenGivenUsernameAndScenarioNameThenReturnResponseEntity() {
        String username = "testUsername";
        String scenarioName = "testScenario";
        ResponseEntity<List<ScenarioRequest>> expectedResponse = ResponseEntity.ok().build();
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(expectedResponse);
        when(rsaManager.encrypt(anyString())).thenReturn("encryptedToken");

        ResponseEntity<List<ScenarioRequest>> actualResponse = restTemplateScenarioService.getScenariosFromQueue(username, scenarioName);

        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class));
    }
}