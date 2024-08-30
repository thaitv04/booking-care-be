package com.project.services.impl;

import com.project.constants.BookingStatus;
import com.project.converters.BookingConverter;
import com.project.dto.BookingDTO;
import com.project.exceptions.DataNotFoundException;
import com.project.exceptions.ResourceAlreadyExitsException;
import com.project.models.*;
import com.project.repositories.*;
import com.project.requests.BookingSearchRequest;
import com.project.requests.BookingUpdateRequest;
import com.project.responses.BookingResponse;
import com.project.responses.CalendarResponse;
import com.project.services.BookingService;
import com.project.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    private final DailyBookingRepository dailyBookingRepository;
    private final BookingConverter bookingConverter;
    private final TimeRepository timeRepository;
    private final MailServiceImpl mailService;

    @Override
    @Transactional
    public void createBooking(BookingDTO bookingDTO) throws ResourceAlreadyExitsException, MessagingException {
        Optional<User> optionalUser = userRepository.findById(bookingDTO.getDoctorId());
        if (!optionalUser.isPresent()) {
            throw new DataNotFoundException("Can not find doctor with id " + bookingDTO.getDoctorId());
        }

        Optional<Major> optionalMajor = majorRepository.findById(bookingDTO.getMajorId());
        if (!optionalMajor.isPresent()) {
            throw new DataNotFoundException("Can not find major with id " + bookingDTO.getMajorId());
        }

        //Kiểm tra bác sĩ có trùng lịch không
        LocalDate dateBooking = LocalDate.parse(bookingDTO.getDateBooking(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        DailyBooking dailyBooking = dailyBookingRepository.findByDoctorIdAndDateAndTimeId(bookingDTO.getDoctorId(), dateBooking, bookingDTO.getTimeBookingId());

        Booking booking = bookingConverter.fromBookingDTOToBooking(bookingDTO);
        if (dailyBooking == null) {
            //Lưu lịch khám
            booking.setStatus(BookingStatus.PENDING);
            bookingRepository.save(booking);

            dailyBooking = new DailyBooking();
            dailyBooking.setDoctorId(bookingDTO.getDoctorId());
            dailyBooking.setDate(dateBooking);
            dailyBooking.setTimeId(bookingDTO.getTimeBookingId());
            dailyBooking.setCount(1);
            dailyBookingRepository.save(dailyBooking);
        } else if (dailyBooking.getCount() >= 3) {
            throw new ResourceAlreadyExitsException("Lịch đặt bị trùng");
        } else {
            //Lưu lịch khám
            booking.setStatus(BookingStatus.PENDING);
            bookingRepository.save(booking);

            dailyBooking.setCount(dailyBooking.getCount() + 1);
            dailyBookingRepository.save(dailyBooking);
        }

        Optional<Time> optionalTime = timeRepository.findById(bookingDTO.getTimeBookingId());
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Thư xác nhận lịch khám</h1>\n")
                .append("<p>Tên người khám: ").append(bookingDTO.getFullname()).append("</p>\n")
                .append("<p>Ngày sinh: ").append(bookingDTO.getDateOfBirth()).append("</p>\n")
                .append("<p>Số điện thoại: ").append(bookingDTO.getPhoneNumber()).append("</p>\n")
                .append("<p>Ngày khám: ").append(bookingDTO.getDateBooking()).append("</p>\n")
                .append("<p>Thòi gian khám: ").append(optionalTime.get().getStart()).append("h - ")
                .append(optionalTime.get().getEnd()).append("h</p>\n")
                .append("<p>Tên bác sĩ khám: ").append(optionalUser.get().getName()).append("</p>\n")
                .append("<p>Chuyên ngành: ").append(optionalMajor.get().getName()).append("</p>\n")
                .append("<h4>Hãy nhấn xác nhận để lịch khám được hoàn thành đăng ký.</h4>\n")
                .append("<a href='http://localhost:8080/api/bookings/confirm/").append(booking.getId())
                .append("?token=").append(booking.getToken())
                .append("' style='display:inline-block;padding:10px 20px;background-color:#1677ff;color:white;text-align:center;text-decoration:none;border-radius:5px;font-size:16px;'>")
                .append("<h2 style='margin:0;'>Xác nhận đặt lịch khám</h2></a>");
        mailService.sendMail(bookingDTO.getGmail(), "Thư xác nhận lịch khám", sb.toString());
    }

    @Override
    @Transactional
    public void updateBooking(BookingUpdateRequest bookingUpdateRequest) throws MessagingException {
        Optional<Booking> booking = bookingRepository.findById(bookingUpdateRequest.getId());
        if (booking.isPresent()) {
            switch (bookingUpdateRequest.getStatus()) {
                case "accept":
                    booking.get().setStatus(BookingStatus.ACCEPTING);
                    StringBuilder sb = new StringBuilder();
                    sb.append("<h1>Thư xác nhận lịch khám được đặt thành công</h1>\n")
                            .append("<p>Tên người khám: ").append(booking.get().getFullname()).append("</p>\n")
                            .append("<p>Ngày sinh: ").append(booking.get().getDateOfBirth()).append("</p>\n")
                            .append("<p>Số điện thoại: ").append(booking.get().getPhoneNumber()).append("</p>\n")
                            .append("<p>Ngày khám: ").append(booking.get().getDateBooking()).append("</p>\n")
                            .append("<p>Thòi gian khám: ").append(booking.get().getTimeBookingEntities().getStart()).append("h - ")
                            .append(booking.get().getTimeBookingEntities().getEnd()).append("h</p>\n")
                            .append("<p>Tên bác sĩ khám: ").append(booking.get().getUserBookingEntities().getName()).append("</p>\n")
                            .append("<p>Triệu chứng: ").append(booking.get().getNote()).append("</p>\n")
                            .append("<h4>Mong quý bệnh nhân đến khám đúng giờ (tối đa muộn không quá 15 phút).</h4>\n");
                    mailService.sendMail(booking.get().getGmail(), "Thư xác nhận lịch khám được đặt thành công", sb.toString());
                    booking.get().setToken(null);
                    break;
                case "deny":
                    booking.get().setStatus(BookingStatus.DENYING);
                    //Xóa số đếm lượt khám
                    DailyBooking dailyBooking = dailyBookingRepository.findByDoctorIdAndDateAndTimeId(booking.get().getUserBookingEntities().getId(), booking.get().getDateBooking(), booking.get().getTimeBookingEntities().getId());
                    dailyBooking.setCount(dailyBooking.getCount() - 1);
                    dailyBookingRepository.save(dailyBooking);
                    //Gửi mail xin lỗi
                    StringBuilder emailContent = new StringBuilder();
                    emailContent.append("<h2>Kính gửi quý bệnh nhân ")
                            .append(booking.get().getFullname())
                            .append("</h2>")
                            .append("<p>Chúng tôi rất tiếc phải thông báo rằng lịch khám với bác sĩ mà quý bệnh nhân đã đặt vào ")
                            .append(booking.get().getDateBooking())
                            .append(" đã bị hủy do bác sĩ có việc bận đột xuất.</p>")
                            .append("<p>Chúng tôi chân thành xin lỗi về sự bất tiện này và mong được tiếp tục phục vụ quý bệnh nhân vào một thời gian khác phù hợp hơn.</p>")
                            .append("<p>Xin vui lòng liên hệ với chúng tôi để đặt lại lịch khám hoặc nhận thêm thông tin hỗ trợ. Chúng tôi rất mong được tiếp tục đồng hành cùng quý bệnh nhân.</p>")
                            .append("<p>Trân trọng cảm ơn và kính chào,</p>")
                            .append("<p>Phòng khám Tâm Anh</p>");
                    mailService.sendMail(booking.get().getGmail(), "Thư hủy lịch khám", emailContent.toString());
                    booking.get().setToken(null);
                    break;
                case "success":
                    booking.get().setStatus(BookingStatus.SUCCESS);
                    booking.get().setToken(null);
                    break;
                case "failure":
                    booking.get().setStatus(BookingStatus.FAILURE);
                    booking.get().setToken(null);
                    break;
                case "confirm":
                    booking.get().setStatus(BookingStatus.CONFIRMING);
                    booking.get().setToken(null);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + bookingUpdateRequest.getStatus());
            }
            bookingRepository.save(booking.get());
        }
    }

    @Override
    public List<BookingResponse> getAllBookings(String username, String status, String dateBookingFrom, String dateBookingTo) throws DataNotFoundException {
        return bookingRepository.searchBookings(username, status, dateBookingFrom, dateBookingTo).stream()
                .map(bookingConverter::fromBookingToBookingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalendarResponse> getCalendars(String username) throws DataNotFoundException {
        LocalDate[] weekRange = DateUtils.getStartAndEndOfWeek();
        LocalDate startOfWeek = weekRange[0];
        LocalDate endOfWeek = weekRange[1];

        List<Booking> bookings = bookingRepository.getCalendar(startOfWeek, endOfWeek, username);

        List<BookingResponse> bookingResponses = bookings.stream()
                .map(bookingConverter::fromBookingToBookingResponse)
                .collect(Collectors.toList());

        //Nhóm theo thứ trong tuần
        Map<DayOfWeek, List<BookingResponse>> groupedBookings = bookingResponses.stream()
                .collect(Collectors.groupingBy(bookingResponse ->
                        LocalDate.parse(bookingResponse.getDateBooking(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).getDayOfWeek()));

        List<CalendarResponse> calendarResponses = new ArrayList<>();

        // Sắp xếp theo thứ tự từ thứ Hai đến Chủ Nhật
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            List<BookingResponse> bookingsForDay = groupedBookings.getOrDefault(dayOfWeek, Collections.emptyList());
            CalendarResponse calendarResponse = new CalendarResponse();
            calendarResponse.setDay(dayOfWeek);
            calendarResponse.setBookings(bookingsForDay);
            calendarResponses.add(calendarResponse);
        }

        return calendarResponses;
    }
}
