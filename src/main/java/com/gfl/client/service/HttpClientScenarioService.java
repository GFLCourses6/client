package com.gfl.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfl.client.model.ScenarioRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.zip.DataFormatException;

@Service
public class HttpClientScenarioService
        implements ScenarioService {

    @Value("#{ '${worker.base.uri}' + '/api/scenarios' }")
    private String baseUrl;
    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    public HttpClientScenarioService(
            HttpClient httpClient,
            ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public int sendListHttp(
            final List<ScenarioRequest> scenarios) {
        try {
            String scenariosToJson = scenariosToJson(scenarios);
            return httpClient.send(getHttpRequest(scenariosToJson),
                                   HttpResponse.BodyHandlers.ofString())
                             .statusCode();
        } catch (DataFormatException e) {
            return 400;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted: " + e.getMessage());
            return 500;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 500;
        }
    }

    private HttpRequest getHttpRequest(
            final String scenarios) {
        return HttpRequest.newBuilder()
                          .uri(URI.create(baseUrl))
                          .header("Content-Type", "application/json")
                          .POST(HttpRequest.BodyPublishers.ofString(scenarios))
                          .build();
    }

    public String scenariosToJson(
            final List<ScenarioRequest> scenarios)
            throws DataFormatException {
        try {
            return objectMapper.writeValueAsString(scenarios);
        } catch (JsonProcessingException e) {
            throw new DataFormatException(e.getMessage());
        }
    }
}
