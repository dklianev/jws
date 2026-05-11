package org.informatics.winter_olympics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.informatics.winter_olympics.data.entity.MedalType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedalistDto {
    private String name;
    private String country;
    private int age;
    private MedalType medal;
    private String competitionName;
}
