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

    @Value("#{ '${worker.base.uri}' + '/api/scenarios' }")
    private String baseUrl;
    private final RestTemplate restTemplate;
    private final ScenarioQueueHolder holder;

    public ResponseEntity<List<ScenarioResult>> sendScenarios() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<ScenarioRequest> scenarios = holder.takeScenarioFromQueue();
        HttpEntity<List<ScenarioRequest>> requestEntity = new HttpEntity<>(scenarios, headers);
        ParameterizedTypeReference<List<ScenarioResult>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<ScenarioResult>> responseEntity = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, responseType);
        holder.addAllExecutedScenarios(responseEntity.getBody());
        return responseEntity;
    }
}
