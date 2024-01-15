package com.gfl.client.service.proxy.source;

import com.gfl.client.exception.FileReadException;
import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.util.file.FileParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProxySourceServiceFileTest {

    @Mock
    private FileParser fileParser;

    @Value("${proxy.filepath}")
    private String proxyFilePath;

    @InjectMocks
    private ProxySourceServiceFile proxySourceServiceFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllProxyConfigs_Success() throws IOException {
        List<ProxyConfigHolder> expectedProxyConfigs = Collections.singletonList(new ProxyConfigHolder());
        when(fileParser.getAllFromFile(any(), any())).thenReturn(Collections.singletonList(expectedProxyConfigs));

        List<ProxyConfigHolder> actualProxyConfigs = proxySourceServiceFile.getAllProxyConfigs();

        assertNotNull(actualProxyConfigs);
        assertEquals(Collections.singletonList(expectedProxyConfigs), actualProxyConfigs);
    }

    @Test
    void getAllProxyConfigs_FileReadException() throws IOException {
        when(fileParser.getAllFromFile(any(), any())).thenThrow(new IOException("Test IOException"));

        assertThrows(FileReadException.class, () -> proxySourceServiceFile.getAllProxyConfigs());
    }
}