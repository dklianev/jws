package org.informatics.winter_olympics.service.impl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.config.ModelMapperConfig;
import org.informatics.winter_olympics.data.entity.Athlete;
import org.informatics.winter_olympics.data.entity.BiathlonCompetition;
import org.informatics.winter_olympics.data.entity.BiathlonResult;
import org.informatics.winter_olympics.data.entity.User;
import org.informatics.winter_olympics.data.repository.AthleteRepository;
import org.informatics.winter_olympics.data.repository.BiathlonCompetitionRepository;
import org.informatics.winter_olympics.data.repository.BiathlonResultRepository;
import org.informatics.winter_olympics.data.repository.UserRepository;
import org.informatics.winter_olympics.dto.BiathlonResultDto;
import org.informatics.winter_olympics.dto.CreateBiathlonResultDto;
import org.informatics.winter_olympics.exception.AthleteNotFoundException;
import org.informatics.winter_olympics.exception.BusinessRuleException;
import org.informatics.winter_olympics.exception.CompetitionNotFoundException;
import org.informatics.winter_olympics.exception.ResultNotFoundException;
import org.informatics.winter_olympics.service.BiathlonResultService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;
import java.util.List;

@Service
@AllArgsConstructor
public class BiathlonResultServiceImpl implements BiathlonResultService {

    private final BiathlonResultRepository biathlonResultRepository;
    private final BiathlonCompetitionRepository biathlonCompetitionRepository;
    private final AthleteRepository athleteRepository;
    private final UserRepository userRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Override
    public List<BiathlonResultDto> getBiathlonResults() {
        return biathlonResultRepository.findAll().stream().map(this::mapResult).toList();
    }

    @Override
    public BiathlonResultDto getBiathlonResultById(long id) {
        return mapResult(biathlonResultRepository.findById(id)
                .orElseThrow(() -> new ResultNotFoundException(id)));
    }

    @Override
    public BiathlonResultDto createBiathlonResult(@Valid CreateBiathlonResultDto resultDto) {
        Athlete athlete = athleteRepository.findById(resultDto.getAthleteId())
                .orElseThrow(() -> new AthleteNotFoundException(resultDto.getAthleteId()));
        BiathlonCompetition competition = biathlonCompetitionRepository.findById(resultDto.getCompetitionId())
                .orElseThrow(() -> new CompetitionNotFoundException(resultDto.getCompetitionId()));
        validateRegistration(athlete, competition);

        BiathlonResult result = BiathlonResult.builder()
                .athlete(athlete)
                .competition(competition)
                .skiingTimeSeconds(formatTime(resultDto.getSkiingTimeSeconds()))
                .missedShots(resultDto.getMissedShots())
                .didNotFinish(resultDto.isDidNotFinish())
                .build();
        return mapResult(biathlonResultRepository.save(result));
    }

    @Override
    public BiathlonResultDto registerCurrentAthlete(long competitionId, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null || user.getAthlete() == null) {
            throw new BusinessRuleException("Current user is not linked to an athlete!");
        }
        BiathlonCompetition competition = biathlonCompetitionRepository.findById(competitionId)
                .orElseThrow(() -> new CompetitionNotFoundException(competitionId));
        validateRegistration(user.getAthlete(), competition);

        BiathlonResult result = BiathlonResult.builder()
                .athlete(user.getAthlete())
                .competition(competition)
                .didNotFinish(false)
                .build();
        return mapResult(biathlonResultRepository.save(result));
    }

    @Override
    public BiathlonResultDto updateBiathlonResult(@Valid BiathlonResultDto resultDto, long id) {
        BiathlonResult updatedResult = biathlonResultRepository.findById(id)
                .orElseThrow(() -> new ResultNotFoundException(id));
        updatedResult.setSkiingTimeSeconds(formatTime(resultDto.getSkiingTimeSeconds()));
        updatedResult.setMissedShots(resultDto.getMissedShots());
        updatedResult.setDidNotFinish(resultDto.isDidNotFinish());
        return mapResult(biathlonResultRepository.save(updatedResult));
    }

    @Override
    public void deleteBiathlonResult(long id) {
        biathlonResultRepository.deleteById(id);
    }

    @Override
    public List<BiathlonResultDto> findByCompetitionId(Long competitionId) {
        return biathlonResultRepository.findByCompetitionId(competitionId).stream().map(this::mapResult).toList();
    }

    private void validateRegistration(Athlete athlete, BiathlonCompetition competition) {
        if (athlete.getGender() != competition.getGender()) {
            throw new BusinessRuleException("Athlete gender does not match the competition!");
        }
        int age = Period.between(athlete.getDateOfBirth(), competition.getCompetitionDate()).getYears();
        if (age < competition.getMinAge()) {
            throw new BusinessRuleException("Athlete is under the minimum age for this competition!");
        }
        if (biathlonResultRepository.findByCompetitionIdAndAthleteId(competition.getId(), athlete.getId()).isPresent()) {
            throw new BusinessRuleException("Athlete already has a result for this competition!");
        }
    }

    private BiathlonResultDto mapResult(BiathlonResult result) {
        BiathlonResultDto resultDto = modelMapperConfig.modelMapper().map(result, BiathlonResultDto.class);
        resultDto.setAthleteId(result.getAthlete().getId());
        resultDto.setAthleteName(result.getAthlete().getFirstName() + " " + result.getAthlete().getLastName());
        resultDto.setCountry(result.getAthlete().getCountry());
        resultDto.setCompetitionId(result.getCompetition().getId());
        resultDto.setCompetitionName(result.getCompetition().getName());
        resultDto.setFinalTime(result.getFinalTime());
        return resultDto;
    }

    private BigDecimal formatTime(BigDecimal time) {
        if (time == null) {
            return null;
        }
        return time.setScale(3, RoundingMode.HALF_UP);
    }
}
