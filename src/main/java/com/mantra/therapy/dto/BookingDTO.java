package com.mantra.therapy.dto;

import com.mantra.shared.enums.BookingStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;

    @NotNull(message = "Therapist ID is required")
    private Long therapistId;

    private String therapistName;
    private String userEmail;

    @NotNull(message = "Booking date is required")
    @Future(message = "Booking date must be in the future")
    private LocalDateTime bookingDate;

    private BookingStatus status;
    private String notes;
    private LocalDateTime createdAt;
}