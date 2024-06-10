package com.example.testsecurityoauthsession20240610.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 개발 환경이기 때문에 csrf 설정 끄기,
        http
                .csrf((csrf) -> csrf.disable());

        // formLogin 방식과, httpBasic 방식도 이번에는 사용하지 안을예정
        http
                .formLogin((login) -> login.disable());
        http
                .httpBasic((basic) -> basic.disable());

        // oauth2 설정
        // 추후에 디테일하게 람다식으로 구성 예정
        http
                .oauth2Login(Customizer.withDefaults());

        // 경로별 권한 설정해주기
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated());


        return http.build();
    }
}
