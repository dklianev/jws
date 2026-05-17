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
class BiathlonRankingServiceTest {

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
    void biathlonRankingAddsPenaltySeconds() {
        BiathlonCompetition competition = BiathlonCompetition.builder()
                .name("Women Biathlon")
                .gender(Gender.FEMALE)
                .minAge(18)
                .competitionDate(LocalDate.of(2026, 2, 14))
                .lapsCount(5)
                .shootingStagesCount(4)
                .penaltySecondsPerMiss(BigDecimal.valueOf(60.000))
                .build();
        competition.setId(1L);
        Athlete athlete1 = Athlete.builder()
                .firstName("Julia")
                .lastName("Simon")
                .country("France")
                .gender(Gender.FEMALE)
                .dateOfBirth(LocalDate.of(1996, 10, 9))
                .build();
        athlete1.setId(1L);
        Athlete athlete2 = Athlete.builder()
                .firstName("Lisa")
                .lastName("Vittozzi")
                .country("Italy")
                .gender(Gender.FEMALE)
                .dateOfBirth(LocalDate.of(1995, 2, 4))
                .build();
        athlete2.setId(2L);
        BiathlonResult result1 = BiathlonResult.builder()
                .competition(competition)
                .athlete(athlete1)
                .skiingTimeSeconds(BigDecimal.valueOf(1240.500))
                .missedShots(1)
                .build();
        BiathlonResult result2 = BiathlonResult.builder()
                .competition(competition)
                .athlete(athlete2)
                .skiingTimeSeconds(BigDecimal.valueOf(1230.500))
                .missedShots(0)
                .build();

        Mockito.when(competitionRepository.findById(1L)).thenReturn(Optional.of(competition));
        Mockito.when(biathlonResultRepository.findByCompetitionId(1L)).thenReturn(List.of(result1, result2));

        List<RankingEntryDto> ranking = rankingService.getCompetitionRanking(1L);

        assertEquals("Lisa Vittozzi", ranking.getFirst().getAthleteName());
        assertEquals(MedalType.GOLD, ranking.getFirst().getMedal());
    }
}
