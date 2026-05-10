package org.informatics.winter_olympics.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "result_type")
@Getter
@Setter
@NoArgsConstructor
public abstract class Result extends BaseEntity {

    @NotNull
    @ManyToOne
    private Athlete athlete;

    @NotNull
    @ManyToOne
    private Competition competition;

    private boolean didNotFinish;

    public Result(Athlete athlete, Competition competition, boolean didNotFinish) {
        this.athlete = athlete;
        this.competition = competition;
        this.didNotFinish = didNotFinish;
    }
}
