package com.levi.entity;

import com.levi.enums.BookingStatus;

import java.time.LocalDate;

/**
 * 订单实体类
 */
public record Booking(String bookingNumber, LocalDate date, String from, String to, BookingStatus bookingStatus){}