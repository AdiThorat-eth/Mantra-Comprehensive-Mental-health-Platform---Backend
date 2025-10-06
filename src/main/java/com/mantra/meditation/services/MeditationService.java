package com.mantra.meditation.services;

import com.mantra.meditation.dto.MeditationDTO;
import com.mantra.meditation.entities.Meditation;
import com.mantra.meditation.repositories.MeditationRepository;
import com.mantra.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeditationService {

    private final MeditationRepository meditationRepository;

    public List<MeditationDTO> getAllMeditations() {
        return meditationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MeditationDTO getMeditationById(Long id) {
        Meditation meditation = meditationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meditation not found with id: " + id));
        return convertToDTO(meditation);
    }

    public MeditationDTO createMeditation(MeditationDTO meditationDTO) {
        Meditation meditation = convertToEntity(meditationDTO);
        Meditation savedMeditation = meditationRepository.save(meditation);
        return convertToDTO(savedMeditation);
    }

    public MeditationDTO updateMeditation(Long id, MeditationDTO meditationDTO) {
        Meditation meditation = meditationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meditation not found with id: " + id));

        meditation.setTitle(meditationDTO.getTitle());
        meditation.setDescription(meditationDTO.getDescription());
        meditation.setDurationMinutes(meditationDTO.getDurationMinutes());
        meditation.setUrl(meditationDTO.getUrl());

        Meditation updatedMeditation = meditationRepository.save(meditation);
        return convertToDTO(updatedMeditation);
    }

    public void deleteMeditation(Long id) {
        if (!meditationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Meditation not found with id: " + id);
        }
        meditationRepository.deleteById(id);
    }

    private MeditationDTO convertToDTO(Meditation meditation) {
        return new MeditationDTO(
                meditation.getId(),
                meditation.getTitle(),
                meditation.getDescription(),
                meditation.getDurationMinutes(),
                meditation.getUrl(),
                meditation.getCreatedAt(),
                meditation.getUpdatedAt()
        );
    }

    private Meditation convertToEntity(MeditationDTO dto) {
        Meditation meditation = new Meditation();
        meditation.setTitle(dto.getTitle());
        meditation.setDescription(dto.getDescription());
        meditation.setDurationMinutes(dto.getDurationMinutes());
        meditation.setUrl(dto.getUrl());
        return meditation;
    }
}