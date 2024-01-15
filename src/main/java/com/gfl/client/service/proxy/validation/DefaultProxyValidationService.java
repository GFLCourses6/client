package com.gfl.client.service.proxy.validation;

import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.model.ProxyCredentials;
import com.gfl.client.model.ProxyNetworkConfig;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DefaultProxyValidationService implements ProxyValidationService {

  @Value("${proxy.validation.test-url}")
  private String testUrl;

  @Override
  public boolean isValidProxy(ProxyConfigHolder proxyConfig) {
    try (CloseableHttpClient httpClient = buildHttpClient(proxyConfig)) {
      HttpUriRequest request = new HttpGet(testUrl);
      CloseableHttpResponse response = httpClient.execute(request);

      return response.getStatusLine().getStatusCode() == 200;
    } catch (IOException e) {

      return false;
    }
  }

  private CloseableHttpClient buildHttpClient(ProxyConfigHolder proxyConfig) {
    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    ProxyNetworkConfig networkConfig = proxyConfig.getProxyNetworkConfig();
    ProxyCredentials credentials = proxyConfig.getProxyCredentials();

    if (credentials != null) {
      AuthScope authScope = new AuthScope(networkConfig.getHostname(), networkConfig.getPort());
      credentialsProvider.setCredentials(authScope, new org.apache.http.auth.UsernamePasswordCredentials(
          credentials.getUsername(), credentials.getPassword()
      ));
    }

    HttpHost proxy = new HttpHost(networkConfig.getHostname(), networkConfig.getPort());

    return HttpClients.custom()
        .setProxy(proxy)
        .setDefaultCredentialsProvider(credentialsProvider)
        .build();
  }
}
