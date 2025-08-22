package com.mantra.admin.controllers;

import com.mantra.admin.dto.AdminStatsDTO;
import com.mantra.admin.services.AdminService;
import com.mantra.auth.dto.UserDTO;
import com.mantra.auth.services.AuthService;
import com.mantra.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Admin dashboard and management endpoints")
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;

    @GetMapping("/stats")
    @Operation(summary = "Get system statistics")
    public ResponseEntity<ApiResponse<AdminStatsDTO>> getSystemStats() {
        AdminStatsDTO stats = adminService.getSystemStats();
        return ResponseEntity.ok(ApiResponse.success("System statistics retrieved successfully", stats));
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = authService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }
}