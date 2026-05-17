package org.informatics.winter_olympics.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.informatics.winter_olympics.dto.SlalomResultDto;
import org.informatics.winter_olympics.service.SlalomResultService;
import org.informatics.winter_olympics.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(SlalomResultApiController.class)
class SlalomResultApiControllerSampleTest {

    @MockitoBean
    private SlalomResultService slalomResultService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createSlalomResultTest() throws Exception {
        SlalomResultDto result = SlalomResultDto.builder()
                .id(1L)
                .athleteId(1L)
                .competitionId(1L)
                .firstRunTime(BigDecimal.valueOf(51.250))
                .secondRunTime(BigDecimal.valueOf(50.500))
                .totalTime(BigDecimal.valueOf(101.750))
                .build();

        Mockito.when(slalomResultService.createSlalomResult(any())).thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/slalom-results")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(result)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTime", is(101.750)));
    }
}
