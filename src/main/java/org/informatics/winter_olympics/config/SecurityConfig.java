package org.informatics.winter_olympics.config;

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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests
                        (
                                authz -> authz
                                        .requestMatchers("/", "/css/**", "/js/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/rankings/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/rankings/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/medals/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/athletes/**").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/api/athletes/**").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/athletes/self-register").hasAuthority("ATHLETE")
                                        .requestMatchers(HttpMethod.POST, "/api/athletes/self-register").hasAuthority("ATHLETE")
                                        .requestMatchers(HttpMethod.POST, "/athletes/create").hasAuthority("ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/api/athletes").hasAuthority("ADMIN")
                                        .requestMatchers("/athletes/**").hasAnyAuthority("ADMIN", "ATHLETE")
                                        .requestMatchers("/api/athletes/**").hasAnyAuthority("ADMIN", "ATHLETE")
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
                .httpBasic(Customizer.withDefaults())
        ;
        return http.build();
    }
}
