package com.project.repositories;

import com.project.models.Booking;
import com.project.requests.BookingSearchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE " +
            "(:username IS NULL OR b.userBookingEntities.username = :username) AND " +
            "(:status IS NULL OR b.status = :status) AND " +
            "(:dateBookingFrom IS NULL OR b.dateBooking >= :dateBookingFrom) AND " +
            "(:dateBookingTo IS NULL OR b.dateBooking <= :dateBookingTo)")
    List<Booking> searchBookings(@Param("username") String username,
                                 @Param("status") String status,
                                 @Param("dateBookingFrom") String dateBookingFrom,
                                 @Param("dateBookingTo") String dateBookingTo);

    @Query("SELECT b FROM Booking b WHERE " +
            "(:username IS NULL OR b.userBookingEntities.username = :username) AND " +
            "(:startOfWeek IS NULL OR b.dateBooking >= :startOfWeek) AND " +
            "(:endOfWeek IS NULL OR b.dateBooking <= :endOfWeek)")
    List<Booking> getCalendar(@Param("startOfWeek") LocalDate startOfWeek,
                              @Param("endOfWeek") LocalDate endOfWeek,
                              @Param("username") String username);


}