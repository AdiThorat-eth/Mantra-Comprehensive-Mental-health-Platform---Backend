package com.mantra.meditation.repositories;

import com.mantra.meditation.entities.Meditation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeditationRepository extends JpaRepository<Meditation, Long> {
}