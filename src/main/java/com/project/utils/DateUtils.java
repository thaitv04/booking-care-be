package com.project.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtils {
    public static LocalDate[] getStartAndEndOfWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
        return new LocalDate[] { startOfWeek, endOfWeek };
    }
}
