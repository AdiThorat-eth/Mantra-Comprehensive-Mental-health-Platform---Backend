package com.mantra.chatbot.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mantra.auth.entities.User;
import com.mantra.chatbot.dto.ChatRequest;
import com.mantra.chatbot.dto.ChatResponse;
import com.mantra.chatbot.entities.ChatMessage;
import com.mantra.chatbot.repositories.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private final ChatMessageRepository chatMessageRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${openrouter.api.key:}")
    private String routerApiKey;

    @Value("${openrouter.api.url}")
    private String routerApiUrl;

    private final Map<String, String> ruleBased = Map.of(
            "stressed", "It's okay to feel stressed. Try deep breathing for 5 minutes. Inhale for 4 counts, hold for 4, and exhale for 4.",
            "sleep", "Avoid screens before bedtime and try a relaxation meditation. Consider establishing a bedtime routine.",
            "anxiety", "When feeling anxious, ground yourself by naming 5 things you can see, 4 you can touch, 3 you can hear, 2 you can smell, and 1 you can taste.",
            "depressed", "Remember that seeking help is a sign of strength. Consider talking to a therapist or counselor. You're not alone.",
            "meditation", "Meditation can help reduce stress and improve mental clarity. Start with just 5 minutes a day.",
            "help", "I'm here to support you. You can talk to me about stress, anxiety, sleep issues, or book a session with one of our therapists."
    );

    public ChatResponse processMessage(ChatRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String botResponse = getRuleBased(request.getMessage().toLowerCase());
        String responseType = "RULE_BASED";

        if (botResponse == null) {
            botResponse = getAIResponse(request.getMessage());
            responseType = "AI_GENERATED";
        }

        // Save conversation history
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUser(currentUser);
        chatMessage.setUserMessage(request.getMessage());
        chatMessage.setBotResponse(botResponse);
        chatMessage.setResponseType(responseType);
        chatMessageRepository.save(chatMessage);

        return new ChatResponse(botResponse, responseType);
    }

    private String getRuleBased(String message) {
        for (Map.Entry<String, String> entry : ruleBased.entrySet()) {
            if (message.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private String getAIResponse(String message) {
        try {
            return callRouterAPI(message);
        } catch (Exception e) {
            return "I'm here to support you through whatever you're experiencing. While I'm having trouble accessing my advanced responses right now, I can still help you. Would you like me to suggest some meditation resources, or would you prefer information about booking a session with one of our professional therapists?";
        }
    }

    private String callRouterAPI(String userMessage) {
        try {
            log.info("Calling OpenRouter API for message: {}", userMessage);

            if (routerApiKey == null) {
                log.error("OpenRouter API key is not configured (null)");
                throw new RuntimeException("API key not configured");
            }
            String cleanedKey = routerApiKey.trim().replace("\r", "").replace("\n", "");
            if (cleanedKey.isEmpty() || cleanedKey.contains("\r") || cleanedKey.contains("\n")) {
                log.error("OpenRouter API key is empty or contains invalid characters");
                throw new RuntimeException("API key invalid");
            }

            WebClient webClient = webClientBuilder.build();

            // Build OpenAI-compatible Chat Completions request body for OpenRouter
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek/deepseek-r1-distill-llama-70b");

            String systemPrompt = "You are a compassionate mental health support chatbot named Mantra. " +
                    "Respond helpfully and empathetically to the user's message. " +
                    "Keep responses supportive, professional, and under 150 words. " +
                    "If appropriate, suggest meditation, therapy, or community support.";

            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);

            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);

            requestBody.put("messages", List.of(systemMessage, userMsg));
            requestBody.put("temperature", 0.7);
            requestBody.put("top_p", 0.95);
            requestBody.put("max_tokens", 300);

            log.info("Making API call to OpenRouter chat completions endpoint");
            log.info("Request body (no key): {}", requestBody);

            String response = webClient.post()
                    .uri(routerApiUrl)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + cleanedKey)
                    .header("HTTP-Referer", "http://localhost:3000")
                    .header("X-Title", "Mantra Chatbot")
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("OpenRouter API error: {}", errorBody);
                                        return clientResponse.createException();
                                    }))
                    .bodyToMono(String.class)
                    .block();

            log.info("Received response from OpenRouter API: {}", response);

            if (response != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode json = objectMapper.readTree(response);
                    JsonNode choices = json.get("choices");
                    if (choices != null && choices.isArray() && choices.size() > 0) {
                        JsonNode first = choices.get(0);
                        JsonNode messageNode = first.get("message");
                        if (messageNode != null) {
                            JsonNode content = messageNode.get("content");
                            if (content != null) {
                                return content.asText();
                            }
                        }
                        JsonNode textNode = first.get("text");
                        if (textNode != null) {
                            return textNode.asText();
                        }
                    }
                } catch (Exception e) {
                    log.error("Error parsing OpenRouter API response: {}", e.getMessage());
                }
            }

            // Fallback if parsing fails
            return "Thank you for sharing that with me. It takes courage to reach out for support. " +
                    "Based on what you've told me, I'd recommend taking some time for self-care and mindfulness. " +
                    "Our meditation library has some great resources that might help, and if you'd like to talk to someone professionally, " +
                    "our therapists are here to support you. Remember, you're not alone in this journey.";

        } catch (Exception e) {
            log.error("Error calling OpenRouter API: {}", e.getMessage());
            return "I appreciate you reaching out to me. While I'm having some technical difficulties right now, " +
                    "I want you to know that your feelings are valid and support is available. " +
                    "Please consider exploring our meditation resources or booking a session with one of our professional therapists. " +
                    "You deserve care and support.";
        }
    }
}