package org.informatics.winter_olympics.controller.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ViewControllerSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void homePageRendersForAnonymousUsers() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Winter Olympics Management System")))
                .andExpect(content().string(containsString("Athletes")));
    }

    @Test
    @WithAnonymousUser
    void rankingOverviewRendersForAnonymousUsers() throws Exception {
        mockMvc.perform(get("/rankings"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Rankings")));
    }

    @Test
    @WithAnonymousUser
    void loginPageRendersForAnonymousUsers() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please sign in")));
    }

    @Test
    @WithAnonymousUser
    void anonymousProtectedPageRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/registrations").accept(MediaType.TEXT_HTML))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void adminListPagesRender() throws Exception {
        for (String path : List.of(
                "/olympics",
                "/slalom-competitions",
                "/biathlon-competitions"
        )) {
            mockMvc.perform(get(path))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/athletes"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Create Athlete")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void adminCreateFormsRender() throws Exception {
        for (String path : List.of(
                "/athletes/create-athlete",
                "/olympics/create-olympics",
                "/slalom-competitions/create-slalom-competition",
                "/biathlon-competitions/create-biathlon-competition",
                "/results/slalom/enter-slalom-result",
                "/results/biathlon/enter-biathlon-result"
        )) {
            mockMvc.perform(get(path))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @WithMockUser(username = "athlete1", authorities = "ATHLETE")
    void athleteSelfRegisterFormRenders() throws Exception {
        mockMvc.perform(get("/athletes/self-register"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Register Athlete Profile")));
    }

    @Test
    @WithMockUser(username = "athlete1", authorities = "ATHLETE")
    void athleteCompetitionRegistrationPageRenders() throws Exception {
        mockMvc.perform(get("/registrations"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Register for Competition")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void adminCannotOpenAthleteCompetitionRegistrationPage() throws Exception {
        mockMvc.perform(get("/registrations"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin1", authorities = "ADMIN")
    void logoutRedirectsToHomePage() throws Exception {
        mockMvc.perform(post("/logout").with(csrf()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }
}
