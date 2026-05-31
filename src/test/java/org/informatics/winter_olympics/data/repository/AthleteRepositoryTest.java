package org.informatics.winter_olympics.data.repository;

import org.informatics.winter_olympics.data.entity.Athlete;
import org.informatics.winter_olympics.data.entity.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AthleteRepositoryTest {

    @Autowired
    AthleteRepository athleteRepository;

    @Test
    void findByCountry() {
        Athlete athlete1 = Athlete.builder()
                .firstName("Marco")
                .lastName("Odermatt")
                .country("Switzerland")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1997, 10, 8))
                .build();
        Athlete athlete2 = Athlete.builder()
                .firstName("Lara")
                .lastName("Gut")
                .country("Switzerland")
                .gender(Gender.FEMALE)
                .dateOfBirth(LocalDate.of(1991, 4, 27))
                .build();
        Athlete athlete3 = Athlete.builder()
                .firstName("Johannes")
                .lastName("Boe")
                .country("Norway")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1993, 5, 16))
                .build();

        List<Athlete> expectedAthletes = List.of(athlete1, athlete2);
        athleteRepository.saveAll(List.of(athlete1, athlete2, athlete3));

        assertIterableEquals(expectedAthletes, athleteRepository.findByCountry("Switzerland"));
    }

    @Test
    void countByCountry() {
        Athlete athlete1 = Athlete.builder()
                .firstName("Marco")
                .lastName("Odermatt")
                .country("Switzerland")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1997, 10, 8))
                .build();
        Athlete athlete2 = Athlete.builder()
                .firstName("Lara")
                .lastName("Gut")
                .country("Switzerland")
                .gender(Gender.FEMALE)
                .dateOfBirth(LocalDate.of(1991, 4, 27))
                .build();

        athleteRepository.saveAll(List.of(athlete1, athlete2));

        assertEquals(2, athleteRepository.countByCountry("Switzerland"));
    }
}
