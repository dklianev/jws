package org.informatics.winter_olympics.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Athlete extends BaseEntity {

    @NotBlank
    @Size(min = 2, max = 40, message = "Min 2, Max 40")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 40, message = "Min 2, Max 40")
    private String lastName;

    @NotBlank
    @Size(min = 2, max = 50, message = "Min 2, Max 50")
    private String country;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
}
