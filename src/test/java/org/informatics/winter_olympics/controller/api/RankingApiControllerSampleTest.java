package org.informatics.winter_olympics.controller.api;

import org.informatics.winter_olympics.config.SecurityConfig;
import org.informatics.winter_olympics.data.entity.MedalType;
import org.informatics.winter_olympics.dto.RankingEntryDto;
import org.informatics.winter_olympics.service.RankingService;
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
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RankingApiController.class)
@Import(SecurityConfig.class)
class RankingApiControllerSampleTest {

    @MockitoBean
    private RankingService rankingService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = {"ATHLETE"})
    void getCompetitionRankingTest() throws Exception {
        RankingEntryDto row = RankingEntryDto.builder()
                .position(1)
                .athleteName("Marco Odermatt")
                .country("Switzerland")
                .competitionName("Men Slalom")
                .finalTime(BigDecimal.valueOf(101.750))
                .medal(MedalType.GOLD)
                .build();

        given(rankingService.getCompetitionRanking(1L)).willReturn(List.of(row));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/rankings/competition/{competitionId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].position", is(1)))
                .andExpect(jsonPath("$[0].medal", is("GOLD")))
                .andDo(print());
    }

    @Test
    void publicRankingIsAccessibleWithoutLoginTest() throws Exception {
        RankingEntryDto row = RankingEntryDto.builder()
                .position(1)
                .athleteName("Marco Odermatt")
                .country("Switzerland")
                .competitionName("Men Slalom")
                .finalTime(BigDecimal.valueOf(101.750))
                .medal(MedalType.GOLD)
                .build();

        given(rankingService.getCompetitionRanking(1L)).willReturn(List.of(row));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/rankings/competition/{competitionId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].position", is(1)))
                .andDo(print());
    }
}
