package org.informatics.winter_olympics.controller.api;

import org.informatics.winter_olympics.config.SecurityConfig;
import org.informatics.winter_olympics.dto.OlympicsDto;
import org.informatics.winter_olympics.service.OlympicsService;
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

@WebMvcTest(OlympicsApiController.class)
@Import(SecurityConfig.class)
class OlympicsApiControllerSampleTest {

    @MockitoBean
    private OlympicsService olympicsService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void getOlympicsTest() throws Exception {
        OlympicsDto olympics = OlympicsDto.builder()
                .id(1L)
                .name("Beijing 2026")
                .hostCity("Beijing")
                .country("China")
                .startDate(LocalDate.of(2026, 2, 6))
                .endDate(LocalDate.of(2026, 2, 22))
                .build();

        given(olympicsService.getOlympics()).willReturn(List.of(olympics));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/olympics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Beijing 2026")))
                .andDo(print());
    }
}
