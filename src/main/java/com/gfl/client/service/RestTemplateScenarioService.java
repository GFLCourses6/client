package com.gfl.client.service;

import com.gfl.client.model.ScenarioRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RestTemplateScenarioService
        implements ScenarioService {

    @Value("#{ '${worker.base.uri}' + '/api/scenarios' }")
    private String baseUrl;
    private final RestTemplate restTemplate;

    public RestTemplateScenarioService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Void> sendScenarios(List<ScenarioRequest> scenarios) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<ScenarioRequest>> requestEntity = new HttpEntity<>(scenarios, headers);
        return restTemplate.postForEntity(baseUrl, requestEntity, Void.class);
    }
}
