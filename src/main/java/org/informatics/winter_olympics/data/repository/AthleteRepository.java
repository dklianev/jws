package org.informatics.winter_olympics.data.repository;

import org.informatics.winter_olympics.data.entity.Athlete;
import org.informatics.winter_olympics.data.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    List<Athlete> findByCountry(String country);

    List<Athlete> findByGender(Gender gender);

    List<Athlete> findByCountryAndGender(String country, Gender gender);

    List<Athlete> findByDateOfBirthBefore(LocalDate dateOfBirth);

    List<Athlete> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);

    List<Athlete> findByLastNameContainingOrderByFirstNameAsc(String text);

    long countByCountry(String country);
}
