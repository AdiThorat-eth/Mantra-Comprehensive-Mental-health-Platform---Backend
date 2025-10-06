package com.mantra.community.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {
    private Long id;

    @NotBlank(message = "Content is required")
    private String content;

    private Long postId;
    private String authorName;
    private String authorEmail;
    private LocalDateTime createdAt;
}