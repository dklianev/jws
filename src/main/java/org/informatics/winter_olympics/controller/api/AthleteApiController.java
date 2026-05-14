package org.informatics.winter_olympics.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.dto.AthleteDto;
import org.informatics.winter_olympics.dto.CreateAthleteDto;
import org.informatics.winter_olympics.service.AthleteService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/athletes")
@AllArgsConstructor
public class AthleteApiController {

    private final AthleteService athleteService;

    @GetMapping
    public List<AthleteDto> getAthletes() {
        return athleteService.getAthletes();
    }

    @GetMapping("/{id}")
    public AthleteDto getAthleteById(@PathVariable long id) {
        return athleteService.getAthleteById(id);
    }

    @PostMapping
    public AthleteDto createAthlete(@RequestBody @Valid CreateAthleteDto athleteDto) {
        return athleteService.createAthlete(athleteDto);
    }

    @PostMapping("/self-register")
    public AthleteDto selfRegisterAthlete(@RequestBody @Valid CreateAthleteDto athleteDto, Principal principal) {
        return athleteService.selfRegisterAthlete(athleteDto, principal.getName());
    }

    @PutMapping("/{id}")
    public AthleteDto updateAthlete(@RequestBody @Valid AthleteDto athleteDto, @PathVariable long id) {
        return athleteService.updateAthlete(athleteDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteAthlete(@PathVariable long id) {
        athleteService.deleteAthlete(id);
    }

    @GetMapping("/by-country/{country}")
    public List<AthleteDto> findByCountry(@PathVariable String country) {
        return athleteService.findByCountry(country);
    }

    @GetMapping("/by-gender/{gender}")
    public List<AthleteDto> findByGender(@PathVariable Gender gender) {
        return athleteService.findByGender(gender);
    }

    @GetMapping("/by-country-and-gender/{country}/{gender}")
    public List<AthleteDto> findByCountryAndGender(@PathVariable String country, @PathVariable Gender gender) {
        return athleteService.findByCountryAndGender(country, gender);
    }

    @GetMapping("/born-before/{date}")
    public List<AthleteDto> findByDateOfBirthBefore(@PathVariable LocalDate date) {
        return athleteService.findByDateOfBirthBefore(date);
    }

    @GetMapping("/born-between/{startDate}/{endDate}")
    public List<AthleteDto> findByDateOfBirthBetween(@PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        return athleteService.findByDateOfBirthBetween(startDate, endDate);
    }

    @GetMapping("/by-last-name-containing/{text}")
    public List<AthleteDto> findByLastNameContainingOrderByFirstNameAsc(@PathVariable String text) {
        return athleteService.findByLastNameContainingOrderByFirstNameAsc(text);
    }

    @GetMapping("/count-by-country/{country}")
    public long countByCountry(@PathVariable String country) {
        return athleteService.countByCountry(country);
    }
}
