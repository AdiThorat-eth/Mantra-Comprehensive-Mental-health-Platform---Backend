package com.mantra.admin.services;

import com.mantra.admin.dto.AdminStatsDTO;
import com.mantra.auth.repositories.UserRepository;
import com.mantra.chatbot.repositories.ChatMessageRepository;
import com.mantra.community.repositories.PostRepository;
import com.mantra.community.repositories.ReplyRepository;
import com.mantra.contact.repositories.ContactMessageRepository;
import com.mantra.meditation.repositories.MeditationRepository;
import com.mantra.therapy.repositories.BookingRepository;
import com.mantra.therapy.repositories.TherapistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final TherapistRepository therapistRepository;
    private final BookingRepository bookingRepository;
    private final MeditationRepository meditationRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final ContactMessageRepository contactMessageRepository;

    public AdminStatsDTO getSystemStats() {
        return new AdminStatsDTO(
                userRepository.count(),
                therapistRepository.count(),
                bookingRepository.count(),
                meditationRepository.count(),
                postRepository.count(),
                replyRepository.count(),
                contactMessageRepository.count()
        );
    }
}