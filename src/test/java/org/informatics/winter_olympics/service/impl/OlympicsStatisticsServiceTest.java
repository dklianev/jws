package org.informatics.winter_olympics.service.impl;

import org.informatics.winter_olympics.data.entity.Olympics;
import org.informatics.winter_olympics.data.repository.*;
import org.informatics.winter_olympics.dto.OlympicsStatisticsDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OlympicsStatisticsServiceTest {

    @Mock
    CompetitionRepository competitionRepository;

    @Mock
    OlympicsRepository olympicsRepository;

    @Mock
    SlalomResultRepository slalomResultRepository;

    @Mock
    BiathlonResultRepository biathlonResultRepository;

    @Mock
    SlalomResultServiceImpl slalomResultService;

    @InjectMocks
    RankingServiceImpl rankingService;

    @Test
    void emptyOlympicsStatisticsTest() {
        Olympics olympics = Olympics.builder()
                .name("Beijing 2026")
                .hostCity("Beijing")
                .country("China")
                .startDate(LocalDate.of(2026, 2, 6))
                .endDate(LocalDate.of(2026, 2, 22))
                .build();
        olympics.setId(1L);

        Mockito.when(olympicsRepository.findById(1L)).thenReturn(Optional.of(olympics));
        Mockito.when(competitionRepository.findByOlympicsId(1L)).thenReturn(List.of());
        Mockito.when(slalomResultRepository.findByCompetitionOlympicsId(1L)).thenReturn(List.of());
        Mockito.when(biathlonResultRepository.findByCompetitionOlympicsId(1L)).thenReturn(List.of());

        OlympicsStatisticsDto statistics = rankingService.getOlympicsStatistics(1L);

        assertEquals("Beijing 2026", statistics.getOlympicsName());
        assertEquals(0, statistics.getMedalTable().size());
    }
}
