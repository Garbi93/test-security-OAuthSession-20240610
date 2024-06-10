package com.example.testsecurityoauthsession20240610.config;

import com.example.testsecurityoauthsession20240610.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // 우리가 만든 OAuth2 요청을 생성자 주입 받아준다.
    private final CustomOAuth2UserService customOAuth2UserService;

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
        // 우리가 설정한 OAuth2 기능을 넣어준다.
        http
                .oauth2Login((oauth2)-> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) ->
                                userInfoEndpointConfig.userService(customOAuth2UserService)));

        // 경로별 권한 설정해주기
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated());


        return http.build();
    }
}
