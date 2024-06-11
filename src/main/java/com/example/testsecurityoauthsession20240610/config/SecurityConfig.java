package com.example.testsecurityoauthsession20240610.config;

import com.example.testsecurityoauthsession20240610.oauth2.CustomClientRegistrationRepo;
import com.example.testsecurityoauthsession20240610.oauth2.CustomOAuth2AuthorizedClientService;
import com.example.testsecurityoauthsession20240610.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
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

    private final CustomClientRegistrationRepo customClientRegistrationRepo;

    private final CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService;
    private final JdbcTemplate jdbcTemplate;

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
                        .loginPage("/login") // 커스텀 로그인 페이지 등록
                        .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository()) // 우리가 커스텀 하여 만든 OAuth 방식 작동 시키는 것 인식
                        .authorizedClientService(customOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate, customClientRegistrationRepo.clientRegistrationRepository())) // 인 메모리 방식이 아닌 jdbc 를 이용한 DB 저장 방식을 사용
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
