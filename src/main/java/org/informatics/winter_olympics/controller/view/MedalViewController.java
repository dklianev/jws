package org.informatics.winter_olympics.controller.view;

import lombok.RequiredArgsConstructor;
import org.informatics.winter_olympics.service.RankingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/medals")
public class MedalViewController {
    private final RankingService rankingService;

    @GetMapping("/{olympicsId}")
    public String getMedals(Model model, @PathVariable long olympicsId) {
        model.addAttribute("medals", rankingService.getMedalTable(olympicsId));
        return "/rankings/medal-table.html";
    }
}
