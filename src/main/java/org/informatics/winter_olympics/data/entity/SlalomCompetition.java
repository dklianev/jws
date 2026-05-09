package org.informatics.winter_olympics.data.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("SLALOM")
@Getter
@Setter
@NoArgsConstructor
public class SlalomCompetition extends Competition {

    @Min(value = 1, message = "Second run cutoff has to be greater than 0")
    private int secondRunCutoff;

    @Builder
    public SlalomCompetition(String name, Gender gender, int minAge, LocalDate competitionDate,
                             Olympics olympics, int secondRunCutoff) {
        super(name, gender, minAge, competitionDate, olympics);
        this.secondRunCutoff = secondRunCutoff;
    }
}
