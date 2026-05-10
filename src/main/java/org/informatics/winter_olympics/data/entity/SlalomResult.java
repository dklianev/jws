package org.informatics.winter_olympics.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("SLALOM")
@Getter
@Setter
@NoArgsConstructor
public class SlalomResult extends Result {

    @Column(precision = 8, scale = 3)
    @DecimalMin(value = "0.000", message = "First run time cannot be negative")
    private BigDecimal firstRunTime;

    private boolean firstRunDnf;

    @Column(precision = 8, scale = 3)
    @DecimalMin(value = "0.000", message = "Second run time cannot be negative")
    private BigDecimal secondRunTime;

    private boolean secondRunDnf;

    private boolean qualifiedForSecondRun;

    @Builder
    public SlalomResult(Athlete athlete, Competition competition, boolean didNotFinish,
                        BigDecimal firstRunTime, boolean firstRunDnf, BigDecimal secondRunTime,
                        boolean secondRunDnf, boolean qualifiedForSecondRun) {
        super(athlete, competition, didNotFinish);
        this.firstRunTime = firstRunTime;
        this.firstRunDnf = firstRunDnf;
        this.secondRunTime = secondRunTime;
        this.secondRunDnf = secondRunDnf;
        this.qualifiedForSecondRun = qualifiedForSecondRun;
    }

    @Transient
    public BigDecimal getTotalTime() {
        if (firstRunTime == null || secondRunTime == null || firstRunDnf || secondRunDnf || isDidNotFinish()) {
            return null;
        }
        return firstRunTime.add(secondRunTime);
    }
}
