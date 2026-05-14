package org.informatics.winter_olympics.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.dto.BiathlonCompetitionDto;
import org.informatics.winter_olympics.dto.CreateBiathlonCompetitionDto;
import org.informatics.winter_olympics.service.BiathlonCompetitionService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/biathlon-competitions")
@AllArgsConstructor
public class BiathlonCompetitionApiController {

    private final BiathlonCompetitionService biathlonCompetitionService;

    @GetMapping
    public List<BiathlonCompetitionDto> getBiathlonCompetitions() {
        return biathlonCompetitionService.getBiathlonCompetitions();
    }

    @GetMapping("/{id}")
    public BiathlonCompetitionDto getBiathlonCompetitionById(@PathVariable long id) {
        return biathlonCompetitionService.getBiathlonCompetitionById(id);
    }

    @PostMapping
    public BiathlonCompetitionDto createBiathlonCompetition(@RequestBody @Valid CreateBiathlonCompetitionDto competitionDto) {
        return biathlonCompetitionService.createBiathlonCompetition(competitionDto);
    }

    @PutMapping("/{id}")
    public BiathlonCompetitionDto updateBiathlonCompetition(@RequestBody @Valid BiathlonCompetitionDto competitionDto, @PathVariable long id) {
        return biathlonCompetitionService.updateBiathlonCompetition(competitionDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteBiathlonCompetition(@PathVariable long id) {
        biathlonCompetitionService.deleteBiathlonCompetition(id);
    }

    @GetMapping("/by-olympics/{olympicsId}")
    public List<BiathlonCompetitionDto> findByOlympicsId(@PathVariable Long olympicsId) {
        return biathlonCompetitionService.findByOlympicsId(olympicsId);
    }

    @GetMapping("/by-gender/{gender}")
    public List<BiathlonCompetitionDto> findByGender(@PathVariable Gender gender) {
        return biathlonCompetitionService.findByGender(gender);
    }

    @GetMapping("/before/{date}")
    public List<BiathlonCompetitionDto> findByCompetitionDateBefore(@PathVariable LocalDate date) {
        return biathlonCompetitionService.findByCompetitionDateBefore(date);
    }
}
