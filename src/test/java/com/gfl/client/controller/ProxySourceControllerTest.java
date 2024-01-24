package com.gfl.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.model.ProxyCredentials;
import com.gfl.client.model.ProxyNetworkConfig;
import com.gfl.client.service.proxy.queue.ProxySourceQueueHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProxySourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProxySourceQueueHandler proxySourceQueueHandler;

    @Test
    @WithMockUser(roles = "WORKER")
    void testAddProxy_ValidInput_Returns201() throws Exception {
        when(proxySourceQueueHandler.getProxy(any(String.class))).thenReturn(getSampleProxyConfig());

        mockMvc.perform(post("/proxy")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getSampleProxyConfig())))
            .andExpect(status().isCreated());
    }
    @Test
    @WithMockUser(roles = "WORKER")
    void testGetProxy_ValidUsername_Returns200() throws Exception {
        // Mock the behavior of your service
        String username = "testUser";
        when(proxySourceQueueHandler.getProxy(username)).thenReturn(getSampleProxyConfig());

        // Perform the GET request
        mockMvc.perform(get("/proxy/{username}", username)
                .with(csrf())  // Include CSRF token if your security configuration requires it
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }



    private ProxyConfigHolder getSampleProxyConfig() {
        ProxyNetworkConfig networkConfig = new ProxyNetworkConfig("193.42.225.12", 6503);
        ProxyCredentials credentials = new ProxyCredentials("ixfkiyxf", "0v2ypvysubnt");

        ProxyConfigHolder proxyConfigHolder = new ProxyConfigHolder();
        proxyConfigHolder.setProxyNetworkConfig(networkConfig);
        proxyConfigHolder.setProxyCredentials(credentials);
        proxyConfigHolder.setUseAlways(true);

        return proxyConfigHolder;
    }
}
