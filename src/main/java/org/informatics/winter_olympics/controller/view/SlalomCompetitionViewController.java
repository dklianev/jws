package org.informatics.winter_olympics.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.dto.CreateSlalomCompetitionDto;
import org.informatics.winter_olympics.dto.SlalomCompetitionDto;
import org.informatics.winter_olympics.service.OlympicsService;
import org.informatics.winter_olympics.service.SlalomCompetitionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/slalom-competitions")
public class SlalomCompetitionViewController {
    private final SlalomCompetitionService slalomCompetitionService;
    private final OlympicsService olympicsService;

    @GetMapping
    public String getSlalomCompetitions(Model model) {
        List<SlalomCompetitionDto> competitions = this.slalomCompetitionService.getSlalomCompetitions();
        model.addAttribute("competitions", competitions);
        return "/slalom-competitions/slalom-competitions.html";
    }

    @GetMapping("/create-slalom-competition")
    public String showCreateSlalomCompetitionForm(Model model) {
        model.addAttribute("competition", new SlalomCompetitionDto());
        prepareModel(model);
        return "/slalom-competitions/create-slalom-competition";
    }

    @PostMapping("/create")
    public String createSlalomCompetition(@Valid @ModelAttribute("competition") CreateSlalomCompetitionDto competition, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            prepareModel(model);
            return "/slalom-competitions/create-slalom-competition";
        }
        this.slalomCompetitionService.createSlalomCompetition(competition);
        return "redirect:/slalom-competitions";
    }

    @GetMapping("/edit-slalom-competition/{id}")
    public String showEditSlalomCompetitionForm(Model model, @PathVariable Long id) {
        model.addAttribute("competition", this.slalomCompetitionService.getSlalomCompetitionById(id));
        prepareModel(model);
        return "/slalom-competitions/edit-slalom-competition";
    }

    @GetMapping("/view-slalom-competition/{id}")
    public String viewSlalomCompetition(Model model, @PathVariable Long id) {
        model.addAttribute("competition", this.slalomCompetitionService.getSlalomCompetitionById(id));
        return "/slalom-competitions/view-slalom-competition";
    }

    @PostMapping("/update/{id}")
    public String updateSlalomCompetition(@PathVariable long id, @Valid @ModelAttribute("competition") SlalomCompetitionDto competition, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            prepareModel(model);
            return "/slalom-competitions/edit-slalom-competition";
        }
        this.slalomCompetitionService.updateSlalomCompetition(competition, id);
        return "redirect:/slalom-competitions";
    }

    @GetMapping("/delete/{id}")
    public String deleteSlalomCompetition(@PathVariable int id) {
        this.slalomCompetitionService.deleteSlalomCompetition(id);
        return "redirect:/slalom-competitions";
    }

    private void prepareModel(Model model) {
        model.addAttribute("genders", Gender.values());
        model.addAttribute("olympicsList", olympicsService.getOlympics());
    }
}
