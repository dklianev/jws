package org.informatics.winter_olympics.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true
)
@AllArgsConstructor
public class SecurityConfig {
    private final UserService userService;

    @Bean
    public InMemoryUserDetailsManager userDetailsInMemory() {
        UserDetails admin1 = User
                .withUsername("admin1")
                .password(passwordEncoder().encode("admin1"))
                .authorities("ADMIN")
                .build();
        UserDetails athlete1 = User.withUsername("athlete1")
                .password(passwordEncoder().encode("athlete1"))
                .authorities("ATHLETE")
                .build();
        return new InMemoryUserDetailsManager(admin1, athlete1);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/rankings/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rankings/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/medals/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/athletes/self-register").hasAuthority("ATHLETE")
                        .requestMatchers(HttpMethod.POST, "/athletes/self-register").hasAuthority("ATHLETE")
                        .requestMatchers(HttpMethod.POST, "/api/athletes/self-register").hasAuthority("ATHLETE")
                        .requestMatchers("/registrations/**").hasAuthority("ATHLETE")
                        .requestMatchers("/athletes", "/athletes/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/athletes", "/api/athletes/**").hasAuthority("ADMIN")
                        .requestMatchers("/olympics/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/olympics/**").hasAuthority("ADMIN")
                        .requestMatchers("/slalom-competitions/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/slalom-competitions/**").hasAuthority("ADMIN")
                        .requestMatchers("/biathlon-competitions/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/biathlon-competitions/**").hasAuthority("ADMIN")
                        .requestMatchers("/results/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/slalom-results/register/**").hasAuthority("ATHLETE")
                        .requestMatchers(HttpMethod.POST, "/api/biathlon-results/register/**").hasAuthority("ATHLETE")
                        .requestMatchers("/api/slalom-results/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/biathlon-results/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.successHandler((request, response, authentication) -> {
                    String targetUrl = authentication.getAuthorities().stream()
                            .anyMatch(authority -> authority.getAuthority().equals("ATHLETE"))
                            ? "/registrations"
                            : "/athletes";
                    response.sendRedirect(request.getContextPath() + targetUrl);
                }))
                .httpBasic(Customizer.withDefaults())
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
                .exceptionHandling(exception -> exception.accessDeniedHandler((request, response, accessDeniedException) -> {
                    if (request.getRequestURI().startsWith(request.getContextPath() + "/api/")) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    String targetUrl = hasAuthority(authentication, "ATHLETE") ? "/registrations" : "/athletes";
                    response.sendRedirect(request.getContextPath() + targetUrl);
                }))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));
        return http.build();
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(item -> item.getAuthority().equals(authority));
    }
}
