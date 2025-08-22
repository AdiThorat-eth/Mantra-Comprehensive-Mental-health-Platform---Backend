package com.mantra.contact.services;

import com.mantra.auth.entities.User;
import com.mantra.contact.dto.ContactMessageDTO;
import com.mantra.contact.entities.ContactMessage;
import com.mantra.contact.repositories.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageDTO sendMessage(ContactMessageDTO messageDTO) {
        User currentUser = null;
        try {
            currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            // User might not be logged in
        }

        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setUser(currentUser);
        contactMessage.setName(messageDTO.getName());
        contactMessage.setEmail(messageDTO.getEmail());
        contactMessage.setSubject(messageDTO.getSubject());
        contactMessage.setMessage(messageDTO.getMessage());

        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);
        return convertToDTO(savedMessage);
    }

    public List<ContactMessageDTO> getAllMessages() {
        return contactMessageRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ContactMessageDTO convertToDTO(ContactMessage message) {
        return new ContactMessageDTO(
                message.getId(),
                message.getName(),
                message.getEmail(),
                message.getSubject(),
                message.getMessage(),
                message.isResponded(),
                message.getCreatedAt()
        );
    }
}