package com.gfl.client.service.proxy.source;

import com.gfl.client.mapper.ProxyMapper;
import com.gfl.client.model.ProxyApiResponse;
import com.gfl.client.model.ProxyConfigHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ProxySourceServiceUrl implements ProxySourceService {

    @Value("${proxy.webshare.api-key}")
    private String apiKey;
    @Value("${proxy.webshare.api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ProxyMapper proxyMapper;

    @Override
    public List<ProxyConfigHolder> getAllProxyConfigs() {
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ProxyApiResponse> responseEntity = restTemplate.exchange(
                apiUrl, HttpMethod.GET, entity, ProxyApiResponse.class);
        var proxies = responseEntity.getBody();

        if (responseEntity.getStatusCode().is2xxSuccessful()
                && proxies != null && proxies.getCount() > 0) {
            return proxyMapper.responseToProxyConfigHolder(proxies.getResults());
        }
        return List.of();
    }

    private HttpHeaders getHttpHeaders() {
        var headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, "Token " + apiKey);
        return headers;
    }
}