package org.informatics.winter_olympics.service;

import org.informatics.winter_olympics.dto.MedalCountDto;
import org.informatics.winter_olympics.dto.OlympicsStatisticsDto;
import org.informatics.winter_olympics.dto.RankingEntryDto;
import org.informatics.winter_olympics.dto.SlalomResultDto;

import java.util.List;

public interface RankingService {
    List<RankingEntryDto> getCompetitionRanking(long competitionId);

    List<SlalomResultDto> getRunTwoStartOrder(long competitionId);

    List<MedalCountDto> getMedalTable(long olympicsId);

    OlympicsStatisticsDto getOlympicsStatistics(long olympicsId);
}
