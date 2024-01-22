package com.gfl.client.service.scenario;

import com.gfl.client.model.ScenarioRequest;
import com.gfl.client.model.ScenarioResult;
import com.gfl.client.security.RsaManager;
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
    @Value("${client.auth.token.value}")
    private String clientAuthToken;

    private final RestTemplate restTemplate;
    private RsaManager rsaManager;

    public ResponseEntity<Void> sendScenarios(
            List<ScenarioRequest> scenarios) {
        String uri = "%s/api/scenario/queue".formatted(baseUrl);

        var headers = getWorkerCommonHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

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
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
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
        rsaManager.initFromStrings();
        String encryptedClientAuthToken = rsaManager.encrypt(clientAuthToken);
        headers.add(HttpHeaders.AUTHORIZATION, "Token " + encryptedClientAuthToken);
        return headers;
    }
}
