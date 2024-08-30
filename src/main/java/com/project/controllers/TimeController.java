package com.project.controllers;

import com.project.requests.TimeRequest;
import com.project.responses.TimeResponse;
import com.project.services.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/times")
public class TimeController {
    private final TimeService timeService;

    @PostMapping("/search")
    public ResponseEntity<?> getAllDailyBookingFull(@Valid @RequestBody TimeRequest timeRequest) {
        try {
            List<TimeResponse> result = timeService.getAllTimevalid(timeRequest);
            return ResponseEntity.ok().body(result);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}