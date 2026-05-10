package org.informatics.winter_olympics.data.repository;

import org.informatics.winter_olympics.data.entity.SlalomResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SlalomResultRepository extends JpaRepository<SlalomResult, Long> {
    List<SlalomResult> findByCompetitionId(Long competitionId);

    List<SlalomResult> findByAthleteId(Long athleteId);

    Optional<SlalomResult> findByCompetitionIdAndAthleteId(Long competitionId, Long athleteId);

    List<SlalomResult> findByCompetitionIdAndFirstRunDnfFalseAndFirstRunTimeIsNotNullOrderByFirstRunTimeAsc(Long competitionId);

    List<SlalomResult> findByCompetitionIdAndQualifiedForSecondRunTrueOrderByFirstRunTimeDesc(Long competitionId);

    List<SlalomResult> findByCompetitionOlympicsId(Long olympicsId);
}
