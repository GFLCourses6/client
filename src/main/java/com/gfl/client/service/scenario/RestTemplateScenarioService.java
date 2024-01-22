package com.gfl.client.service.scenario;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestTemplateScenarioService
        implements ScenarioService {

    @Value("${worker.base.uri}")
    private String baseUrl;
    @Value("${client.auth.token.value}")
    private String clientAuthToken;

    private final RestTemplate restTemplate;

    public ResponseEntity<Void> sendScenarios(String username,
                                              List<ScenarioRequest> scenarios) {
        scenarios.forEach(scenario -> setUser(scenario, username));
        String uri = "%s/api/scenario/queue".formatted(baseUrl);

        var headers = getWorkerCommonHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(scenarios, headers);

        return restTemplate.postForEntity(uri, requestEntity, Void.class);
    }

    @Override
    public ResponseEntity<List<ScenarioResult>> getExecutedScenarios(String username) {
        String url = "%s/api/result/%s".formatted(baseUrl, username);

        var headers = getWorkerCommonHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {});
    }

    @Override
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueue(String username) {
        String url = "%s/api/scenario/queue/%s".formatted(baseUrl, username);

        var headers = getWorkerCommonHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {});
    }

    @Override
    public ResponseEntity<List<ScenarioRequest>> getScenariosFromQueue(
            String username, String scenarioName) {
        String url = "%s/api/scenario/queue/%s/%s"
                .formatted(baseUrl, username, scenarioName);

        var headers = getWorkerCommonHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {});
    }

    private HttpHeaders getWorkerCommonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, "Token " + clientAuthToken);
        return headers;
    }

    private void setUser(ScenarioRequest scenarioRequest, String username) {
        scenarioRequest.setUsername(username);
    }
}
