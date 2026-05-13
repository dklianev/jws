package org.informatics.winter_olympics.service.impl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.config.ModelMapperConfig;
import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.data.entity.Olympics;
import org.informatics.winter_olympics.data.entity.SlalomCompetition;
import org.informatics.winter_olympics.data.repository.OlympicsRepository;
import org.informatics.winter_olympics.data.repository.SlalomCompetitionRepository;
import org.informatics.winter_olympics.dto.CreateSlalomCompetitionDto;
import org.informatics.winter_olympics.dto.SlalomCompetitionDto;
import org.informatics.winter_olympics.exception.CompetitionNotFoundException;
import org.informatics.winter_olympics.exception.OlympicsNotFoundException;
import org.informatics.winter_olympics.service.SlalomCompetitionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class SlalomCompetitionServiceImpl implements SlalomCompetitionService {

    private final SlalomCompetitionRepository slalomCompetitionRepository;
    private final OlympicsRepository olympicsRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Override
    public List<SlalomCompetitionDto> getSlalomCompetitions() {
        return slalomCompetitionRepository.findAll().stream().map(this::mapCompetition).toList();
    }

    @Override
    public SlalomCompetitionDto getSlalomCompetitionById(long id) {
        return mapCompetition(slalomCompetitionRepository.findById(id)
                .orElseThrow(() -> new CompetitionNotFoundException(id)));
    }

    @Override
    public SlalomCompetitionDto createSlalomCompetition(@Valid CreateSlalomCompetitionDto competitionDto) {
        Olympics olympics = olympicsRepository.findById(competitionDto.getOlympicsId())
                .orElseThrow(() -> new OlympicsNotFoundException(competitionDto.getOlympicsId()));
        SlalomCompetition competition = SlalomCompetition.builder()
                .name(competitionDto.getName())
                .gender(competitionDto.getGender())
                .minAge(competitionDto.getMinAge())
                .competitionDate(competitionDto.getCompetitionDate())
                .olympics(olympics)
                .secondRunCutoff(competitionDto.getSecondRunCutoff())
                .build();
        return mapCompetition(slalomCompetitionRepository.save(competition));
    }

    @Override
    public SlalomCompetitionDto updateSlalomCompetition(@Valid SlalomCompetitionDto competitionDto, long id) {
        SlalomCompetition updatedCompetition = slalomCompetitionRepository.findById(id)
                .orElseThrow(() -> new CompetitionNotFoundException(id));
        Olympics olympics = olympicsRepository.findById(competitionDto.getOlympicsId())
                .orElseThrow(() -> new OlympicsNotFoundException(competitionDto.getOlympicsId()));
        updatedCompetition.setName(competitionDto.getName());
        updatedCompetition.setGender(competitionDto.getGender());
        updatedCompetition.setMinAge(competitionDto.getMinAge());
        updatedCompetition.setCompetitionDate(competitionDto.getCompetitionDate());
        updatedCompetition.setOlympics(olympics);
        updatedCompetition.setSecondRunCutoff(competitionDto.getSecondRunCutoff());
        return mapCompetition(slalomCompetitionRepository.save(updatedCompetition));
    }

    @Override
    public void deleteSlalomCompetition(long id) {
        slalomCompetitionRepository.deleteById(id);
    }

    @Override
    public List<SlalomCompetitionDto> findByOlympicsId(Long olympicsId) {
        return slalomCompetitionRepository.findByOlympicsId(olympicsId).stream().map(this::mapCompetition).toList();
    }

    @Override
    public List<SlalomCompetitionDto> findByGender(Gender gender) {
        return slalomCompetitionRepository.findByGender(gender).stream().map(this::mapCompetition).toList();
    }

    @Override
    public List<SlalomCompetitionDto> findByCompetitionDateBefore(LocalDate date) {
        return slalomCompetitionRepository.findByCompetitionDateBefore(date).stream().map(this::mapCompetition).toList();
    }

    private SlalomCompetitionDto mapCompetition(SlalomCompetition competition) {
        SlalomCompetitionDto competitionDto = modelMapperConfig.modelMapper().map(competition, SlalomCompetitionDto.class);
        competitionDto.setOlympicsId(competition.getOlympics().getId());
        competitionDto.setOlympicsName(competition.getOlympics().getName());
        return competitionDto;
    }
}
