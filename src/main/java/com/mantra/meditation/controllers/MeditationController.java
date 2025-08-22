package com.mantra.meditation.controllers;

import com.mantra.meditation.dto.MeditationDTO;
import com.mantra.meditation.services.MeditationService;
import com.mantra.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meditations")
@RequiredArgsConstructor
@Tag(name = "Meditations", description = "Meditation content management")
public class MeditationController {

    private final MeditationService meditationService;

    @GetMapping
    @Operation(summary = "Get all meditations")
    public ResponseEntity<ApiResponse<List<MeditationDTO>>> getAllMeditations() {
        List<MeditationDTO> meditations = meditationService.getAllMeditations();
        return ResponseEntity.ok(ApiResponse.success("Meditations retrieved successfully", meditations));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get meditation by ID")
    public ResponseEntity<ApiResponse<MeditationDTO>> getMeditationById(@PathVariable Long id) {
        MeditationDTO meditation = meditationService.getMeditationById(id);
        return ResponseEntity.ok(ApiResponse.success("Meditation retrieved successfully", meditation));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new meditation (Admin only)")
    public ResponseEntity<ApiResponse<MeditationDTO>> createMeditation(@Valid @RequestBody MeditationDTO meditationDTO) {
        MeditationDTO createdMeditation = meditationService.createMeditation(meditationDTO);
        return ResponseEntity.ok(ApiResponse.success("Meditation created successfully", createdMeditation));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update meditation (Admin only)")
    public ResponseEntity<ApiResponse<MeditationDTO>> updateMeditation(
            @PathVariable Long id, 
            @Valid @RequestBody MeditationDTO meditationDTO) {
        MeditationDTO updatedMeditation = meditationService.updateMeditation(id, meditationDTO);
        return ResponseEntity.ok(ApiResponse.success("Meditation updated successfully", updatedMeditation));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete meditation (Admin only)")
    public ResponseEntity<ApiResponse<Object>> deleteMeditation(@PathVariable Long id) {
        meditationService.deleteMeditation(id);
        return ResponseEntity.ok(ApiResponse.success("Meditation deleted successfully", null));
    }
}