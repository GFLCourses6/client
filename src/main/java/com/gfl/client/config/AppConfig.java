package com.gfl.client.config;

import com.gfl.client.mapper.ProxyMapper;
import com.gfl.client.service.proxy.source.ProxySourceService;
import com.gfl.client.service.proxy.source.ProxySourceServiceFile;
import com.gfl.client.service.proxy.source.ProxySourceServiceUrl;
import com.gfl.client.util.file.FileParser;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer{

    @Value("${proxy.validation.test-url}")
    private String testUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnProperty(name = "proxy.source.type", havingValue = "url")
    public ProxySourceService proxySourceServiceUrl(RestTemplate restTemplate, ProxyMapper proxyMapper) {
        return new ProxySourceServiceUrl(restTemplate, proxyMapper);
    }

    @Bean
    @ConditionalOnProperty(name = "proxy.source.type", havingValue = "file")
    public ProxySourceService proxySourceServiceFile(FileParser fileParser) {
        return new ProxySourceServiceFile(fileParser);
    }

    @Bean
    @Qualifier("proxyValidationRequest")
    public HttpUriRequest proxyValidationRequest() {
        return new HttpGet(testUrl);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
