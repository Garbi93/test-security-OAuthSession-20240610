package com.example.testsecurityoauthsession20240610.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    // oAuth2Response 과 role 을 생성자 주입 받아와 사용한다.
    private final OAuth2Response oAuth2Response;
    private final String role;


    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role;
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return oAuth2Response.getName();
    }

    // 소셜 로그인의 경우 회원 아이디를 특정하지 않기 때문에 만들어 준다고 한다.
    // 해당 데이터로 아이디 값을 생성해 주려고 한다고 한다.
    public String getUsername() {
        return oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
    }
}
