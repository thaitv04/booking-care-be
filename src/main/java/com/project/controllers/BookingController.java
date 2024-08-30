package com.project.controllers;

import com.project.dto.BookingDTO;
import com.project.models.Booking;
import com.project.repositories.BookingRepository;
import com.project.requests.BookingSearchRequest;
import com.project.requests.BookingUpdateRequest;
import com.project.responses.BookingResponse;
import com.project.responses.CalendarResponse;
import com.project.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    @PostMapping("/search")
    public ResponseEntity<?> getAllBookings(@RequestParam(value = "status", required = false) String status,
                                            @RequestParam(value = "dateBookingFrom", required = false) String dateBookingFrom,
                                            @RequestParam(value = "dateBookingTo", required = false) String dateBookingTo) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean isAdmin = authorities.stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
            StringBuilder username = new StringBuilder();
            if(!isAdmin) {
                username.append(authentication.getName());
            }
            List<BookingResponse> result = bookingService.getAllBookings(username.toString(), status, dateBookingFrom, dateBookingTo);
            return ResponseEntity.ok().body(result);
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/calendars")
    public ResponseEntity<?> getCalendars() {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            List<CalendarResponse> result = bookingService.getCalendars(username);
            return ResponseEntity.ok().body(result);
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        try{
            bookingService.createBooking(bookingDTO);
            return ResponseEntity.ok().body("Success");
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/confirm/{id}")
    public ResponseEntity<?> confirmBooking(@PathVariable("id") Long id,
                                            @RequestParam("token") String token) {
        try{
            Optional<Booking> booking = bookingRepository.findById(id);
            if(!booking.isPresent() || booking.get().getToken() == null || !booking.get().getToken().equals(token)) {
                return ResponseEntity.badRequest().body("Invalid path");
            }
            BookingUpdateRequest bookingUpdateRequest = new BookingUpdateRequest(id, "confirm");
            bookingService.updateBooking(bookingUpdateRequest);
            return ResponseEntity.ok().body("Thành công");
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateBooking(@RequestBody BookingUpdateRequest bookingUpdateRequest) {
        try{
            bookingService.updateBooking(bookingUpdateRequest);
            return ResponseEntity.ok().body("Success");
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
