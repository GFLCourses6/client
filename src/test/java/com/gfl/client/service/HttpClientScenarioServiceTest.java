package com.gfl.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfl.client.model.ScenarioRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HttpClientScenarioServiceTest {


    @Mock
    HttpClient httpClient;
    @Mock
    ObjectMapper objectMapper;
    HttpResponse<String> httpResponse;

    static AutoCloseable autoCloseable;


    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    static void tearsDrop() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSendListHttpDataFormatException()
            throws Exception {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        HttpClientScenarioService service =
                new HttpClientScenarioService(httpClient, objectMapper);
        ScenarioRequest scenarioRequest = new ScenarioRequest();
        List<ScenarioRequest> scenarios = List.of(scenarioRequest);
        when(objectMapper.writeValueAsString(scenarios)).thenThrow(
                JsonProcessingException.class);
        int result = service.sendListHttp(scenarios);
        assertEquals(400, result);
    }

//    @Test
    void testSendListHttpSuccess() throws IOException, InterruptedException {
        List<ScenarioRequest> scenarioRequests = Collections.singletonList(new ScenarioRequest());
        HttpClientScenarioService service = new HttpClientScenarioService(httpClient, objectMapper);
        when(objectMapper.writeValueAsString(scenarioRequests)).thenReturn("{}");
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpClient.send(any(HttpRequest.class), any())).thenAnswer(invocation -> httpResponse);
        int statusCode = service.sendListHttp(scenarioRequests);
        assertEquals(200, statusCode);
    }
}
