package org.informatics.winter_olympics.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.informatics.winter_olympics.dto.BiathlonResultDto;
import org.informatics.winter_olympics.dto.CreateBiathlonResultDto;
import org.informatics.winter_olympics.service.AthleteService;
import org.informatics.winter_olympics.service.BiathlonCompetitionService;
import org.informatics.winter_olympics.service.BiathlonResultService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/results/biathlon")
public class BiathlonResultViewController {
    private final BiathlonResultService biathlonResultService;
    private final AthleteService athleteService;
    private final BiathlonCompetitionService biathlonCompetitionService;

    @GetMapping("/enter-biathlon-result")
    public String showCreateBiathlonResultForm(Model model) {
        model.addAttribute("result", new BiathlonResultDto());
        prepareModel(model);
        return "/results/enter-biathlon-result";
    }

    @PostMapping("/create")
    public String createBiathlonResult(@Valid @ModelAttribute("result") CreateBiathlonResultDto result, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            prepareModel(model);
            return "/results/enter-biathlon-result";
        }
        this.biathlonResultService.createBiathlonResult(result);
        return "redirect:/rankings";
    }

    @GetMapping("/edit-biathlon-result/{id}")
    public String showEditBiathlonResultForm(Model model, @PathVariable Long id) {
        model.addAttribute("result", this.biathlonResultService.getBiathlonResultById(id));
        return "/results/edit-biathlon-result";
    }

    @PostMapping("/update/{id}")
    public String updateBiathlonResult(@PathVariable long id, @Valid @ModelAttribute("result") BiathlonResultDto result, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/results/edit-biathlon-result";
        }
        this.biathlonResultService.updateBiathlonResult(result, id);
        return "redirect:/rankings";
    }

    @GetMapping("/delete/{id}")
    public String deleteBiathlonResult(@PathVariable int id) {
        this.biathlonResultService.deleteBiathlonResult(id);
        return "redirect:/rankings";
    }

    private void prepareModel(Model model) {
        model.addAttribute("athletes", athleteService.getAthletes());
        model.addAttribute("competitions", biathlonCompetitionService.getBiathlonCompetitions());
    }
}
