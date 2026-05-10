package org.informatics.winter_olympics.data.repository;

import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.data.entity.SlalomCompetition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SlalomCompetitionRepository extends JpaRepository<SlalomCompetition, Long> {
    List<SlalomCompetition> findByOlympicsId(Long olympicsId);

    List<SlalomCompetition> findByGender(Gender gender);

    List<SlalomCompetition> findByCompetitionDateBefore(LocalDate date);
}
