package org.informatics.winter_olympics.controller.view;

import lombok.RequiredArgsConstructor;
import org.informatics.winter_olympics.exception.BusinessRuleException;
import org.informatics.winter_olympics.service.BiathlonCompetitionService;
import org.informatics.winter_olympics.service.BiathlonResultService;
import org.informatics.winter_olympics.service.SlalomCompetitionService;
import org.informatics.winter_olympics.service.SlalomResultService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registrations")
public class CompetitionRegistrationViewController {

    private final SlalomCompetitionService slalomCompetitionService;
    private final BiathlonCompetitionService biathlonCompetitionService;
    private final SlalomResultService slalomResultService;
    private final BiathlonResultService biathlonResultService;

    @GetMapping
    public String showRegistrationPage(Model model) {
        model.addAttribute("slalomCompetitions", slalomCompetitionService.getSlalomCompetitions());
        model.addAttribute("biathlonCompetitions", biathlonCompetitionService.getBiathlonCompetitions());
        return "/registrations/competition-registration";
    }

    @PostMapping("/slalom/{competitionId}")
    public String registerForSlalom(@PathVariable long competitionId, Principal principal,
                                    RedirectAttributes redirectAttributes) {
        try {
            slalomResultService.registerCurrentAthlete(competitionId, principal.getName());
            redirectAttributes.addFlashAttribute("message", "You are registered for the selected slalom competition.");
        } catch (BusinessRuleException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/registrations";
    }

    @PostMapping("/biathlon/{competitionId}")
    public String registerForBiathlon(@PathVariable long competitionId, Principal principal,
                                      RedirectAttributes redirectAttributes) {
        try {
            biathlonResultService.registerCurrentAthlete(competitionId, principal.getName());
            redirectAttributes.addFlashAttribute("message", "You are registered for the selected biathlon competition.");
        } catch (BusinessRuleException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/registrations";
    }
}
