package com.project.constants;

import lombok.Getter;

@Getter
public enum BookingStatus {
    PENDING("Chờ xác nhận"),
    CONFIRMING("Chờ xử lý"),
    ACCEPTING("Đã xử lý"),
    DENYING("Đã từ chối"),
    SUCCESS("Thành công"),
    FAILURE("Không tới khám");

    private final String bookingStatusName;

    BookingStatus(String bookingStatusName) {
        this.bookingStatusName = bookingStatusName;
    }
}
