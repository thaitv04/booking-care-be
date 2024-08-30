package com.project.services;

import com.project.requests.TimeRequest;
import com.project.responses.TimeResponse;

import java.util.List;

public interface TimeService {
    List<TimeResponse> getAllTimevalid(TimeRequest timeRequest);
}
