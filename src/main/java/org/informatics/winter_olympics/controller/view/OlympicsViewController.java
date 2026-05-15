package org.informatics.winter_olympics.controller.view;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.informatics.winter_olympics.dto.CreateOlympicsDto;
import org.informatics.winter_olympics.dto.OlympicsDto;
import org.informatics.winter_olympics.service.OlympicsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/olympics")
public class OlympicsViewController {
    private final OlympicsService olympicsService;

    @GetMapping
    public String getOlympics(Model model) {
        List<OlympicsDto> olympics = this.olympicsService.getOlympics();
        model.addAttribute("olympics", olympics);
        return "/olympics/olympics.html";
    }

    @GetMapping("/create-olympics")
    public String showCreateOlympicsForm(Model model) {
        model.addAttribute("olympics", new OlympicsDto());
        return "/olympics/create-olympics";
    }

    @PostMapping("/create")
    public String createOlympics(@Valid @ModelAttribute("olympics") CreateOlympicsDto olympics, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/olympics/create-olympics";
        }
        this.olympicsService.createOlympics(olympics);
        return "redirect:/olympics";
    }

    @GetMapping("/edit-olympics/{id}")
    public String showEditOlympicsForm(Model model, @PathVariable Long id) {
        model.addAttribute("olympics", this.olympicsService.getOlympicsById(id));
        return "/olympics/edit-olympics";
    }

    @GetMapping("/view-olympics/{id}")
    public String viewOlympics(Model model, @PathVariable Long id) {
        model.addAttribute("olympics", this.olympicsService.getOlympicsById(id));
        return "/olympics/view-olympics";
    }

    @PostMapping("/update/{id}")
    public String updateOlympics(@PathVariable long id, @Valid @ModelAttribute("olympics") OlympicsDto olympics, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/olympics/edit-olympics";
        }
        this.olympicsService.updateOlympics(olympics, id);
        return "redirect:/olympics";
    }

    @GetMapping("/delete/{id}")
    public String deleteOlympics(@PathVariable int id) {
        this.olympicsService.deleteOlympics(id);
        return "redirect:/olympics";
    }
}
