package org.informatics.winter_olympics.data.repository;

import org.informatics.winter_olympics.data.entity.Competition;
import org.informatics.winter_olympics.data.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    List<Competition> findByOlympicsId(Long olympicsId);

    List<Competition> findByGender(Gender gender);

    List<Competition> findByCompetitionDateBefore(LocalDate date);

    List<Competition> findByNameContainingOrderByCompetitionDateAsc(String text);
}
