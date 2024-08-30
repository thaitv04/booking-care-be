package com.project.services;

import com.project.dto.BookingDTO;
import com.project.exceptions.DataNotFoundException;
import com.project.exceptions.ResourceAlreadyExitsException;
import com.project.requests.BookingSearchRequest;
import com.project.requests.BookingUpdateRequest;
import com.project.responses.BookingResponse;
import com.project.responses.CalendarResponse;

import javax.mail.MessagingException;
import java.util.List;

public interface BookingService {
    void createBooking(BookingDTO bookingDTO) throws ResourceAlreadyExitsException, MessagingException;
    void updateBooking(BookingUpdateRequest bookingUpdateRequest) throws MessagingException;
    List<BookingResponse> getAllBookings(String username, String status, String dateBookingFrom, String dateBookingTo) throws DataNotFoundException;
    List<CalendarResponse> getCalendars(String username) throws DataNotFoundException;
}
