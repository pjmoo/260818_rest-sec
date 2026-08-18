package org.example.restsec.config;

import org.example.restsec.auth.RestAccessDeniedHandler;
import org.example.restsec.auth.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    // SecurityFilterChain <- 기본값으로 알아서 설정되는 것 대신
    // 내 커스텀으로 넣어주겠다
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 이 사이에 http에다가 메서드 체이닝 -> 설정을 주입
        return http
//                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(Customizer.withDefaults())
                .httpBasic(
                        basic -> basic
                                .authenticationEntryPoint(new RestAuthenticationEntryPoint())

                )
                .exceptionHandling(
                        ex -> ex
                                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                                .accessDeniedHandler(new RestAccessDeniedHandler())
                )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                                        "/", "/index.html",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**")
                                        .permitAll()
//                                .requestMatchers("/chair/**")
                                .requestMatchers( HttpMethod.GET,"/chair/**")
                                        .permitAll()
                                .requestMatchers( HttpMethod.POST,"/chair/**")
                                    .authenticated()
                                .requestMatchers( HttpMethod.DELETE,"/chair/**")
                                    .hasRole("ADMIN")
                )
                .build();
    }
}
