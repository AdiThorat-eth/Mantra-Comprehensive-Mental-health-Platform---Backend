package com.mantra.therapy.controllers;

import com.mantra.shared.dto.ApiResponse;
import com.mantra.therapy.dto.BookingDTO;
import com.mantra.therapy.dto.TherapistDTO;
import com.mantra.therapy.services.TherapyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Therapy", description = "Therapy booking and therapist management")
public class TherapyController {

    private final TherapyService therapyService;

    @GetMapping("/therapists")
    @Operation(summary = "Get all available therapists")
    public ResponseEntity<ApiResponse<List<TherapistDTO>>> getAllTherapists() {
        List<TherapistDTO> therapists = therapyService.getAllTherapists();
        return ResponseEntity.ok(ApiResponse.success("Therapists retrieved successfully", therapists));
    }

    @PostMapping("/bookings")
    @Operation(summary = "Book a therapy session")
    public ResponseEntity<ApiResponse<BookingDTO>> createBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        BookingDTO createdBooking = therapyService.createBooking(bookingDTO);
        return ResponseEntity.ok(ApiResponse.success("Booking created successfully", createdBooking));
    }

    @GetMapping("/bookings/me")
    @Operation(summary = "Get current user's bookings")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getUserBookings() {
        List<BookingDTO> bookings = therapyService.getUserBookings();
        return ResponseEntity.ok(ApiResponse.success("User bookings retrieved successfully", bookings));
    }

    @GetMapping("/bookings/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all bookings (Admin only)")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getAllBookings() {
        List<BookingDTO> bookings = therapyService.getAllBookings();
        return ResponseEntity.ok(ApiResponse.success("All bookings retrieved successfully", bookings));
    }
}