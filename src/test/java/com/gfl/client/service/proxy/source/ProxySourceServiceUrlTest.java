package com.gfl.client.service.proxy.source;

import com.gfl.client.mapper.ProxyMapper;
import com.gfl.client.model.ProxyApiResponse;
import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.model.ProxyInfoApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "proxy.source.type=url")
class ProxySourceServiceUrlTest {

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private ProxyMapper proxyMapper;

    @Autowired
    private ProxySourceService proxySourceService;

    @Test
    void testFileProxySourceService() {
        assertInstanceOf(ProxySourceServiceUrl.class, proxySourceService);
    }

    @Test
    void testGetAllProxyConfigsSuccess() {
        ProxyApiResponse mockApiResponse = mock(ProxyApiResponse.class);
        var expectedProxiesResponse = List.of(new ProxyInfoApiResponse(), new ProxyInfoApiResponse());
        var expectedProxies = List.of(new ProxyConfigHolder(), new ProxyConfigHolder());
        int proxiesCount = 2;

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET),
                any(HttpEntity.class), eq(ProxyApiResponse.class)))
                .thenReturn(ResponseEntity.ok(mockApiResponse));

        when(mockApiResponse.getCount()).thenReturn(proxiesCount);
        when(mockApiResponse.getResults()).thenReturn(expectedProxiesResponse);
        when(proxyMapper.responseToProxyConfigHolder(expectedProxiesResponse))
                .thenReturn(expectedProxies);

        List<ProxyConfigHolder> actualProxies = proxySourceService.getAllProxyConfigs();

        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET),
                any(HttpEntity.class), eq(ProxyApiResponse.class));
        verify(mockApiResponse).getCount();
        verify(mockApiResponse).getResults();
        verify(proxyMapper).responseToProxyConfigHolder(expectedProxiesResponse);
        assertEquals(2, actualProxies.size());
        assertEquals(expectedProxies, actualProxies);
    }

    @Test
    void testGetAllProxyConfigsFail() {
        ResponseEntity<ProxyApiResponse> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET),
                any(HttpEntity.class), eq(ProxyApiResponse.class)))
                .thenReturn(response);

        List<ProxyConfigHolder> actualProxies = proxySourceService.getAllProxyConfigs();

        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET),
                any(HttpEntity.class), eq(ProxyApiResponse.class));
        assertTrue(response.getStatusCode().is4xxClientError());
        verify(proxyMapper, never()).responseToProxyConfigHolder(any(List.class));
        assertTrue(actualProxies.isEmpty());
    }
}

