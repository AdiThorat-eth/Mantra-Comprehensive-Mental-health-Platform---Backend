package com.mantra.config;

import com.mantra.auth.entities.User;
import com.mantra.auth.repositories.UserRepository;
import com.mantra.chatbot.entities.ChatMessage;
import com.mantra.chatbot.repositories.ChatMessageRepository;
import com.mantra.community.entities.Post;
import com.mantra.community.entities.Reply;
import com.mantra.community.repositories.PostRepository;
import com.mantra.community.repositories.ReplyRepository;
import com.mantra.contact.entities.ContactMessage;
import com.mantra.contact.repositories.ContactMessageRepository;
import com.mantra.meditation.entities.Meditation;
import com.mantra.meditation.repositories.MeditationRepository;
import com.mantra.shared.enums.BookingStatus;
import com.mantra.shared.enums.Role;
import com.mantra.therapy.entities.Booking;
import com.mantra.therapy.entities.Therapist;
import com.mantra.therapy.repositories.BookingRepository;
import com.mantra.therapy.repositories.TherapistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MeditationRepository meditationRepository;
    private final TherapistRepository therapistRepository;
    private final BookingRepository bookingRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final ContactMessageRepository contactMessageRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            log.info("Data already exists, skipping initialization");
            return;
        }

        log.info("Initializing demo data...");
        
        // Create Users
        User admin = createUser("admin@mantra.com", "Admin", "User", Role.ADMIN);
        User sanket = createUser("sanket@example.com", "Sanket", "Patel", Role.USER);
        User adi = createUser("adi@example.com", "Aditya", "Shah", Role.USER);

        log.info("Created users: Admin, Sanket, Adi");

        // Create Meditations
        createMeditations();
        log.info("Created sample meditations");

        // Create Therapists
        Therapist drSharma = createTherapist("Dr. Priya Sharma", "Stress Management", 
            "Specialized in stress reduction and workplace anxiety management", 
            "dr.sharma@mantra.com", "+91-9876543210", 8);
            
        Therapist drPatel = createTherapist("Dr. Raj Patel", "Cognitive Behavioral Therapy", 
            "Expert in CBT and depression treatment with over 10 years experience", 
            "dr.patel@mantra.com", "+91-9876543211", 12);

        log.info("Created therapists: Dr. Sharma, Dr. Patel");

        // Create Bookings
        createBooking(sanket, drSharma, LocalDateTime.now().plusDays(3), 
            "Feeling overwhelmed with work stress lately");
        createBooking(adi, drPatel, LocalDateTime.now().plusDays(5), 
            "Need help managing anxiety during exams");

        log.info("Created sample bookings");

        // Create Community Posts and Replies
        Post stressPost = createPost(adi, "How do you deal with exam stress?", 
            "I'm in my final year and feeling really stressed about upcoming exams. " +
            "The pressure is getting to me and I can't sleep properly. " +
            "What strategies have worked for you?");

        createReply(sanket, stressPost, 
            "I use meditation before studying. It really helps clear my mind. " +
            "Also, try breaking your study sessions into smaller chunks with regular breaks.");

        createReply(admin, stressPost, 
            "Consider booking a session with one of our therapists. They can provide " +
            "personalized strategies for managing exam anxiety.");

        Post sleepPost = createPost(sanket, "Meditation before sleep - does it work?", 
            "I've been having trouble sleeping and someone recommended meditation. " +
            "Has anyone tried this? Which meditations work best for sleep?");

        createReply(adi, sleepPost, 
            "Yes! I use the 'Sleep Calm' meditation on this platform. " +
            "It's 20 minutes long and really helps me relax.");

        log.info("Created community forum posts and replies");

        // Create Contact Messages
        createContactMessage("sanket@example.com", "Sanket Patel", 
            "Therapy Session Inquiry", 
            "Hi, I want to know more about therapy sessions. " +
            "What types of therapy do you offer and how can I book a session? " +
            "Also, do you have any specialists for work-related stress?", sanket);

        createContactMessage("student@college.edu", "Rahul Kumar", 
            "Student Discount Inquiry", 
            "I'm a college student and interested in your meditation programs. " +
            "Do you offer any student discounts or special packages for students?", null);

        log.info("Created sample contact messages");

        // Create Chat Message History
        createChatMessage(sanket, "I feel really stressed about work", 
            "It's okay to feel stressed. Try deep breathing for 5 minutes. " +
            "Inhale for 4 counts, hold for 4, and exhale for 4.", "RULE_BASED");

        createChatMessage(adi, "I can't sleep at night", 
            "Avoid screens before bedtime and try a relaxation meditation. " +
            "Consider establishing a bedtime routine.", "RULE_BASED");

        createChatMessage(sanket, "What meditation would you recommend for beginners?", 
            "For beginners, I'd recommend starting with our 'Morning Relaxation' meditation. " +
            "It's only 10 minutes long and perfect for getting started with mindfulness practice.", "AI_GENERATED");

        log.info("Created sample chat message history");

        log.info("Demo data initialization completed successfully!");
        log.info("You can now login with:");
        log.info("Admin: admin@mantra.com / password");
        log.info("User: sanket@example.com / password");
        log.info("User: adi@example.com / password");
    }

    private User createUser(String email, String firstName, String lastName, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(role);
        return userRepository.save(user);
    }

    private void createMeditations() {
        List<Meditation> meditations = List.of(
            new Meditation("Morning Relaxation", 
                "Start your day with peace and mindfulness. This guided meditation helps you center yourself and prepare for the day ahead with positive energy.",
                10, "https://www.youtube.com/watch?v=ZToicYcHIOU"),
                
            new Meditation("Anxiety Relief", 
                "Find calm in moments of anxiety. This meditation guides you through breathing exercises and mindfulness techniques to ease anxious thoughts.",
                15, "https://www.youtube.com/watch?v=O-6f5wQXSu8"),
                
            new Meditation("Sleep Calm", 
                "Drift into peaceful sleep with this relaxing meditation. Perfect for those who struggle with falling asleep or staying asleep through the night.",
                20, "https://www.youtube.com/watch?v=1ZYbU82GVz4"),
                
            new Meditation("Workplace Stress Relief", 
                "Combat work-related stress with this focused meditation. Ideal for taking a mental break during busy workdays.",
                12, "https://www.youtube.com/watch?v=SEfs5TJZ6Nk"),
                
            new Meditation("Self-Compassion Practice", 
                "Learn to be kind to yourself with this loving-kindness meditation. Perfect for building self-esteem and emotional resilience.",
                18, "https://www.youtube.com/watch?v=11cQcrAhAeY")
        );
        
        meditationRepository.saveAll(meditations);
    }

    private Therapist createTherapist(String name, String specialization, String description, 
                                    String email, String phone, int experience) {
        Therapist therapist = new Therapist();
        therapist.setName(name);
        therapist.setSpecialization(specialization);
        therapist.setDescription(description);
        therapist.setEmail(email);
        therapist.setPhoneNumber(phone);
        therapist.setYearsOfExperience(experience);
        therapist.setAvailable(true);
        return therapistRepository.save(therapist);
    }

    private void createBooking(User user, Therapist therapist, LocalDateTime bookingDate, String notes) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTherapist(therapist);
        booking.setBookingDate(bookingDate);
        booking.setNotes(notes);
        booking.setStatus(BookingStatus.PENDING);
        bookingRepository.save(booking);
    }

    private Post createPost(User author, String title, String content) {
        Post post = new Post();
        post.setAuthor(author);
        post.setTitle(title);
        post.setContent(content);
        return postRepository.save(post);
    }

    private void createReply(User author, Post post, String content) {
        Reply reply = new Reply();
        reply.setAuthor(author);
        reply.setPost(post);
        reply.setContent(content);
        replyRepository.save(reply);
    }

    private void createContactMessage(String email, String name, String subject, String message, User user) {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setEmail(email);
        contactMessage.setName(name);
        contactMessage.setSubject(subject);
        contactMessage.setMessage(message);
        contactMessage.setUser(user);
        contactMessage.setResponded(false);
        contactMessageRepository.save(contactMessage);
    }

    private void createChatMessage(User user, String userMessage, String botResponse, String responseType) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUser(user);
        chatMessage.setUserMessage(userMessage);
        chatMessage.setBotResponse(botResponse);
        chatMessage.setResponseType(responseType);
        chatMessageRepository.save(chatMessage);
    }
}