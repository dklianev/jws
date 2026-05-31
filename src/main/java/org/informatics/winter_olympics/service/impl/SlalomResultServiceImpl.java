package org.informatics.winter_olympics.service.impl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.config.ModelMapperConfig;
import org.informatics.winter_olympics.data.entity.Athlete;
import org.informatics.winter_olympics.data.entity.SlalomCompetition;
import org.informatics.winter_olympics.data.entity.SlalomResult;
import org.informatics.winter_olympics.data.entity.User;
import org.informatics.winter_olympics.data.repository.AthleteRepository;
import org.informatics.winter_olympics.data.repository.SlalomCompetitionRepository;
import org.informatics.winter_olympics.data.repository.SlalomResultRepository;
import org.informatics.winter_olympics.data.repository.UserRepository;
import org.informatics.winter_olympics.dto.CreateSlalomResultDto;
import org.informatics.winter_olympics.dto.SlalomResultDto;
import org.informatics.winter_olympics.exception.AthleteNotFoundException;
import org.informatics.winter_olympics.exception.BusinessRuleException;
import org.informatics.winter_olympics.exception.CompetitionNotFoundException;
import org.informatics.winter_olympics.exception.ResultNotFoundException;
import org.informatics.winter_olympics.service.SlalomResultService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class SlalomResultServiceImpl implements SlalomResultService {

    private final SlalomResultRepository slalomResultRepository;
    private final SlalomCompetitionRepository slalomCompetitionRepository;
    private final AthleteRepository athleteRepository;
    private final UserRepository userRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Override
    public List<SlalomResultDto> getSlalomResults() {
        return slalomResultRepository.findAll().stream().map(this::mapResult).toList();
    }

    @Override
    public SlalomResultDto getSlalomResultById(long id) {
        return mapResult(slalomResultRepository.findById(id)
                .orElseThrow(() -> new ResultNotFoundException(id)));
    }

    @Override
    public SlalomResultDto createSlalomResult(@Valid CreateSlalomResultDto resultDto) {
        Athlete athlete = athleteRepository.findById(resultDto.getAthleteId())
                .orElseThrow(() -> new AthleteNotFoundException(resultDto.getAthleteId()));
        SlalomCompetition competition = slalomCompetitionRepository.findById(resultDto.getCompetitionId())
                .orElseThrow(() -> new CompetitionNotFoundException(resultDto.getCompetitionId()));
        validateRegistration(athlete, competition);

        SlalomResult result = SlalomResult.builder()
                .athlete(athlete)
                .competition(competition)
                .firstRunTime(formatTime(resultDto.getFirstRunTime()))
                .firstRunDnf(resultDto.isFirstRunDnf())
                .secondRunTime(formatTime(resultDto.getSecondRunTime()))
                .secondRunDnf(resultDto.isSecondRunDnf())
                .didNotFinish(resultDto.isDidNotFinish() || resultDto.isFirstRunDnf() || resultDto.isSecondRunDnf())
                .build();
        return mapResult(slalomResultRepository.save(result));
    }

    @Override
    public SlalomResultDto registerCurrentAthlete(long competitionId, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null || user.getAthlete() == null) {
            throw new BusinessRuleException("Current user is not linked to an athlete!");
        }
        SlalomCompetition competition = slalomCompetitionRepository.findById(competitionId)
                .orElseThrow(() -> new CompetitionNotFoundException(competitionId));
        validateRegistration(user.getAthlete(), competition);

        SlalomResult result = SlalomResult.builder()
                .athlete(user.getAthlete())
                .competition(competition)
                .didNotFinish(false)
                .build();
        return mapResult(slalomResultRepository.save(result));
    }

    @Override
    public SlalomResultDto updateSlalomResult(@Valid SlalomResultDto resultDto, long id) {
        SlalomResult updatedResult = slalomResultRepository.findById(id)
                .orElseThrow(() -> new ResultNotFoundException(id));
        updatedResult.setFirstRunTime(formatTime(resultDto.getFirstRunTime()));
        updatedResult.setFirstRunDnf(resultDto.isFirstRunDnf());
        updatedResult.setSecondRunTime(formatTime(resultDto.getSecondRunTime()));
        updatedResult.setSecondRunDnf(resultDto.isSecondRunDnf());
        updatedResult.setQualifiedForSecondRun(resultDto.isQualifiedForSecondRun());
        updatedResult.setDidNotFinish(resultDto.isDidNotFinish() || resultDto.isFirstRunDnf() || resultDto.isSecondRunDnf());
        return mapResult(slalomResultRepository.save(updatedResult));
    }

    @Override
    public void deleteSlalomResult(long id) {
        slalomResultRepository.deleteById(id);
    }

    @Override
    public List<SlalomResultDto> findByCompetitionId(Long competitionId) {
        return slalomResultRepository.findByCompetitionId(competitionId).stream().map(this::mapResult).toList();
    }

    @Override
    public List<SlalomResultDto> calculateSecondRunQualifiers(long competitionId) {
        SlalomCompetition competition = slalomCompetitionRepository.findById(competitionId)
                .orElseThrow(() -> new CompetitionNotFoundException(competitionId));
        List<SlalomResult> allResults = slalomResultRepository.findByCompetitionId(competitionId);
        allResults.forEach(result -> result.setQualifiedForSecondRun(false));

        List<SlalomResult> firstRunResults = allResults
                .stream()
                .filter(result -> !result.isFirstRunDnf())
                .filter(result -> result.getFirstRunTime() != null)
                .filter(result -> !result.isDidNotFinish())
                .sorted(Comparator.comparing(SlalomResult::getFirstRunTime))
                .toList();

        for (int i = 0; i < firstRunResults.size(); i++) {
            firstRunResults.get(i).setQualifiedForSecondRun(i < competition.getSecondRunCutoff());
        }
        slalomResultRepository.saveAll(allResults);
        return firstRunResults.stream()
                .filter(SlalomResult::isQualifiedForSecondRun)
                .map(this::mapResult)
                .toList();
    }

    @Override
    public List<SlalomResultDto> getRunTwoStartOrder(long competitionId) {
        calculateSecondRunQualifiers(competitionId);
        return slalomResultRepository.findByCompetitionIdAndQualifiedForSecondRunTrueOrderByFirstRunTimeDesc(competitionId)
                .stream()
                .map(this::mapResult)
                .toList();
    }

    private void validateRegistration(Athlete athlete, SlalomCompetition competition) {
        if (athlete.getGender() != competition.getGender()) {
            throw new BusinessRuleException("Athlete gender does not match the competition!");
        }
        int age = Period.between(athlete.getDateOfBirth(), competition.getCompetitionDate()).getYears();
        if (age < competition.getMinAge()) {
            throw new BusinessRuleException("Athlete is under the minimum age for this competition!");
        }
        if (slalomResultRepository.findByCompetitionIdAndAthleteId(competition.getId(), athlete.getId()).isPresent()) {
            throw new BusinessRuleException("Athlete already has a result for this competition!");
        }
    }

    private SlalomResultDto mapResult(SlalomResult result) {
        SlalomResultDto resultDto = modelMapperConfig.modelMapper().map(result, SlalomResultDto.class);
        resultDto.setAthleteId(result.getAthlete().getId());
        resultDto.setAthleteName(result.getAthlete().getFirstName() + " " + result.getAthlete().getLastName());
        resultDto.setCountry(result.getAthlete().getCountry());
        resultDto.setCompetitionId(result.getCompetition().getId());
        resultDto.setCompetitionName(result.getCompetition().getName());
        resultDto.setTotalTime(result.getTotalTime());
        return resultDto;
    }

    private BigDecimal formatTime(BigDecimal time) {
        if (time == null) {
            return null;
        }
        return time.setScale(3, RoundingMode.HALF_UP);
    }
}
