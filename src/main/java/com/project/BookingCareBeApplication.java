package com.project;

import com.project.services.impl.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.mail.MessagingException;

@SpringBootApplication
public class BookingCareBeApplication {
//    @Autowired
//    private MailServiceImpl mailService;

    public static void main(String[] args) {
        SpringApplication.run(BookingCareBeApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void sendMail() throws MessagingException {
//        mailService.sendMail("cntuanhung1805@gmail.com", "Thư xác nhận lịch khám", "Xác nhận đi con");
//    }

}
