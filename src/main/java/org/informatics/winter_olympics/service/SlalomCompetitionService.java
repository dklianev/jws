package org.informatics.winter_olympics.service;

import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.dto.CreateSlalomCompetitionDto;
import org.informatics.winter_olympics.dto.SlalomCompetitionDto;

import java.time.LocalDate;
import java.util.List;

public interface SlalomCompetitionService {
    List<SlalomCompetitionDto> getSlalomCompetitions();

    SlalomCompetitionDto getSlalomCompetitionById(long id);

    SlalomCompetitionDto createSlalomCompetition(CreateSlalomCompetitionDto competitionDto);

    SlalomCompetitionDto updateSlalomCompetition(SlalomCompetitionDto competitionDto, long id);

    void deleteSlalomCompetition(long id);

    List<SlalomCompetitionDto> findByOlympicsId(Long olympicsId);

    List<SlalomCompetitionDto> findByGender(Gender gender);

    List<SlalomCompetitionDto> findByCompetitionDateBefore(LocalDate date);
}
