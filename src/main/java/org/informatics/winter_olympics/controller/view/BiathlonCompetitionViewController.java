package org.informatics.winter_olympics.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.dto.BiathlonCompetitionDto;
import org.informatics.winter_olympics.dto.CreateBiathlonCompetitionDto;
import org.informatics.winter_olympics.service.BiathlonCompetitionService;
import org.informatics.winter_olympics.service.OlympicsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/biathlon-competitions")
public class BiathlonCompetitionViewController {
    private final BiathlonCompetitionService biathlonCompetitionService;
    private final OlympicsService olympicsService;

    @GetMapping
    public String getBiathlonCompetitions(Model model) {
        List<BiathlonCompetitionDto> competitions = this.biathlonCompetitionService.getBiathlonCompetitions();
        model.addAttribute("competitions", competitions);
        return "/biathlon-competitions/biathlon-competitions.html";
    }

    @GetMapping("/create-biathlon-competition")
    public String showCreateBiathlonCompetitionForm(Model model) {
        model.addAttribute("competition", new BiathlonCompetitionDto());
        prepareModel(model);
        return "/biathlon-competitions/create-biathlon-competition";
    }

    @PostMapping("/create")
    public String createBiathlonCompetition(@Valid @ModelAttribute("competition") CreateBiathlonCompetitionDto competition, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            prepareModel(model);
            return "/biathlon-competitions/create-biathlon-competition";
        }
        this.biathlonCompetitionService.createBiathlonCompetition(competition);
        return "redirect:/biathlon-competitions";
    }

    @GetMapping("/edit-biathlon-competition/{id}")
    public String showEditBiathlonCompetitionForm(Model model, @PathVariable Long id) {
        model.addAttribute("competition", this.biathlonCompetitionService.getBiathlonCompetitionById(id));
        prepareModel(model);
        return "/biathlon-competitions/edit-biathlon-competition";
    }

    @GetMapping("/view-biathlon-competition/{id}")
    public String viewBiathlonCompetition(Model model, @PathVariable Long id) {
        model.addAttribute("competition", this.biathlonCompetitionService.getBiathlonCompetitionById(id));
        return "/biathlon-competitions/view-biathlon-competition";
    }

    @PostMapping("/update/{id}")
    public String updateBiathlonCompetition(@PathVariable long id, @Valid @ModelAttribute("competition") BiathlonCompetitionDto competition, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            prepareModel(model);
            return "/biathlon-competitions/edit-biathlon-competition";
        }
        this.biathlonCompetitionService.updateBiathlonCompetition(competition, id);
        return "redirect:/biathlon-competitions";
    }

    @GetMapping("/delete/{id}")
    public String deleteBiathlonCompetition(@PathVariable int id) {
        this.biathlonCompetitionService.deleteBiathlonCompetition(id);
        return "redirect:/biathlon-competitions";
    }

    private void prepareModel(Model model) {
        model.addAttribute("genders", Gender.values());
        model.addAttribute("olympicsList", olympicsService.getOlympics());
    }
}
