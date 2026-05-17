package org.informatics.winter_olympics.data.repository;

import org.informatics.winter_olympics.data.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BiathlonResultRepositoryTest {

    @Autowired
    AthleteRepository athleteRepository;

    @Autowired
    OlympicsRepository olympicsRepository;

    @Autowired
    BiathlonCompetitionRepository biathlonCompetitionRepository;

    @Autowired
    BiathlonResultRepository biathlonResultRepository;

    @Test
    void findByCompetitionId() {
        Olympics olympics = Olympics.builder()
                .name("Beijing 2026")
                .hostCity("Beijing")
                .country("China")
                .startDate(LocalDate.of(2026, 2, 6))
                .endDate(LocalDate.of(2026, 2, 22))
                .build();
        olympicsRepository.save(olympics);
        BiathlonCompetition competition = BiathlonCompetition.builder()
                .name("Women Biathlon")
                .gender(Gender.FEMALE)
                .minAge(18)
                .competitionDate(LocalDate.of(2026, 2, 14))
                .olympics(olympics)
                .lapsCount(5)
                .shootingStagesCount(4)
                .penaltySecondsPerMiss(BigDecimal.valueOf(60.000))
                .build();
        biathlonCompetitionRepository.save(competition);
        Athlete athlete = Athlete.builder()
                .firstName("Julia")
                .lastName("Simon")
                .country("France")
                .gender(Gender.FEMALE)
                .dateOfBirth(LocalDate.of(1996, 10, 9))
                .build();
        athleteRepository.save(athlete);
        BiathlonResult result = BiathlonResult.builder()
                .competition(competition)
                .athlete(athlete)
                .skiingTimeSeconds(BigDecimal.valueOf(1240.500))
                .missedShots(1)
                .build();

        biathlonResultRepository.save(result);

        assertEquals(1, biathlonResultRepository.findByCompetitionId(competition.getId()).size());
    }
}
