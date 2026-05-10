package org.informatics.winter_olympics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OlympicsStatisticsDto {
    private long olympicsId;
    private String olympicsName;
    private BigDecimal averageAge;
    private MedalistDto youngestMedalist;
    private MedalistDto oldestMedalist;
    private List<MedalCountDto> medalTable;
}
