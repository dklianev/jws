package org.informatics.winter_olympics.service.impl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.config.ModelMapperConfig;
import org.informatics.winter_olympics.data.entity.BiathlonCompetition;
import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.data.entity.Olympics;
import org.informatics.winter_olympics.data.repository.BiathlonCompetitionRepository;
import org.informatics.winter_olympics.data.repository.OlympicsRepository;
import org.informatics.winter_olympics.dto.BiathlonCompetitionDto;
import org.informatics.winter_olympics.dto.CreateBiathlonCompetitionDto;
import org.informatics.winter_olympics.exception.CompetitionNotFoundException;
import org.informatics.winter_olympics.exception.OlympicsNotFoundException;
import org.informatics.winter_olympics.service.BiathlonCompetitionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class BiathlonCompetitionServiceImpl implements BiathlonCompetitionService {

    private final BiathlonCompetitionRepository biathlonCompetitionRepository;
    private final OlympicsRepository olympicsRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Override
    public List<BiathlonCompetitionDto> getBiathlonCompetitions() {
        return biathlonCompetitionRepository.findAll().stream().map(this::mapCompetition).toList();
    }

    @Override
    public BiathlonCompetitionDto getBiathlonCompetitionById(long id) {
        return mapCompetition(biathlonCompetitionRepository.findById(id)
                .orElseThrow(() -> new CompetitionNotFoundException(id)));
    }

    @Override
    public BiathlonCompetitionDto createBiathlonCompetition(@Valid CreateBiathlonCompetitionDto competitionDto) {
        Olympics olympics = olympicsRepository.findById(competitionDto.getOlympicsId())
                .orElseThrow(() -> new OlympicsNotFoundException(competitionDto.getOlympicsId()));
        BiathlonCompetition competition = BiathlonCompetition.builder()
                .name(competitionDto.getName())
                .gender(competitionDto.getGender())
                .minAge(competitionDto.getMinAge())
                .competitionDate(competitionDto.getCompetitionDate())
                .olympics(olympics)
                .lapsCount(competitionDto.getLapsCount())
                .shootingStagesCount(competitionDto.getShootingStagesCount())
                .penaltySecondsPerMiss(competitionDto.getPenaltySecondsPerMiss())
                .build();
        return mapCompetition(biathlonCompetitionRepository.save(competition));
    }

    @Override
    public BiathlonCompetitionDto updateBiathlonCompetition(@Valid BiathlonCompetitionDto competitionDto, long id) {
        BiathlonCompetition updatedCompetition = biathlonCompetitionRepository.findById(id)
                .orElseThrow(() -> new CompetitionNotFoundException(id));
        Olympics olympics = olympicsRepository.findById(competitionDto.getOlympicsId())
                .orElseThrow(() -> new OlympicsNotFoundException(competitionDto.getOlympicsId()));
        updatedCompetition.setName(competitionDto.getName());
        updatedCompetition.setGender(competitionDto.getGender());
        updatedCompetition.setMinAge(competitionDto.getMinAge());
        updatedCompetition.setCompetitionDate(competitionDto.getCompetitionDate());
        updatedCompetition.setOlympics(olympics);
        updatedCompetition.setLapsCount(competitionDto.getLapsCount());
        updatedCompetition.setShootingStagesCount(competitionDto.getShootingStagesCount());
        updatedCompetition.setPenaltySecondsPerMiss(competitionDto.getPenaltySecondsPerMiss());
        return mapCompetition(biathlonCompetitionRepository.save(updatedCompetition));
    }

    @Override
    public void deleteBiathlonCompetition(long id) {
        biathlonCompetitionRepository.deleteById(id);
    }

    @Override
    public List<BiathlonCompetitionDto> findByOlympicsId(Long olympicsId) {
        return biathlonCompetitionRepository.findByOlympicsId(olympicsId).stream().map(this::mapCompetition).toList();
    }

    @Override
    public List<BiathlonCompetitionDto> findByGender(Gender gender) {
        return biathlonCompetitionRepository.findByGender(gender).stream().map(this::mapCompetition).toList();
    }

    @Override
    public List<BiathlonCompetitionDto> findByCompetitionDateBefore(LocalDate date) {
        return biathlonCompetitionRepository.findByCompetitionDateBefore(date).stream().map(this::mapCompetition).toList();
    }

    private BiathlonCompetitionDto mapCompetition(BiathlonCompetition competition) {
        BiathlonCompetitionDto competitionDto = modelMapperConfig.modelMapper().map(competition, BiathlonCompetitionDto.class);
        competitionDto.setOlympicsId(competition.getOlympics().getId());
        competitionDto.setOlympicsName(competition.getOlympics().getName());
        return competitionDto;
    }
}
