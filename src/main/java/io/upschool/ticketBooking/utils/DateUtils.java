package io.upschool.ticketBooking.utils;

import io.upschool.ticketBooking.exception.InvalidDateTimeFormatException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm";

    public static LocalDate parseLocalDate(String dateInput) {
        if (!isValidDateFormat(dateInput)) {
            throw new InvalidDateTimeFormatException("Invalid date format. Use yyyy-MM-dd");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return LocalDate.parse(dateInput, formatter);
    }

    public static LocalTime parseLocalTime(String timeInput) {
        if (!isValidTimeFormat(timeInput)) {
            throw new InvalidDateTimeFormatException("Invalid time format. Use HH:mm");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        return LocalTime.parse(timeInput, formatter);
    }
    public static LocalDateTime combineDateAndTime(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time);
    }

    private static boolean isValidDateFormat(String dateInput) {
        return dateInput.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private static boolean isValidTimeFormat(String timeInput) {
        return timeInput.matches("\\d{2}:\\d{2}");
    }
}
