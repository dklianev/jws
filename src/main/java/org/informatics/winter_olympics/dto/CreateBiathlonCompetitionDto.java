package org.informatics.winter_olympics.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.informatics.winter_olympics.data.entity.Gender;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBiathlonCompetitionDto {
    @NotBlank
    private String name;

    @NotNull
    private Gender gender;

    @Min(value = 0, message = "Minimum age cannot be negative")
    private int minAge;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate competitionDate;

    @Min(value = 1, message = "Olympics id is required")
    private long olympicsId;

    @Min(value = 1, message = "Laps count has to be greater than 0")
    private int lapsCount;

    @Min(value = 0, message = "Shooting stages count cannot be negative")
    private int shootingStagesCount;

    @NotNull
    @DecimalMin(value = "0.0", message = "Penalty seconds cannot be negative")
    private BigDecimal penaltySecondsPerMiss;
}
