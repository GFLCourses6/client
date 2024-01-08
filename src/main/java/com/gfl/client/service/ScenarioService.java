package com.gfl.client.service;

import com.gfl.client.model.ScenarioRequest;

import java.util.List;

public interface ScenarioService {

    int sendListHttp(List<ScenarioRequest> scenarios);
}
