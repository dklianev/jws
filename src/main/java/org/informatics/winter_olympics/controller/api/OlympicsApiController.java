package org.informatics.winter_olympics.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.dto.CreateOlympicsDto;
import org.informatics.winter_olympics.dto.OlympicsDto;
import org.informatics.winter_olympics.service.OlympicsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/olympics")
@AllArgsConstructor
public class OlympicsApiController {

    private final OlympicsService olympicsService;

    @GetMapping
    public List<OlympicsDto> getOlympics() {
        return olympicsService.getOlympics();
    }

    @GetMapping("/{id}")
    public OlympicsDto getOlympicsById(@PathVariable long id) {
        return olympicsService.getOlympicsById(id);
    }

    @PostMapping
    public OlympicsDto createOlympics(@RequestBody @Valid CreateOlympicsDto olympicsDto) {
        return olympicsService.createOlympics(olympicsDto);
    }

    @PutMapping("/{id}")
    public OlympicsDto updateOlympics(@RequestBody @Valid OlympicsDto olympicsDto, @PathVariable long id) {
        return olympicsService.updateOlympics(olympicsDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteOlympics(@PathVariable long id) {
        olympicsService.deleteOlympics(id);
    }

    @GetMapping("/by-country/{country}")
    public List<OlympicsDto> findByCountry(@PathVariable String country) {
        return olympicsService.findByCountry(country);
    }

    @GetMapping("/by-name/{text}")
    public List<OlympicsDto> findByNameContaining(@PathVariable String text) {
        return olympicsService.findByNameContaining(text);
    }

    @GetMapping("/before/{date}")
    public List<OlympicsDto> findByStartDateBefore(@PathVariable LocalDate date) {
        return olympicsService.findByStartDateBefore(date);
    }
}
