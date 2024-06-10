package com.example.testsecurityoauthsession20240610.dto;

public interface OAuth2Response {

    // 로그인 기능 구현 회사 이름 naver, kakao, google 같은거
    String getProvider();

    // 제공자에서 발급해주는 Id 번호
    String getProviderId();

    // 이메일
    String getEmail();

    // 사용자 실명
    String getName();
}
