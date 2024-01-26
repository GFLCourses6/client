package com.gfl.client.service.proxy.validation;

import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.model.ProxyCredentials;
import com.gfl.client.model.ProxyNetworkConfig;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultProxyValidationService implements ProxyValidationService {

    @Value("${proxy.validation.timeout}")
    private int timeout;
    private final HttpUriRequest request;

    private final Logger logger = LoggerFactory.getLogger(DefaultProxyValidationService.class);

    public DefaultProxyValidationService(@Qualifier("proxyValidationRequest") HttpUriRequest request) {
        this.request = request;
    }

    @Override
    public boolean isInvalidProxy(ProxyConfigHolder proxyConfig) {
        return !isValidProxy(proxyConfig);
    }

    @Override
    public boolean isValidProxy(ProxyConfigHolder proxyConfig) {
        try (CloseableHttpClient httpClient = buildHttpClient(proxyConfig)) {
            CloseableHttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode == 200 || statusCode == 429; // ok or too many requests
        } catch (Exception e) {
            logger.error("Invalid proxy detected: {}", proxyConfig);
            return false;
        }
    }

    private CloseableHttpClient buildHttpClient(ProxyConfigHolder proxyConfig) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        ProxyNetworkConfig networkConfig = proxyConfig.getProxyNetworkConfig();
        ProxyCredentials credentials = proxyConfig.getProxyCredentials();

        // configure proxy authentication
        if (credentials != null) {
            AuthScope authScope = new AuthScope(networkConfig.getHostname(), networkConfig.getPort());
            credentialsProvider.setCredentials(authScope, new org.apache.http.auth.UsernamePasswordCredentials(
                credentials.getUsername(), credentials.getPassword()
            ));
        }

        // build proxy
        HttpHost proxy = new HttpHost(networkConfig.getHostname(), networkConfig.getPort());

        // set timeouts
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(timeout)
                .build();

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build();

        return HttpClients.custom()
                .setProxy(proxy)
                .setDefaultSocketConfig(socketConfig)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultRequestConfig(config)
                .build();
    }
}
