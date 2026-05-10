package org.informatics.winter_olympics.data.repository;

import org.informatics.winter_olympics.data.entity.Olympics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OlympicsRepository extends JpaRepository<Olympics, Long> {
    List<Olympics> findByCountry(String country);

    List<Olympics> findByNameContaining(String text);

    List<Olympics> findByStartDateBefore(LocalDate date);

    List<Olympics> findByHostCityContainingOrderByStartDateAsc(String hostCity);
}
