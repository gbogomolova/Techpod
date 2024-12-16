package com.example.techpod;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Отключение CSRF для REST API
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/about","/home", "/register", "/login", "/error", "/api/users").permitAll() // Доступ для всех
                        .requestMatchers("/api/troubles/statistics", "/api/user/role", "/api/users/role").permitAll() //  API
                        .requestMatchers("/edit/**", "/delete/**", "/manage_users").hasRole("ADMIN") // Доступ только для админа
                        .requestMatchers("/index", "/histogram", "/new").hasAnyRole("USER", "ADMIN") // Доступ для пользователей и админов
                        .anyRequest().authenticated() // Все запросы по авторизации
                )
                .formLogin(form -> form
                        .loginPage("/login") // Страница входа
                        .loginProcessingUrl("/api/login") // URL обработки логина
                        .usernameParameter("username") // Поле имени пользователя
                        .passwordParameter("password") // Поле пароля
                        .defaultSuccessUrl("/index", true) // Перенаправление после успешного входа
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"message\": \"Успех! Вы успешно вошли в систему.\"}"); // Уведомление
                        })
                        .failureUrl("/login?error=true") // Перенаправление при ошибке входа
                        .permitAll()
                        .failureHandler((request, response, exception) -> {
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("text/plain; charset=UTF-8");
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.getWriter().write("Ошибка авторизации: Неверное имя пользователя или пароль.");
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL выхода
                        .logoutSuccessUrl("/login?logout=true") // Перенаправление после выхода
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID") // Очистка сессии и кук
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // Возвращаем 401
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("Доступ запрещен: " + accessDeniedException.getMessage());
                            response.setStatus(HttpStatus.FORBIDDEN.value()); // Возвращаем 403
                        })
                );

        return http.build();
    }
}
