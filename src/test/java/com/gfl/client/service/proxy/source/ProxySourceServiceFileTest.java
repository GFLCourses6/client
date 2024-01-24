package com.gfl.client.service.proxy.source;

import com.gfl.client.exception.FileReadException;
import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.util.file.FileParser;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "proxy.source.type=file")
public class ProxySourceServiceFileTest {

    @Autowired
    private ProxySourceService proxySourceService;

    @MockBean
    private FileParser fileParser;

    @Test
    @Order(0)
    public void testFileProxySourceService() {
        assertInstanceOf(ProxySourceServiceFile.class, proxySourceService);
    }

    @Test
    void getAllProxyConfigs_Success() throws IOException {
        List<ProxyConfigHolder> expectedProxyConfigs = Collections.singletonList(new ProxyConfigHolder());
        Mockito.<List<ProxyConfigHolder>>when(fileParser.getAllFromFile(any(), any())).thenReturn(expectedProxyConfigs);

        List<ProxyConfigHolder> actualProxyConfigs = proxySourceService.getAllProxyConfigs();

        assertNotNull(actualProxyConfigs);
        assertEquals(expectedProxyConfigs, actualProxyConfigs);
    }

    @Test
    void getAllProxyConfigs_FileReadException() throws IOException {
        when(fileParser.getAllFromFile(any(), any())).thenThrow(new IOException("Test IOException"));

        assertThrows(FileReadException.class, () -> proxySourceService.getAllProxyConfigs());
    }
}
