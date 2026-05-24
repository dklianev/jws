package org.informatics.winter_olympics.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.informatics.winter_olympics.config.SecurityConfig;
import org.informatics.winter_olympics.dto.BiathlonResultDto;
import org.informatics.winter_olympics.service.BiathlonResultService;
import org.informatics.winter_olympics.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BiathlonResultApiController.class)
@Import(SecurityConfig.class)
class BiathlonResultApiControllerSampleTest {

    @MockitoBean
    private BiathlonResultService biathlonResultService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createBiathlonResultTest() throws Exception {
        BiathlonResultDto result = BiathlonResultDto.builder()
                .id(1L)
                .athleteId(1L)
                .competitionId(1L)
                .skiingTimeSeconds(BigDecimal.valueOf(1240.500))
                .missedShots(1)
                .finalTime(BigDecimal.valueOf(1300.500))
                .build();

        Mockito.when(biathlonResultService.createBiathlonResult(any())).thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/biathlon-results")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(result)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.missedShots", is(1)));
    }

    @Test
    @WithMockUser(authorities = {"ATHLETE"})
    void athleteCannotCreateBiathlonResultDirectlyTest() throws Exception {
        BiathlonResultDto result = BiathlonResultDto.builder()
                .athleteId(1L)
                .competitionId(1L)
                .skiingTimeSeconds(BigDecimal.valueOf(1240.500))
                .missedShots(1)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/biathlon-results")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(result)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
