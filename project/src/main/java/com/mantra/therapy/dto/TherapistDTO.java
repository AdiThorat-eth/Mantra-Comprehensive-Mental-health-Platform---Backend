package com.mantra.therapy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TherapistDTO {
    private Long id;
    private String name;
    private String specialization;
    private String description;
    private String email;
    private String phoneNumber;
    private Integer yearsOfExperience;
    private boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}