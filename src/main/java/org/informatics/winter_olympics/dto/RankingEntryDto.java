package org.informatics.winter_olympics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.informatics.winter_olympics.data.entity.MedalType;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingEntryDto {
    private int position;
    private long athleteId;
    private String athleteName;
    private String country;
    private long competitionId;
    private String competitionName;
    private BigDecimal firstRunTime;
    private BigDecimal secondRunTime;
    private BigDecimal skiingTimeSeconds;
    private Integer missedShots;
    private BigDecimal penaltySeconds;
    private BigDecimal finalTime;
    private MedalType medal;
}
