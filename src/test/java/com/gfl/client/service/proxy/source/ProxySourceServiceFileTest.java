package com.gfl.client.service.proxy.source;

import com.gfl.client.exception.FileReadException;
import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.util.file.FileParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "proxy.source.type=file")
public class ProxySourceServiceFileTest {

    @Value("${proxy.filepath}")
    private String proxyFilePath;

    @MockBean
    private FileParser fileParser;

    @Autowired
    private ProxySourceService proxySourceService;

    @Test
    public void testGetAllProxyConfigsFileParsingIsSuccessfulAndReturnListOfProxyConfigs() throws IOException {
        List<ProxyConfigHolder> expectedProxyConfigHolders = Arrays.asList(new ProxyConfigHolder(), new ProxyConfigHolder());
        when(fileParser.getAllFromFile(eq(proxyFilePath), eq(ProxyConfigHolder.class))).thenReturn(expectedProxyConfigHolders);

        List<ProxyConfigHolder> actualProxyConfigHolders = proxySourceService.getAllProxyConfigs();

        assertNotNull(actualProxyConfigHolders, "The returned list should not be null");
        assertEquals(expectedProxyConfigHolders, actualProxyConfigHolders, "The returned list should match the expected list");
    }

    @Test
    public void testGetAllProxyConfigsFileParsingFailsThrowFileReadException() throws IOException {
        when(fileParser.getAllFromFile(eq(proxyFilePath), eq(ProxyConfigHolder.class))).thenThrow(new IOException("File parsing failed"));

        assertThrows(FileReadException.class, () -> proxySourceService.getAllProxyConfigs(), "When file parsing fails, a FileReadException should be thrown");
    }

}
