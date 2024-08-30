package com.project.converters;

import com.project.dto.BookingDTO;
import com.project.models.Booking;
import com.project.models.Major;
import com.project.models.Time;
import com.project.models.User;
import com.project.repositories.MajorRepository;
import com.project.repositories.TimeRepository;
import com.project.repositories.UserRepository;
import com.project.responses.BookingResponse;
import com.project.responses.MajorResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingConverter {
    private final ModelMapper modelMapper;
    private final TimeRepository timeRepository;
    private final UserRepository userRepository;
    private final MajorRepository majorRepository;

    /**
     * Phương thức này để chuyển đổi từ bookingDTO sang BookingEntity
     * @param bookingDTO - dưới dạng Entity
     * @return dưới dạng Response
     */
    public Booking fromBookingDTOToBooking(BookingDTO bookingDTO) {
        modelMapper.typeMap(BookingDTO.class, Booking.class)
                .addMappings(mapper -> mapper.skip(Booking::setId));
        Booking booking = modelMapper.map(bookingDTO, Booking.class);
        Optional<User> user = userRepository.findById(bookingDTO.getDoctorId());
        booking.setUserBookingEntities(user.get());
        Optional<Time> time = timeRepository.findById(bookingDTO.getTimeBookingId());
        booking.setTimeBookingEntities(time.get());
        LocalDate dateBooking = LocalDate.parse(bookingDTO.getDateBooking(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        booking.setDateBooking(dateBooking);
        LocalDate dateOfBirth = LocalDate.parse(bookingDTO.getDateOfBirth(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        booking.setDateOfBirth(dateOfBirth);
        booking.setToken(UUID.randomUUID().toString());
        return booking;
    }

    /**
     * Phương thức này để chuyển đổi từ Booking sang BookingResponse
     * @param booking - dưới dạng Entity
     * @return dưới dạng Response
     */
    public BookingResponse fromBookingToBookingResponse(Booking booking) {
        modelMapper.typeMap(Booking.class, BookingResponse.class)
                .addMappings(mapper -> {
                    mapper.skip(BookingResponse::setTimeBooking);
                    mapper.skip(BookingResponse::setStatus);
                });
        BookingResponse bookingResponse = modelMapper.map(booking, BookingResponse.class);
        bookingResponse.setStatus(booking.getStatus().getBookingStatusName());
        bookingResponse.setDoctor(booking.getUserBookingEntities().getName());
        bookingResponse.setMajor(majorRepository.findById(booking.getMajorId()).get().getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = booking.getDateBooking().format(formatter);
        bookingResponse.setDateBooking(formattedDate);
        bookingResponse.setTimeBooking(booking.getTimeBookingEntities().getStart() + "h - " + booking.getTimeBookingEntities().getEnd() + "h");
        return bookingResponse;
    }
}
