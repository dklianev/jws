package org.informatics.winter_olympics.service;

import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.dto.BiathlonCompetitionDto;
import org.informatics.winter_olympics.dto.CreateBiathlonCompetitionDto;

import java.time.LocalDate;
import java.util.List;

public interface BiathlonCompetitionService {
    List<BiathlonCompetitionDto> getBiathlonCompetitions();

    BiathlonCompetitionDto getBiathlonCompetitionById(long id);

    BiathlonCompetitionDto createBiathlonCompetition(CreateBiathlonCompetitionDto competitionDto);

    BiathlonCompetitionDto updateBiathlonCompetition(BiathlonCompetitionDto competitionDto, long id);

    void deleteBiathlonCompetition(long id);

    List<BiathlonCompetitionDto> findByOlympicsId(Long olympicsId);

    List<BiathlonCompetitionDto> findByGender(Gender gender);

    List<BiathlonCompetitionDto> findByCompetitionDateBefore(LocalDate date);
}
