package com.project.services.impl;

import com.project.converters.TimeConverter;
import com.project.models.DailyBooking;
import com.project.models.Time;
import com.project.repositories.DailyBookingRepository;
import com.project.repositories.TimeRepository;
import com.project.requests.TimeRequest;
import com.project.responses.TimeResponse;
import com.project.services.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeServiceImpl implements TimeService {
    private final TimeRepository timeRepository;
    private final DailyBookingRepository dailyBookingRepository;
    private final TimeConverter timeConverter;

    @Override
    public List<TimeResponse> getAllTimevalid(TimeRequest timeRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(timeRequest.getDate(), formatter);
        List<DailyBooking> dailyBookings = dailyBookingRepository.findAllByCountAndDoctorIdAndDate(3, timeRequest.getDoctorId(), date);
        List<Time> times = new ArrayList<>();
        if(dailyBookings.isEmpty()){
            times = timeRepository.findAll();
        } else {
            List<Long> ids = new ArrayList<>();
            for (DailyBooking dailyBooking : dailyBookings) {
                ids.add(dailyBooking.getTimeId());
            }
            times = timeRepository.findAllByIdNotIn(ids);
        }
        List<TimeResponse> timeResponses = new ArrayList<>();
        for (Time item : times) {
            TimeResponse timeResponse = timeConverter.fromTimeToResponse(item);
            timeResponses.add(timeResponse);
        }
        return timeResponses;
    }
}
