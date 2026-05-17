package org.informatics.winter_olympics.service.impl;

import org.informatics.winter_olympics.config.ModelMapperConfig;
import org.informatics.winter_olympics.data.entity.Athlete;
import org.informatics.winter_olympics.data.repository.AthleteRepository;
import org.informatics.winter_olympics.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class AthleteServiceImplSampleTest {

    @Mock
    Athlete athlete1;

    @Mock
    Athlete athlete2;

    @Mock
    AthleteRepository athleteRepository;

    @Mock
    ModelMapperConfig modelMapperConfig;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AthleteServiceImpl athleteService;

    @Test
    void athleteServiceGetAthletesTest() {
        Mockito.when(athlete1.getFirstName()).thenReturn("Marco");
        Mockito.when(athlete2.getFirstName()).thenReturn("Lara");
        Mockito.when(athleteRepository.findAll()).thenReturn(List.of(athlete1, athlete2));
        Mockito.when(modelMapperConfig.mapList(any(), any())).thenReturn(List.of(athlete1, athlete2));

        assertEquals(2, athleteService.getAthletes().size());
    }
}
