package org.informatics.winter_olympics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedalCountDto {
    private String country;
    private long goldMedals;
    private long silverMedals;
    private long bronzeMedals;
    private long totalMedals;
}
