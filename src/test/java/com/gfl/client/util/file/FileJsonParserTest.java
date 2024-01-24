package com.gfl.client.util.file;

import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.model.ProxyNetworkConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FileJsonParserTest {

    @Autowired
    private FileJsonParser fileJsonParser;

    @ParameterizedTest
    @MethodSource("com.gfl.client.params.ProxyConfigHolderArgumentsProvider#testExecute")
    void testGetAllFromFile(Queue<ProxyConfigHolder> expected) throws IOException {
        List<ProxyConfigHolder> actual = fileJsonParser.getAllFromFile(
                "json/ProxyConfigs.json", ProxyConfigHolder.class);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void testGetFromFile() throws IOException {
        ProxyNetworkConfig expected = new ProxyNetworkConfig("proxy1.example.com", 8080);
        ProxyNetworkConfig actual = fileJsonParser.getFromFile(
                "SingleProxyNetworkConfig.json", ProxyNetworkConfig.class);

        assertEquals(expected, actual);
    }
}