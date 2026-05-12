package org.informatics.winter_olympics.service;

import org.informatics.winter_olympics.dto.CreateOlympicsDto;
import org.informatics.winter_olympics.dto.OlympicsDto;

import java.time.LocalDate;
import java.util.List;

public interface OlympicsService {
    List<OlympicsDto> getOlympics();

    OlympicsDto getOlympicsById(long id);

    OlympicsDto createOlympics(CreateOlympicsDto createOlympicsDto);

    OlympicsDto updateOlympics(OlympicsDto olympicsDto, long id);

    void deleteOlympics(long id);

    List<OlympicsDto> findByCountry(String country);

    List<OlympicsDto> findByNameContaining(String text);

    List<OlympicsDto> findByStartDateBefore(LocalDate date);
}
