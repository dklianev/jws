package org.informatics.winter_olympics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlalomResultDto {
    private long id;
    private long athleteId;
    private String athleteName;
    private String country;
    private long competitionId;
    private String competitionName;
    private BigDecimal firstRunTime;
    private boolean firstRunDnf;
    private BigDecimal secondRunTime;
    private boolean secondRunDnf;
    private boolean qualifiedForSecondRun;
    private boolean didNotFinish;
    private BigDecimal totalTime;
}
