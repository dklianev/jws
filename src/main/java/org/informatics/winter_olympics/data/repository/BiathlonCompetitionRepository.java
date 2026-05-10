package org.informatics.winter_olympics.data.repository;

import org.informatics.winter_olympics.data.entity.BiathlonCompetition;
import org.informatics.winter_olympics.data.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BiathlonCompetitionRepository extends JpaRepository<BiathlonCompetition, Long> {
    List<BiathlonCompetition> findByOlympicsId(Long olympicsId);

    List<BiathlonCompetition> findByGender(Gender gender);

    List<BiathlonCompetition> findByCompetitionDateBefore(LocalDate date);
}
