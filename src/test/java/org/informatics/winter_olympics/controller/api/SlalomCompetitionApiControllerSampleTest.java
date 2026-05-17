package org.informatics.winter_olympics.controller.api;

import org.informatics.winter_olympics.config.SecurityConfig;
import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.dto.SlalomCompetitionDto;
import org.informatics.winter_olympics.service.SlalomCompetitionService;
import org.informatics.winter_olympics.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SlalomCompetitionApiController.class)
@Import(SecurityConfig.class)
class SlalomCompetitionApiControllerSampleTest {

    @MockitoBean
    private SlalomCompetitionService slalomCompetitionService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void getSlalomCompetitionsTest() throws Exception {
        SlalomCompetitionDto competition = SlalomCompetitionDto.builder()
                .id(1L)
                .name("Men Slalom")
                .gender(Gender.MALE)
                .minAge(18)
                .competitionDate(LocalDate.of(2026, 2, 12))
                .olympicsName("Beijing 2026")
                .secondRunCutoff(30)
                .build();

        given(slalomCompetitionService.getSlalomCompetitions()).willReturn(List.of(competition));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/slalom-competitions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].secondRunCutoff", is(30)))
                .andDo(print());
    }
}
