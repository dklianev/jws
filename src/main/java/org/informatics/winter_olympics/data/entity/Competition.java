package org.informatics.winter_olympics.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discipline")
@Getter
@Setter
@NoArgsConstructor
public abstract class Competition extends BaseEntity {

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Min(value = 0, message = "Minimum age cannot be negative")
    private int minAge;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate competitionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Olympics olympics;

    @OneToMany(mappedBy = "competition")
    @JsonIgnore
    private Set<Result> results;

    public Competition(String name, Gender gender, int minAge, LocalDate competitionDate, Olympics olympics) {
        this.name = name;
        this.gender = gender;
        this.minAge = minAge;
        this.competitionDate = competitionDate;
        this.olympics = olympics;
    }
}
