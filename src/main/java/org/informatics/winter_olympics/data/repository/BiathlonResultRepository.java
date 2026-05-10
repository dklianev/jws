package org.informatics.winter_olympics.data.repository;

import org.informatics.winter_olympics.data.entity.BiathlonResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BiathlonResultRepository extends JpaRepository<BiathlonResult, Long> {
    List<BiathlonResult> findByCompetitionId(Long competitionId);

    List<BiathlonResult> findByAthleteId(Long athleteId);

    Optional<BiathlonResult> findByCompetitionIdAndAthleteId(Long competitionId, Long athleteId);

    List<BiathlonResult> findByCompetitionOlympicsId(Long olympicsId);
}
