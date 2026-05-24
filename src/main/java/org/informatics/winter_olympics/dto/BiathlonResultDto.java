package org.informatics.winter_olympics.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiathlonResultDto {
    private long id;
    private long athleteId;
    private String athleteName;
    private String country;
    private long competitionId;
    private String competitionName;

    @DecimalMin(value = "0.001", message = "Time has to be greater than 0")
    private BigDecimal skiingTimeSeconds;

    @Min(value = 0, message = "Missed shots cannot be negative")
    private int missedShots;

    private boolean didNotFinish;
    private BigDecimal finalTime;
}
