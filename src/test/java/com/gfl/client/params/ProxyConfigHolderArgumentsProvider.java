package com.gfl.client.params;

import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.model.ProxyCredentials;
import com.gfl.client.model.ProxyNetworkConfig;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Stream;

public class ProxyConfigHolderArgumentsProvider {

    static Stream<Arguments> testExecute(){
        Queue<ProxyConfigHolder> proxyConfigHolders = new LinkedBlockingDeque<>();

        proxyConfigHolders.add(new ProxyConfigHolder(
                new ProxyNetworkConfig(
                        "193.42.225.12",
                        6503
                ),
                new ProxyCredentials(
                        "ixfkiyxf",
                        "0v2ypvysubnt"
                ), 1L, true
        ));

        proxyConfigHolders.add(new ProxyConfigHolder(
                new ProxyNetworkConfig(
                        "119.42.39.191",
                        5819
                ),
                new ProxyCredentials(
                        "ixfkiyxf",
                        "0v2ypvysubnt"
                ), 1L, true
        ));

        proxyConfigHolders.add(new ProxyConfigHolder(
                new ProxyNetworkConfig(
                        "103.101.90.97",
                        6362
                ),
                new ProxyCredentials(
                        "ixfkiyxf",
                        "0v2ypvysubnt"
                ), 1L, true
        ));

        proxyConfigHolders.add(new ProxyConfigHolder(
                new ProxyNetworkConfig(
                        "104.238.10.147",
                        6093
                ),
                new ProxyCredentials(
                        "ixfkiyxf",
                        "0v2ypvysubnt"
                ), 1L, true
        ));


        return Stream.of(
                Arguments.of(proxyConfigHolders)
        );
    }
}
