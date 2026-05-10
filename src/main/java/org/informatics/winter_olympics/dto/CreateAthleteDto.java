package org.informatics.winter_olympics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.informatics.winter_olympics.data.entity.Gender;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAthleteDto {
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
    private Gender gender;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
}
