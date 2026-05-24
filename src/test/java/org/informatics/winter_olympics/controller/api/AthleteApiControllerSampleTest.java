package org.informatics.winter_olympics.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.informatics.winter_olympics.data.entity.Gender;
import org.informatics.winter_olympics.dto.AthleteDto;
import org.informatics.winter_olympics.config.SecurityConfig;
import org.informatics.winter_olympics.dto.CreateAthleteDto;
import org.informatics.winter_olympics.service.AthleteService;
import org.informatics.winter_olympics.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AthleteApiController.class)
@Import(SecurityConfig.class)
class AthleteApiControllerSampleTest {

    @MockitoBean
    private AthleteService athleteService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createAthleteTest() throws Exception {
        AthleteDto athlete1 = AthleteDto.builder()
                .id(1L)
                .firstName("Marco")
                .lastName("Odermatt")
                .country("Switzerland")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1997, 10, 8))
                .build();

        Mockito.when(athleteService.createAthlete(any())).thenReturn(athlete1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/athletes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(athlete1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country", is(athlete1.getCountry())));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void invalidAthleteReturnsBadRequestTest() throws Exception {
        CreateAthleteDto athlete = CreateAthleteDto.builder()
                .firstName("")
                .lastName("O")
                .country("")
                .gender(null)
                .dateOfBirth(LocalDate.now().plusDays(1))
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/athletes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(athlete)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        Mockito.verify(athleteService, Mockito.never()).createAthlete(any());
    }

    @Test
    @WithMockUser(authorities = {"ATHLETE"})
    void athleteCannotCreateOtherAthleteTest() throws Exception {
        AthleteDto athlete = AthleteDto.builder()
                .firstName("Marco")
                .lastName("Odermatt")
                .country("Switzerland")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1997, 10, 8))
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/athletes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(athlete)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "athlete1", authorities = {"ATHLETE"})
    void athleteCanSelfRegisterProfileTest() throws Exception {
        CreateAthleteDto athlete = CreateAthleteDto.builder()
                .firstName("Marco")
                .lastName("Odermatt")
                .country("Switzerland")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1997, 10, 8))
                .build();
        AthleteDto registeredAthlete = AthleteDto.builder()
                .id(1L)
                .firstName("Marco")
                .lastName("Odermatt")
                .country("Switzerland")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1997, 10, 8))
                .build();

        Mockito.when(athleteService.selfRegisterAthlete(any(), anyString())).thenReturn(registeredAthlete);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/athletes/self-register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(athlete)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithMockUser(authorities = {"ATHLETE"})
    void getAthletesTest() throws Exception {
        AthleteDto athlete1 = AthleteDto.builder()
                .id(1L)
                .firstName("Marco")
                .lastName("Odermatt")
                .country("Switzerland")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1997, 10, 8))
                .build();
        AthleteDto athlete2 = AthleteDto.builder()
                .id(2L)
                .firstName("Lara")
                .lastName("Gut")
                .country("Switzerland")
                .gender(Gender.FEMALE)
                .dateOfBirth(LocalDate.of(1991, 4, 27))
                .build();
        List<AthleteDto> athletesExpected = Arrays.asList(athlete1, athlete2);

        given(athleteService.getAthletes()).willReturn(athletesExpected);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/athletes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(athlete1.getId()), Long.class))
                .andExpect(jsonPath("$[0].dateOfBirth", is("1997-10-08")))
                .andExpect(jsonPath("$[1].id", is(athlete2.getId()), Long.class))
                .andExpect(jsonPath("$[1].dateOfBirth", is("1991-04-27")))
                .andDo(print());
    }
}
