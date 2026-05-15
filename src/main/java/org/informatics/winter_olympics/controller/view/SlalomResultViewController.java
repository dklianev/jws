package org.informatics.winter_olympics.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.informatics.winter_olympics.dto.CreateSlalomResultDto;
import org.informatics.winter_olympics.dto.SlalomResultDto;
import org.informatics.winter_olympics.service.AthleteService;
import org.informatics.winter_olympics.service.SlalomCompetitionService;
import org.informatics.winter_olympics.service.SlalomResultService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/results/slalom")
public class SlalomResultViewController {
    private final SlalomResultService slalomResultService;
    private final AthleteService athleteService;
    private final SlalomCompetitionService slalomCompetitionService;

    @GetMapping("/enter-slalom-result")
    public String showCreateSlalomResultForm(Model model) {
        model.addAttribute("result", new SlalomResultDto());
        prepareModel(model);
        return "/results/enter-slalom-result";
    }

    @PostMapping("/create")
    public String createSlalomResult(@Valid @ModelAttribute("result") CreateSlalomResultDto result, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            prepareModel(model);
            return "/results/enter-slalom-result";
        }
        this.slalomResultService.createSlalomResult(result);
        return "redirect:/rankings";
    }

    @GetMapping("/edit-slalom-result/{id}")
    public String showEditSlalomResultForm(Model model, @PathVariable Long id) {
        model.addAttribute("result", this.slalomResultService.getSlalomResultById(id));
        return "/results/edit-slalom-result";
    }

    @PostMapping("/update/{id}")
    public String updateSlalomResult(@PathVariable long id, @Valid @ModelAttribute("result") SlalomResultDto result, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/results/edit-slalom-result";
        }
        this.slalomResultService.updateSlalomResult(result, id);
        return "redirect:/rankings";
    }

    @GetMapping("/delete/{id}")
    public String deleteSlalomResult(@PathVariable int id) {
        this.slalomResultService.deleteSlalomResult(id);
        return "redirect:/rankings";
    }

    private void prepareModel(Model model) {
        model.addAttribute("athletes", athleteService.getAthletes());
        model.addAttribute("competitions", slalomCompetitionService.getSlalomCompetitions());
    }
}
