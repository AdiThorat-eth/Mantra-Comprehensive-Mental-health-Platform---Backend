package com.mantra.therapy.services;

import com.mantra.auth.entities.User;
import com.mantra.shared.exceptions.ResourceNotFoundException;
import com.mantra.therapy.dto.BookingDTO;
import com.mantra.therapy.dto.TherapistDTO;
import com.mantra.therapy.entities.Booking;
import com.mantra.therapy.entities.Therapist;
import com.mantra.therapy.repositories.BookingRepository;
import com.mantra.therapy.repositories.TherapistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TherapyService {

    private final TherapistRepository therapistRepository;
    private final BookingRepository bookingRepository;

    public List<TherapistDTO> getAllTherapists() {
        return therapistRepository.findByAvailableTrue().stream()
                .map(this::convertTherapistToDTO)
                .collect(Collectors.toList());
    }

    public BookingDTO createBooking(BookingDTO bookingDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Therapist therapist = therapistRepository.findById(bookingDTO.getTherapistId())
                .orElseThrow(() -> new ResourceNotFoundException("Therapist not found with id: " + bookingDTO.getTherapistId()));

        Booking booking = new Booking();
        booking.setUser(currentUser);
        booking.setTherapist(therapist);
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setNotes(bookingDTO.getNotes());

        Booking savedBooking = bookingRepository.save(booking);
        return convertBookingToDTO(savedBooking);
    }

    public List<BookingDTO> getUserBookings() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return bookingRepository.findByUserId(currentUser.getId()).stream()
                .map(this::convertBookingToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertBookingToDTO)
                .collect(Collectors.toList());
    }

    private TherapistDTO convertTherapistToDTO(Therapist therapist) {
        return new TherapistDTO(
                therapist.getId(),
                therapist.getName(),
                therapist.getSpecialization(),
                therapist.getDescription(),
                therapist.getEmail(),
                therapist.getPhoneNumber(),
                therapist.getYearsOfExperience(),
                therapist.isAvailable(),
                therapist.getCreatedAt(),
                therapist.getUpdatedAt()
        );
    }

    private BookingDTO convertBookingToDTO(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getTherapist().getId(),
                booking.getTherapist().getName(),
                booking.getUser().getEmail(),
                booking.getBookingDate(),
                booking.getStatus(),
                booking.getNotes(),
                booking.getCreatedAt()
        );
    }
}