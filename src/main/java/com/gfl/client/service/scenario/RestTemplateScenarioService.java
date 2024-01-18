package com.gfl.client.service.scenario;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestTemplateScenarioService
        implements ScenarioService {

    @Value("${worker.base.uri}")
    private String baseUrl;
    private final RestTemplate restTemplate;

    public ResponseEntity<Void> sendScenarios(
            List<ScenarioRequest> scenarios) {
        String uri = "%s/api/scenario/queue".formatted(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForEntity(
                uri, new HttpEntity<>(scenarios, headers), Void.class);
    }

    @Override
    public List<ScenarioResult> getExecutedScenarios(String username) {
        String url = "%s/api/result/%s".formatted(baseUrl, username);
        ResponseEntity<List<ScenarioResult>> response =
                restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        return response.getBody();
    }

    @Override
    public ResponseEntity<List<ScenarioRequest>> getScenarioFromQueue(
            final String username, final String scenarioName) {
        String url = "%s/api/scenario/queue/%s/%s"
                .formatted(baseUrl, username, scenarioName);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
    }
}
