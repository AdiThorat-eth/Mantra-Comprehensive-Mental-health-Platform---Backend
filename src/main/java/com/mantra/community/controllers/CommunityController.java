package com.mantra.community.controllers;

import com.mantra.community.dto.PostDTO;
import com.mantra.community.dto.ReplyDTO;
import com.mantra.community.services.CommunityService;
import com.mantra.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Community", description = "Community forum for sharing and discussion")
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping
    @Operation(summary = "Get all posts")
    public ResponseEntity<ApiResponse<List<PostDTO>>> getAllPosts() {
        List<PostDTO> posts = communityService.getAllPosts();
        return ResponseEntity.ok(ApiResponse.success("Posts retrieved successfully", posts));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID with replies")
    public ResponseEntity<ApiResponse<PostDTO>> getPostById(@PathVariable Long id) {
        PostDTO post = communityService.getPostById(id);
        return ResponseEntity.ok(ApiResponse.success("Post retrieved successfully", post));
    }

    @PostMapping
    @Operation(summary = "Create new post")
    public ResponseEntity<ApiResponse<PostDTO>> createPost(@Valid @RequestBody PostDTO postDTO) {
        PostDTO createdPost = communityService.createPost(postDTO);
        return ResponseEntity.ok(ApiResponse.success("Post created successfully", createdPost));
    }

    @PostMapping("/{id}/replies")
    @Operation(summary = "Reply to a post")
    public ResponseEntity<ApiResponse<ReplyDTO>> createReply(
            @PathVariable Long id, 
            @Valid @RequestBody ReplyDTO replyDTO) {
        ReplyDTO createdReply = communityService.createReply(id, replyDTO);
        return ResponseEntity.ok(ApiResponse.success("Reply created successfully", createdReply));
    }
}