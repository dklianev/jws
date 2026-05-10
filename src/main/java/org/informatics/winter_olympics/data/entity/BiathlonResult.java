package org.informatics.winter_olympics.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("BIATHLON")
@Getter
@Setter
@NoArgsConstructor
public class BiathlonResult extends Result {

    @Column(precision = 8, scale = 3)
    @DecimalMin(value = "0.000", message = "Skiing time cannot be negative")
    private BigDecimal skiingTimeSeconds;

    @Min(value = 0, message = "Missed shots cannot be negative")
    private int missedShots;

    @Builder
    public BiathlonResult(Athlete athlete, Competition competition, boolean didNotFinish,
                          BigDecimal skiingTimeSeconds, int missedShots) {
        super(athlete, competition, didNotFinish);
        this.skiingTimeSeconds = skiingTimeSeconds;
        this.missedShots = missedShots;
    }

    @Transient
    public BigDecimal getFinalTime() {
        if (skiingTimeSeconds == null || isDidNotFinish()) {
            return null;
        }
        BiathlonCompetition biathlonCompetition = (BiathlonCompetition) getCompetition();
        return skiingTimeSeconds.add(biathlonCompetition.getPenaltySecondsPerMiss().multiply(BigDecimal.valueOf(missedShots)));
    }
}
