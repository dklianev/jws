package org.informatics.winter_olympics.data.repository;

import org.informatics.winter_olympics.data.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SlalomResultRepositoryTest {

    @Autowired
    AthleteRepository athleteRepository;

    @Autowired
    OlympicsRepository olympicsRepository;

    @Autowired
    SlalomCompetitionRepository slalomCompetitionRepository;

    @Autowired
    SlalomResultRepository slalomResultRepository;

    @Test
    void findByCompetitionIdAndFirstRunDnfFalseAndFirstRunTimeIsNotNullOrderByFirstRunTimeAsc() {
        Olympics olympics = Olympics.builder()
                .name("Beijing 2026")
                .hostCity("Beijing")
                .country("China")
                .startDate(LocalDate.of(2026, 2, 6))
                .endDate(LocalDate.of(2026, 2, 22))
                .build();
        olympicsRepository.save(olympics);
        SlalomCompetition competition = SlalomCompetition.builder()
                .name("Men Slalom")
                .gender(Gender.MALE)
                .minAge(18)
                .competitionDate(LocalDate.of(2026, 2, 12))
                .olympics(olympics)
                .secondRunCutoff(30)
                .build();
        slalomCompetitionRepository.save(competition);
        Athlete athlete1 = Athlete.builder()
                .firstName("Marco")
                .lastName("Odermatt")
                .country("Switzerland")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1997, 10, 8))
                .build();
        Athlete athlete2 = Athlete.builder()
                .firstName("Henrik")
                .lastName("Kristoffersen")
                .country("Norway")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1994, 7, 2))
                .build();
        athleteRepository.saveAll(List.of(athlete1, athlete2));
        SlalomResult result1 = SlalomResult.builder()
                .competition(competition)
                .athlete(athlete1)
                .firstRunTime(BigDecimal.valueOf(52.120))
                .build();
        SlalomResult result2 = SlalomResult.builder()
                .competition(competition)
                .athlete(athlete2)
                .firstRunTime(BigDecimal.valueOf(51.820))
                .build();

        slalomResultRepository.saveAll(List.of(result1, result2));

        assertEquals(result2, slalomResultRepository
                .findByCompetitionIdAndFirstRunDnfFalseAndFirstRunTimeIsNotNullOrderByFirstRunTimeAsc(competition.getId())
                .getFirst());
    }
}
