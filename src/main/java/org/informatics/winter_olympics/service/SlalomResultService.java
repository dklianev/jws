package org.informatics.winter_olympics.service;

import org.informatics.winter_olympics.dto.CreateSlalomResultDto;
import org.informatics.winter_olympics.dto.SlalomResultDto;

import java.util.List;

public interface SlalomResultService {
    List<SlalomResultDto> getSlalomResults();

    SlalomResultDto getSlalomResultById(long id);

    SlalomResultDto createSlalomResult(CreateSlalomResultDto resultDto);

    SlalomResultDto registerCurrentAthlete(long competitionId, String username);

    SlalomResultDto updateSlalomResult(SlalomResultDto resultDto, long id);

    void deleteSlalomResult(long id);

    List<SlalomResultDto> findByCompetitionId(Long competitionId);

    List<SlalomResultDto> calculateSecondRunQualifiers(long competitionId);

    List<SlalomResultDto> getRunOneStandings(long competitionId);

    List<SlalomResultDto> getRunTwoStartOrder(long competitionId);
}
