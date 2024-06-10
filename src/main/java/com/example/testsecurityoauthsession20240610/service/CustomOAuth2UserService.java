package com.example.testsecurityoauthsession20240610.service;

import com.example.testsecurityoauthsession20240610.dto.GoogleResponse;
import com.example.testsecurityoauthsession20240610.dto.NaverResponse;
import com.example.testsecurityoauthsession20240610.dto.OAuth2Response;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        // naver 인지 google 인지 kakao 인지 인증 프로그램 이름을 받아오기 위해 사용
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // naver 와 google DTO 가 구현하는 OAUth2Response 형식으로 바구니를 만들어준다.
        OAuth2Response oAuth2Response = null;
        // 이렇게 나누는 이유는 각 회사마다 나누어 주는 인증 규격이 다르기 때문
        if (registrationId.equals("naver")) {
            // naver 일 경우 로직 -> naver 관련 기능만 구현 함
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            // google 일 경우 로직 -> 우리는 구현 하지 않음
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        // 나머지 구현 후 리턴 해주기
    }
}
