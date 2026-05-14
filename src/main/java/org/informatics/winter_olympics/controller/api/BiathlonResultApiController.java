package org.informatics.winter_olympics.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.dto.BiathlonResultDto;
import org.informatics.winter_olympics.dto.CreateBiathlonResultDto;
import org.informatics.winter_olympics.service.BiathlonResultService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/biathlon-results")
@AllArgsConstructor
public class BiathlonResultApiController {

    private final BiathlonResultService biathlonResultService;

    @GetMapping
    public List<BiathlonResultDto> getBiathlonResults() {
        return biathlonResultService.getBiathlonResults();
    }

    @GetMapping("/{id}")
    public BiathlonResultDto getBiathlonResultById(@PathVariable long id) {
        return biathlonResultService.getBiathlonResultById(id);
    }

    @PostMapping
    public BiathlonResultDto createBiathlonResult(@RequestBody @Valid CreateBiathlonResultDto resultDto) {
        return biathlonResultService.createBiathlonResult(resultDto);
    }

    @PostMapping("/register/{competitionId}")
    public BiathlonResultDto registerCurrentAthlete(@PathVariable long competitionId, Principal principal) {
        return biathlonResultService.registerCurrentAthlete(competitionId, principal.getName());
    }

    @PutMapping("/{id}")
    public BiathlonResultDto updateBiathlonResult(@RequestBody @Valid BiathlonResultDto resultDto, @PathVariable long id) {
        return biathlonResultService.updateBiathlonResult(resultDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteBiathlonResult(@PathVariable long id) {
        biathlonResultService.deleteBiathlonResult(id);
    }

    @GetMapping("/by-competition/{competitionId}")
    public List<BiathlonResultDto> findByCompetitionId(@PathVariable Long competitionId) {
        return biathlonResultService.findByCompetitionId(competitionId);
    }
}
