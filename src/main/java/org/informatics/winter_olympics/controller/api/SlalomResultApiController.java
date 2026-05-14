package org.informatics.winter_olympics.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.dto.CreateSlalomResultDto;
import org.informatics.winter_olympics.dto.SlalomResultDto;
import org.informatics.winter_olympics.service.SlalomResultService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/slalom-results")
@AllArgsConstructor
public class SlalomResultApiController {

    private final SlalomResultService slalomResultService;

    @GetMapping
    public List<SlalomResultDto> getSlalomResults() {
        return slalomResultService.getSlalomResults();
    }

    @GetMapping("/{id}")
    public SlalomResultDto getSlalomResultById(@PathVariable long id) {
        return slalomResultService.getSlalomResultById(id);
    }

    @PostMapping
    public SlalomResultDto createSlalomResult(@RequestBody @Valid CreateSlalomResultDto resultDto) {
        return slalomResultService.createSlalomResult(resultDto);
    }

    @PostMapping("/register/{competitionId}")
    public SlalomResultDto registerCurrentAthlete(@PathVariable long competitionId, Principal principal) {
        return slalomResultService.registerCurrentAthlete(competitionId, principal.getName());
    }

    @PutMapping("/{id}")
    public SlalomResultDto updateSlalomResult(@RequestBody @Valid SlalomResultDto resultDto, @PathVariable long id) {
        return slalomResultService.updateSlalomResult(resultDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteSlalomResult(@PathVariable long id) {
        slalomResultService.deleteSlalomResult(id);
    }

    @GetMapping("/by-competition/{competitionId}")
    public List<SlalomResultDto> findByCompetitionId(@PathVariable Long competitionId) {
        return slalomResultService.findByCompetitionId(competitionId);
    }

    @GetMapping("/second-run-order/{competitionId}")
    public List<SlalomResultDto> getRunTwoStartOrder(@PathVariable long competitionId) {
        return slalomResultService.getRunTwoStartOrder(competitionId);
    }
}
