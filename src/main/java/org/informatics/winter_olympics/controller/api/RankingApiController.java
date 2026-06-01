package org.informatics.winter_olympics.controller.api;

import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.dto.MedalCountDto;
import org.informatics.winter_olympics.dto.OlympicsStatisticsDto;
import org.informatics.winter_olympics.dto.RankingEntryDto;
import org.informatics.winter_olympics.dto.SlalomResultDto;
import org.informatics.winter_olympics.service.RankingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rankings")
@AllArgsConstructor
public class RankingApiController {

    private final RankingService rankingService;

    @GetMapping("/competition/{competitionId}")
    public List<RankingEntryDto> getCompetitionRanking(@PathVariable long competitionId) {
        return rankingService.getCompetitionRanking(competitionId);
    }

    @GetMapping("/competition/{competitionId}/first-run")
    public List<SlalomResultDto> getRunOneStandings(@PathVariable long competitionId) {
        return rankingService.getRunOneStandings(competitionId);
    }

    @GetMapping("/competition/{competitionId}/second-run")
    public List<SlalomResultDto> getRunTwoStartOrder(@PathVariable long competitionId) {
        return rankingService.getRunTwoStartOrder(competitionId);
    }

    @GetMapping("/olympics/{olympicsId}/medals")
    public List<MedalCountDto> getMedalTable(@PathVariable long olympicsId) {
        return rankingService.getMedalTable(olympicsId);
    }

    @GetMapping("/olympics/{olympicsId}/statistics")
    public OlympicsStatisticsDto getOlympicsStatistics(@PathVariable long olympicsId) {
        return rankingService.getOlympicsStatistics(olympicsId);
    }
}
