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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "property.source.type=url")
class ProxySourceServiceUrlTest {

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private ProxyMapper proxyMapper;

    @Autowired
    private ProxySourceService proxySourceService;

    @Test
    void testGetAllProxyConfigsSuccessfulResponseReturnsProxyConfigHolders() {
        ProxyApiResponse proxyApiResponseMock = mock(ProxyApiResponse.class);

        List<ProxyInfoApiResponse> proxiesResponseExpected = List.of(new ProxyInfoApiResponse(), new ProxyInfoApiResponse());
        List<ProxyConfigHolder> expectedProxyFromConfigHolders = List.of(new ProxyConfigHolder(), new ProxyConfigHolder());
        int proxiesCount = 2;

        when(proxyApiResponseMock.getCount()).thenReturn(proxiesCount);
        when(proxyApiResponseMock.getResults()).thenReturn(proxiesResponseExpected);

        ResponseEntity<ProxyApiResponse> responseEntity = new ResponseEntity<>(proxyApiResponseMock, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ProxyApiResponse.class))).thenReturn(responseEntity);

        when(proxyMapper.responseToProxyConfigHolder(proxiesResponseExpected)).thenReturn(expectedProxyFromConfigHolders);

        List<ProxyConfigHolder> actualProxyConfigHolders = proxySourceService.getAllProxyConfigs();

        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ProxyApiResponse.class));
        verify(proxyMapper).responseToProxyConfigHolder(proxiesResponseExpected);
        assertEquals(expectedProxyFromConfigHolders, actualProxyConfigHolders);
    }

    @Test
    void testGetAllProxyConfigsEmptyResponseReturnsEmptyList() {
        ProxyApiResponse proxyApiResponseMock = mock(ProxyApiResponse.class);
        proxyApiResponseMock.setCount(0);
        proxyApiResponseMock.setResults(Collections.emptyList());
        ResponseEntity<ProxyApiResponse> responseEntity = new ResponseEntity<>(proxyApiResponseMock, HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ProxyApiResponse.class))).thenReturn(responseEntity);

        List<ProxyConfigHolder> actualProxyInConfigHolders = proxySourceService.getAllProxyConfigs();

        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ProxyApiResponse.class));
        verify(proxyMapper, never()).responseToProxyConfigHolder(anyList());
        assertTrue(responseEntity.getStatusCode().is4xxClientError());
        assertTrue(actualProxyInConfigHolders.isEmpty());
    }

    @Test
    void testGetAllProxyConfigsApiResponseIsSuccessfulButNoProxyConfigsReturnEmptyList() {
        ProxyApiResponse proxyApiResponse = new ProxyApiResponse(0, null, null, Collections.emptyList());
        ResponseEntity<ProxyApiResponse> responseEntity = new ResponseEntity<>(proxyApiResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(ProxyApiResponse.class))).thenReturn(responseEntity);

        List<ProxyConfigHolder> actualProxyConfigs = proxySourceService.getAllProxyConfigs();

        assertEquals(Collections.emptyList(), actualProxyConfigs);
    }

    @Test
    void testGetAllProxyConfigsApiResponseIsNotSuccessfulReturnEmptyList() {
        ResponseEntity<ProxyApiResponse> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(anyString(), any(), any(), eq(ProxyApiResponse.class))).thenReturn(responseEntity);

        List<ProxyConfigHolder> actualProxyConfigs = proxySourceService.getAllProxyConfigs();

        assertEquals(Collections.emptyList(), actualProxyConfigs);
    }
}