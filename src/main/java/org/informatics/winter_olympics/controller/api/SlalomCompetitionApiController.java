package org.informatics.winter_olympics.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.dto.CreateSlalomCompetitionDto;
import org.informatics.winter_olympics.dto.SlalomCompetitionDto;
import org.informatics.winter_olympics.service.SlalomCompetitionService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/slalom-competitions")
@AllArgsConstructor
public class SlalomCompetitionApiController {

    private final SlalomCompetitionService slalomCompetitionService;

    @GetMapping
    public List<SlalomCompetitionDto> getSlalomCompetitions() {
        return slalomCompetitionService.getSlalomCompetitions();
    }

    @GetMapping("/{id}")
    public SlalomCompetitionDto getSlalomCompetitionById(@PathVariable long id) {
        return slalomCompetitionService.getSlalomCompetitionById(id);
    }

    @PostMapping
    public SlalomCompetitionDto createSlalomCompetition(@RequestBody @Valid CreateSlalomCompetitionDto competitionDto) {
        return slalomCompetitionService.createSlalomCompetition(competitionDto);
    }

    @PutMapping("/{id}")
    public SlalomCompetitionDto updateSlalomCompetition(@RequestBody @Valid SlalomCompetitionDto competitionDto, @PathVariable long id) {
        return slalomCompetitionService.updateSlalomCompetition(competitionDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteSlalomCompetition(@PathVariable long id) {
        slalomCompetitionService.deleteSlalomCompetition(id);
    }

    @GetMapping("/by-olympics/{olympicsId}")
    public List<SlalomCompetitionDto> findByOlympicsId(@PathVariable Long olympicsId) {
        return slalomCompetitionService.findByOlympicsId(olympicsId);
    }

    @GetMapping("/by-gender/{gender}")
    public List<SlalomCompetitionDto> findByGender(@PathVariable Gender gender) {
        return slalomCompetitionService.findByGender(gender);
    }

    @GetMapping("/before/{date}")
    public List<SlalomCompetitionDto> findByCompetitionDateBefore(@PathVariable LocalDate date) {
        return slalomCompetitionService.findByCompetitionDateBefore(date);
    }
}
