package org.informatics.winter_olympics.controller.api;

import org.informatics.winter_olympics.config.SecurityConfig;
import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.dto.BiathlonCompetitionDto;
import org.informatics.winter_olympics.service.BiathlonCompetitionService;
import org.informatics.winter_olympics.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BiathlonCompetitionApiController.class)
@Import(SecurityConfig.class)
class BiathlonCompetitionApiControllerSampleTest {

    @MockitoBean
    private BiathlonCompetitionService biathlonCompetitionService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void getBiathlonCompetitionsTest() throws Exception {
        BiathlonCompetitionDto competition = BiathlonCompetitionDto.builder()
                .id(1L)
                .name("Women Biathlon")
                .gender(Gender.FEMALE)
                .minAge(18)
                .competitionDate(LocalDate.of(2026, 2, 14))
                .olympicsName("Beijing 2026")
                .lapsCount(5)
                .shootingStagesCount(4)
                .penaltySecondsPerMiss(BigDecimal.valueOf(60.000))
                .build();

        given(biathlonCompetitionService.getBiathlonCompetitions()).willReturn(List.of(competition));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/biathlon-competitions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lapsCount", is(5)))
                .andDo(print());
    }
}
