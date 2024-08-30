package com.project.requests;

import com.project.constants.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingSearchRequest {
    private String username;
    private BookingStatus status;
    private LocalDate dateBookingFrom;
    private LocalDate dateBookingTo;
}
