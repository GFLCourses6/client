package com.gfl.client.model;

import com.gfl.client.validation.SingleUseField;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SingleUseField // only useTimes or useAlways in the request
public class ProxyConfigHolder {

    @Valid
    @NotNull(message = "proxy network configuration can't be null")
    private ProxyNetworkConfig proxyNetworkConfig;

    @Valid
    private ProxyCredentials proxyCredentials;

    @Min(value = 1, message = "useTimes can't be lower than 1")
    private Long useTimes;

    private boolean useAlways;

    public void countDownUseTimes() {
        useTimes--;
    }
}
