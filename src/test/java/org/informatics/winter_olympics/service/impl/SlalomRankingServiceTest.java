package org.informatics.winter_olympics.service.impl;

import org.informatics.winter_olympics.data.entity.*;
import org.informatics.winter_olympics.data.repository.*;
import org.informatics.winter_olympics.dto.RankingEntryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class SlalomRankingServiceTest {

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
    void slalomRankingOrdersByTotalTime() {
        SlalomCompetition competition = SlalomCompetition.builder()
                .name("Men Slalom")
                .gender(Gender.MALE)
                .minAge(18)
                .competitionDate(LocalDate.of(2026, 2, 12))
                .secondRunCutoff(30)
                .build();
        competition.setId(1L);
        Athlete athlete1 = Athlete.builder()
                .firstName("Marco")
                .lastName("Odermatt")
                .country("Switzerland")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1997, 10, 8))
                .build();
        athlete1.setId(1L);
        Athlete athlete2 = Athlete.builder()
                .firstName("Henrik")
                .lastName("Kristoffersen")
                .country("Norway")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1994, 7, 2))
                .build();
        athlete2.setId(2L);
        SlalomResult result1 = SlalomResult.builder()
                .competition(competition)
                .athlete(athlete1)
                .firstRunTime(BigDecimal.valueOf(52.000))
                .secondRunTime(BigDecimal.valueOf(51.000))
                .build();
        SlalomResult result2 = SlalomResult.builder()
                .competition(competition)
                .athlete(athlete2)
                .firstRunTime(BigDecimal.valueOf(51.000))
                .secondRunTime(BigDecimal.valueOf(50.000))
                .build();

        Mockito.when(competitionRepository.findById(1L)).thenReturn(Optional.of(competition));
        Mockito.when(slalomResultRepository.findByCompetitionId(1L)).thenReturn(List.of(result1, result2));

        List<RankingEntryDto> ranking = rankingService.getCompetitionRanking(1L);

        assertEquals("Henrik Kristoffersen", ranking.getFirst().getAthleteName());
        assertEquals(MedalType.GOLD, ranking.getFirst().getMedal());
    }
}
