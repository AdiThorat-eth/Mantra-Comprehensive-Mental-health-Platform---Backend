package com.mantra.community.services;

import com.mantra.auth.entities.User;
import com.mantra.community.dto.PostDTO;
import com.mantra.community.dto.ReplyDTO;
import com.mantra.community.entities.Post;
import com.mantra.community.entities.Reply;
import com.mantra.community.repositories.PostRepository;
import com.mantra.community.repositories.ReplyRepository;
import com.mantra.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertPostToDTO)
                .collect(Collectors.toList());
    }

    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        
        PostDTO postDTO = convertPostToDTO(post);
        List<ReplyDTO> replies = replyRepository.findByPostId(id).stream()
                .map(this::convertReplyToDTO)
                .collect(Collectors.toList());
        postDTO.setReplies(replies);
        
        return postDTO;
    }

    public PostDTO createPost(PostDTO postDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setAuthor(currentUser);

        Post savedPost = postRepository.save(post);
        return convertPostToDTO(savedPost);
    }

    public ReplyDTO createReply(Long postId, ReplyDTO replyDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        Reply reply = new Reply();
        reply.setContent(replyDTO.getContent());
        reply.setAuthor(currentUser);
        reply.setPost(post);

        Reply savedReply = replyRepository.save(reply);
        return convertReplyToDTO(savedReply);
    }

    private PostDTO convertPostToDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getFirstName() + " " + post.getAuthor().getLastName(),
                post.getAuthor().getEmail(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                null // replies will be set separately if needed
        );
    }

    private ReplyDTO convertReplyToDTO(Reply reply) {
        return new ReplyDTO(
                reply.getId(),
                reply.getContent(),
                reply.getPost().getId(),
                reply.getAuthor().getFirstName() + " " + reply.getAuthor().getLastName(),
                reply.getAuthor().getEmail(),
                reply.getCreatedAt()
        );
    }
}