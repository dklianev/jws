package org.informatics.winter_olympics.data.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("BIATHLON")
@Getter
@Setter
@NoArgsConstructor
public class BiathlonCompetition extends Competition {

    @Min(value = 1, message = "Laps count has to be greater than 0")
    private int lapsCount;

    @Min(value = 0, message = "Shooting stages count cannot be negative")
    private int shootingStagesCount;

    @DecimalMin(value = "0.0", message = "Penalty seconds cannot be negative")
    private BigDecimal penaltySecondsPerMiss;

    @Builder
    public BiathlonCompetition(String name, Gender gender, int minAge, LocalDate competitionDate,
                               Olympics olympics, int lapsCount, int shootingStagesCount,
                               BigDecimal penaltySecondsPerMiss) {
        super(name, gender, minAge, competitionDate, olympics);
        this.lapsCount = lapsCount;
        this.shootingStagesCount = shootingStagesCount;
        this.penaltySecondsPerMiss = penaltySecondsPerMiss;
    }
}
