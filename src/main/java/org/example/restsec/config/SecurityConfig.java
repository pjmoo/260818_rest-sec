package org.example.restsec.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    // SecurityFilterChain <- 기본값으로 알아서 설정되는 것 대신
    // 내 커스텀으로 넣어주겠다
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        // 이 사이에 http에다가 메서드 체이닝 -> 설정을 주입
        return http
//                .csrf(csrf -> csrf.disable())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**")
                                        .permitAll()
                                .requestMatchers("/chair/**")
                                        .permitAll()
                )
                .build();
    }
}
