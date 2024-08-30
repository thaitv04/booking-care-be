package com.project.converters;

import com.project.models.Time;
import com.project.responses.TimeResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeConverter {
    private final ModelMapper modelMapper;

    public TimeResponse fromTimeToResponse(Time time) {
        TimeResponse timeResponse = modelMapper.map(time, TimeResponse.class);
        timeResponse.setTime(time.getStart() + "h - " + time.getEnd() + "h");
        return timeResponse;
    }
}
