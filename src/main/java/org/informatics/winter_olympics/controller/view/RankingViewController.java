package org.informatics.winter_olympics.controller.view;

import lombok.RequiredArgsConstructor;
import org.informatics.winter_olympics.service.BiathlonCompetitionService;
import org.informatics.winter_olympics.service.OlympicsService;
import org.informatics.winter_olympics.service.RankingService;
import org.informatics.winter_olympics.service.SlalomCompetitionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rankings")
public class RankingViewController {
    private final RankingService rankingService;
    private final OlympicsService olympicsService;
    private final SlalomCompetitionService slalomCompetitionService;
    private final BiathlonCompetitionService biathlonCompetitionService;

    @GetMapping
    public String getRankingHome(Model model) {
        model.addAttribute("olympics", olympicsService.getOlympics());
        model.addAttribute("slalomCompetitions", slalomCompetitionService.getSlalomCompetitions());
        model.addAttribute("biathlonCompetitions", biathlonCompetitionService.getBiathlonCompetitions());
        return "/rankings/competition-ranking.html";
    }

    @GetMapping("/competition/{competitionId}")
    public String getCompetitionRanking(Model model, @PathVariable long competitionId) {
        model.addAttribute("ranking", rankingService.getCompetitionRanking(competitionId));
        return "/rankings/competition-ranking.html";
    }

    @GetMapping("/second-run/{competitionId}")
    public String getRunTwoStartOrder(Model model, @PathVariable long competitionId) {
        model.addAttribute("results", rankingService.getRunTwoStartOrder(competitionId));
        return "/rankings/second-run.html";
    }

    @GetMapping("/medal-table/{olympicsId}")
    public String getMedalTable(Model model, @PathVariable long olympicsId) {
        model.addAttribute("medals", rankingService.getMedalTable(olympicsId));
        return "/rankings/medal-table.html";
    }

    @GetMapping("/statistics/{olympicsId}")
    public String getOlympicsStatistics(Model model, @PathVariable long olympicsId) {
        model.addAttribute("statistics", rankingService.getOlympicsStatistics(olympicsId));
        return "/rankings/olympics-statistics.html";
    }
}
