package org.informatics.winter_olympics.service;

import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.dto.AthleteDto;
import org.informatics.winter_olympics.dto.CreateAthleteDto;

import java.time.LocalDate;
import java.util.List;

public interface AthleteService {
    List<AthleteDto> getAthletes();

    AthleteDto getAthleteById(long id);

    AthleteDto createAthlete(CreateAthleteDto createAthleteDto);

    AthleteDto selfRegisterAthlete(CreateAthleteDto createAthleteDto, String username);

    AthleteDto updateAthlete(AthleteDto athleteDto, long id);

    void deleteAthlete(long id);

    List<AthleteDto> findByCountry(String country);

    List<AthleteDto> findByGender(Gender gender);

    List<AthleteDto> findByCountryAndGender(String country, Gender gender);

    List<AthleteDto> findByDateOfBirthBefore(LocalDate dateOfBirth);

    List<AthleteDto> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);

    List<AthleteDto> findByLastNameContainingOrderByFirstNameAsc(String text);

    long countByCountry(String country);
}
