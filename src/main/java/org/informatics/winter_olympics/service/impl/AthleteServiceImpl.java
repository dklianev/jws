package org.informatics.winter_olympics.service.impl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.config.ModelMapperConfig;
import org.informatics.winter_olympics.data.entity.Athlete;
import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.data.entity.User;
import org.informatics.winter_olympics.data.repository.AthleteRepository;
import org.informatics.winter_olympics.data.repository.UserRepository;
import org.informatics.winter_olympics.dto.AthleteDto;
import org.informatics.winter_olympics.dto.CreateAthleteDto;
import org.informatics.winter_olympics.exception.AthleteNotFoundException;
import org.informatics.winter_olympics.exception.BusinessRuleException;
import org.informatics.winter_olympics.service.AthleteService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class AthleteServiceImpl implements AthleteService {

    private final AthleteRepository athleteRepository;
    private final ModelMapperConfig modelMapperConfig;
    private final UserRepository userRepository;

    @Override
    public List<AthleteDto> getAthletes() {
        return modelMapperConfig.mapList(athleteRepository.findAll(), AthleteDto.class);
    }

    @Override
    public AthleteDto getAthleteById(long id) {
        return modelMapperConfig.modelMapper().map(athleteRepository.findById(id)
                        .orElseThrow(() -> new AthleteNotFoundException(id)),
                AthleteDto.class);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public AthleteDto createAthlete(@Valid CreateAthleteDto createAthleteDto) {
        Athlete athlete = modelMapperConfig.modelMapper().map(createAthleteDto, Athlete.class);
        return modelMapperConfig.modelMapper().map(athleteRepository.save(athlete), AthleteDto.class);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ATHLETE')")
    public AthleteDto selfRegisterAthlete(@Valid CreateAthleteDto createAthleteDto, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BusinessRuleException("Current user was not found!");
        }
        if (user.getAthlete() != null) {
            throw new BusinessRuleException("Current user already has an athlete profile!");
        }
        Athlete athlete = modelMapperConfig.modelMapper().map(createAthleteDto, Athlete.class);
        Athlete savedAthlete = athleteRepository.save(athlete);
        user.setAthlete(savedAthlete);
        userRepository.save(user);
        return modelMapperConfig.modelMapper().map(savedAthlete, AthleteDto.class);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or @athleteServiceImpl.isAthleteOwner(#id, authentication.name)")
    public AthleteDto updateAthlete(@Valid AthleteDto athleteDto, long id) {
        Athlete updatedAthlete = athleteRepository.findById(id)
                .orElseThrow(() -> new AthleteNotFoundException(id));
        updatedAthlete.setFirstName(athleteDto.getFirstName());
        updatedAthlete.setLastName(athleteDto.getLastName());
        updatedAthlete.setCountry(athleteDto.getCountry());
        updatedAthlete.setGender(athleteDto.getGender());
        updatedAthlete.setDateOfBirth(athleteDto.getDateOfBirth());
        return modelMapperConfig.modelMapper().map(athleteRepository.save(updatedAthlete), AthleteDto.class);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or @athleteServiceImpl.isAthleteOwner(#id, authentication.name)")
    public void deleteAthlete(long id) {
        athleteRepository.deleteById(id);
    }

    public boolean isAthleteOwner(long athleteId, String username) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getAthlete() != null && user.getAthlete().getId() == athleteId;
    }

    @Override
    public List<AthleteDto> findByCountry(String country) {
        return modelMapperConfig.mapList(athleteRepository.findByCountry(country), AthleteDto.class);
    }

    @Override
    public List<AthleteDto> findByGender(Gender gender) {
        return modelMapperConfig.mapList(athleteRepository.findByGender(gender), AthleteDto.class);
    }

    @Override
    public List<AthleteDto> findByCountryAndGender(String country, Gender gender) {
        return modelMapperConfig.mapList(athleteRepository.findByCountryAndGender(country, gender), AthleteDto.class);
    }

    @Override
    public List<AthleteDto> findByDateOfBirthBefore(LocalDate dateOfBirth) {
        return modelMapperConfig.mapList(athleteRepository.findByDateOfBirthBefore(dateOfBirth), AthleteDto.class);
    }

    @Override
    public List<AthleteDto> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate) {
        return modelMapperConfig.mapList(athleteRepository.findByDateOfBirthBetween(startDate, endDate), AthleteDto.class);
    }

    @Override
    public List<AthleteDto> findByLastNameContainingOrderByFirstNameAsc(String text) {
        return modelMapperConfig.mapList(athleteRepository.findByLastNameContainingOrderByFirstNameAsc(text), AthleteDto.class);
    }

    @Override
    public long countByCountry(String country) {
        return athleteRepository.countByCountry(country);
    }
}
