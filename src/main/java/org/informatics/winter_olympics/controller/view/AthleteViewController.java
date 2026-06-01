package org.informatics.winter_olympics.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.dto.AthleteDto;
import org.informatics.winter_olympics.dto.CreateAthleteDto;
import org.informatics.winter_olympics.service.AthleteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/athletes")
public class AthleteViewController {
    private final AthleteService athleteService;

    @GetMapping
    public String getAthletes(Model model) {
        List<AthleteDto> athletes = this.athleteService.getAthletes();
        model.addAttribute("athletes", athletes);
        return "/athletes/athletes.html";
    }

    @GetMapping("/create-athlete")
    public String showCreateAthleteForm(Model model) {
        model.addAttribute("athlete", new AthleteDto());
        model.addAttribute("genders", Gender.values());
        return "/athletes/create-athlete";
    }

    @PostMapping("/create")
    public String createAthlete(@Valid @ModelAttribute("athlete") CreateAthleteDto athlete, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genders", Gender.values());
            return "/athletes/create-athlete";
        }
        this.athleteService.createAthlete(athlete);
        return "redirect:/athletes";
    }

    @GetMapping("/self-register")
    public String showSelfRegisterAthleteForm(Model model) {
        model.addAttribute("athlete", new AthleteDto());
        model.addAttribute("genders", Gender.values());
        return "/athletes/self-register-athlete";
    }

    @PostMapping("/self-register")
    public String selfRegisterAthlete(@Valid @ModelAttribute("athlete") CreateAthleteDto athlete, BindingResult bindingResult,
                                      Model model, Principal principal, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genders", Gender.values());
            return "/athletes/self-register-athlete";
        }
        this.athleteService.selfRegisterAthlete(athlete, principal.getName());
        redirectAttributes.addFlashAttribute("message", "Your athlete profile was created. You can now register for competitions.");
        return "redirect:/registrations";
    }

    @GetMapping("/edit-athlete/{id}")
    public String showEditAthleteForm(Model model, @PathVariable Long id) {
        model.addAttribute("athlete", this.athleteService.getAthleteById(id));
        model.addAttribute("genders", Gender.values());
        return "/athletes/edit-athlete";
    }

    @PostMapping("/update/{id}")
    public String updateAthlete(@PathVariable long id, @Valid @ModelAttribute("athlete") AthleteDto athlete, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genders", Gender.values());
            return "/athletes/edit-athlete";
        }
        this.athleteService.updateAthlete(athlete, id);
        return "redirect:/athletes";
    }

    @GetMapping("/delete/{id}")
    public String deleteAthlete(@PathVariable int id) {
        this.athleteService.deleteAthlete(id);
        return "redirect:/athletes";
    }
}
