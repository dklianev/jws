package org.informatics.winter_olympics.service;

import org.informatics.winter_olympics.dto.BiathlonResultDto;
import org.informatics.winter_olympics.dto.CreateBiathlonResultDto;

import java.util.List;

public interface BiathlonResultService {
    List<BiathlonResultDto> getBiathlonResults();

    BiathlonResultDto getBiathlonResultById(long id);

    BiathlonResultDto createBiathlonResult(CreateBiathlonResultDto resultDto);

    BiathlonResultDto registerCurrentAthlete(long competitionId, String username);

    BiathlonResultDto updateBiathlonResult(BiathlonResultDto resultDto, long id);

    void deleteBiathlonResult(long id);

    List<BiathlonResultDto> findByCompetitionId(Long competitionId);
}
