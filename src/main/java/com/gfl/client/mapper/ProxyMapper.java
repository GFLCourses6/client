package com.gfl.client.mapper;

import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.model.ProxyInfoApiResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProxyMapper {

    @Mapping(source = "proxyAddress", target = "proxyNetworkConfig.hostname")
    @Mapping(source = "port", target = "proxyNetworkConfig.port")
    @Mapping(source = "username", target = "proxyCredentials.username")
    @Mapping(source = "password", target = "proxyCredentials.password")
    ProxyConfigHolder responseToProxyConfigHolder(ProxyInfoApiResponse response);

    List<ProxyConfigHolder> responseToProxyConfigHolder(List<ProxyInfoApiResponse> response);
}
