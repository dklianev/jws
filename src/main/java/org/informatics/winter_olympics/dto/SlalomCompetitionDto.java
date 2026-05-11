package org.informatics.winter_olympics.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class SlalomCompetitionDto {
    private long id;

    @NotBlank
    private String name;

    @NotNull
    private Gender gender;

    @Min(value = 0, message = "Minimum age cannot be negative")
    private int minAge;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate competitionDate;

    private long olympicsId;
    private String olympicsName;

    @Min(value = 1, message = "Second run cutoff has to be greater than 0")
    private int secondRunCutoff;
}
