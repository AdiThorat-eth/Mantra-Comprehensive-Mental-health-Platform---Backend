package com.mantra.contact.controllers;

import com.mantra.contact.dto.ContactMessageDTO;
import com.mantra.contact.services.ContactService;
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
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@Tag(name = "Contact", description = "Contact message management")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @Operation(summary = "Send contact message")
    public ResponseEntity<ApiResponse<ContactMessageDTO>> sendMessage(@Valid @RequestBody ContactMessageDTO messageDTO) {
        ContactMessageDTO sentMessage = contactService.sendMessage(messageDTO);
        return ResponseEntity.ok(ApiResponse.success("Message sent successfully", sentMessage));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all contact messages (Admin only)")
    public ResponseEntity<ApiResponse<List<ContactMessageDTO>>> getAllMessages() {
        List<ContactMessageDTO> messages = contactService.getAllMessages();
        return ResponseEntity.ok(ApiResponse.success("Messages retrieved successfully", messages));
    }
}