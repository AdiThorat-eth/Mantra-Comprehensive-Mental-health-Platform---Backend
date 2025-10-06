package com.mantra.chatbot.controllers;

import com.mantra.chatbot.dto.ChatRequest;
import com.mantra.chatbot.dto.ChatResponse;
import com.mantra.chatbot.services.ChatbotService;
import com.mantra.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Tag(name = "Chatbot", description = "AI-powered mental health support chatbot")
@CrossOrigin(origins = "*") // allow frontend requests
public class ChatbotController {

    private final ChatbotService chatbotService;
    
    @Value("${openrouter.api.key:}")
    private String routerApiKey;

    @PostMapping("/message")
    @Operation(summary = "Send message to chatbot")
    public ResponseEntity<ApiResponse<ChatResponse>> sendMessage(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = chatbotService.processMessage(request);
        return ResponseEntity.ok(ApiResponse.success("Message processed successfully", response));
    }
    
    @GetMapping("/test")
    @Operation(summary = "Test OpenRouter API configuration")
    public ResponseEntity<ApiResponse<String>> testApiConfig() {
        String apiKeyStatus = routerApiKey != null && !routerApiKey.isEmpty() ? 
            "API Key configured (first 10 chars: " + routerApiKey.substring(0, 10) + "...)" : 
            "API Key not configured";
        return ResponseEntity.ok(ApiResponse.success("API Configuration Test", apiKeyStatus));
    }
}
