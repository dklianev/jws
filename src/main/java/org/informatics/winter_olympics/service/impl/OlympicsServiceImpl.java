package org.informatics.winter_olympics.service.impl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.config.ModelMapperConfig;
import org.informatics.winter_olympics.data.entity.Olympics;
import org.informatics.winter_olympics.data.repository.OlympicsRepository;
import org.informatics.winter_olympics.dto.CreateOlympicsDto;
import org.informatics.winter_olympics.dto.OlympicsDto;
import org.informatics.winter_olympics.exception.OlympicsNotFoundException;
import org.informatics.winter_olympics.service.OlympicsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class OlympicsServiceImpl implements OlympicsService {

    private final OlympicsRepository olympicsRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Override
    public List<OlympicsDto> getOlympics() {
        return modelMapperConfig.mapList(olympicsRepository.findAll(), OlympicsDto.class);
    }

    @Override
    public OlympicsDto getOlympicsById(long id) {
        return modelMapperConfig.modelMapper().map(olympicsRepository.findById(id)
                        .orElseThrow(() -> new OlympicsNotFoundException(id)),
                OlympicsDto.class);
    }

    @Override
    public OlympicsDto createOlympics(@Valid CreateOlympicsDto createOlympicsDto) {
        Olympics olympics = modelMapperConfig.modelMapper().map(createOlympicsDto, Olympics.class);
        return modelMapperConfig.modelMapper().map(olympicsRepository.save(olympics), OlympicsDto.class);
    }

    @Override
    public OlympicsDto updateOlympics(@Valid OlympicsDto olympicsDto, long id) {
        Olympics updatedOlympics = olympicsRepository.findById(id)
                .orElseThrow(() -> new OlympicsNotFoundException(id));
        updatedOlympics.setName(olympicsDto.getName());
        updatedOlympics.setHostCity(olympicsDto.getHostCity());
        updatedOlympics.setCountry(olympicsDto.getCountry());
        updatedOlympics.setStartDate(olympicsDto.getStartDate());
        updatedOlympics.setEndDate(olympicsDto.getEndDate());
        return modelMapperConfig.modelMapper().map(olympicsRepository.save(updatedOlympics), OlympicsDto.class);
    }

    @Override
    public void deleteOlympics(long id) {
        olympicsRepository.deleteById(id);
    }

    @Override
    public List<OlympicsDto> findByCountry(String country) {
        return modelMapperConfig.mapList(olympicsRepository.findByCountry(country), OlympicsDto.class);
    }

    @Override
    public List<OlympicsDto> findByNameContaining(String text) {
        return modelMapperConfig.mapList(olympicsRepository.findByNameContaining(text), OlympicsDto.class);
    }

    @Override
    public List<OlympicsDto> findByStartDateBefore(LocalDate date) {
        return modelMapperConfig.mapList(olympicsRepository.findByStartDateBefore(date), OlympicsDto.class);
    }
}
