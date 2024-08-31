package com.example.MQAdminTemplate.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/assets/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/login", "/registry","/test-front", "/confirm-account", "/forgot-password", "/send-reset-code", "/enter-reset-code", "/verify-reset-code", "/reset-password", "/reset-password", "/auth-lock-screen").permitAll()
                                .requestMatchers(HttpMethod.POST,"/registry", "/confirm-account", "/forgot-password", "/send-reset-code", "/enter-reset-code", "/verify-reset-code", "/reset-password", "/reset-password", "/auth-lock-screen").permitAll()

                                .requestMatchers(HttpMethod.GET,"/admin/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/admin/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/user/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/user/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/admin/admin", true)
                                .failureHandler((request, response, exception) -> {
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.setContentType("application/json; charset=UTF-8");
                                    response.getWriter().write("{\"error\": \"Неверное имя пользователя или пароль\"}");
                                })

                )

                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/login")
                                .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
