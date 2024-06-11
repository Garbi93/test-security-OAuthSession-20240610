package com.example.testsecurityoauthsession20240610.service;

import com.example.testsecurityoauthsession20240610.dto.CustomOAuth2User;
import com.example.testsecurityoauthsession20240610.dto.GoogleResponse;
import com.example.testsecurityoauthsession20240610.dto.NaverResponse;
import com.example.testsecurityoauthsession20240610.dto.OAuth2Response;
import com.example.testsecurityoauthsession20240610.entity.UserEntity;
import com.example.testsecurityoauthsession20240610.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    // DB 에 회원 기능을 사용하기 위해 생성자 방식 의존성 주입을 받는다.
    private final UserRepository userRepository;

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

        // 소셜로부터 받은 회원 정보를 DB 에 저장하는 로직 추가 하기
        // 소셜 에서는 우리에게 로그인 ID 값을 따로 제공해주지 않기 때문에 우리가 임의로 로그인 ID 값을 만들어준다.
        // 내생각에는 email 로 하면 될것 같지만 이 강의에서는 로그인ID 와 email 을 다른 컬럼으로 관리하기 때문에 이렇게 하는거 같다.
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        String role = null; //아래에 CustomOAuth2User 에 role 값을 넣어주기 위해

        // 우리 DB 에 해당 회원이 존재하는지 찾아 보기
        UserEntity existData = userRepository.findByUsername(username);

        // 만일 회원 정보가 없는 첫 로그인 이라면 가입 로직 실행시키기
        if (existData == null) {
            UserEntity userEntity = new UserEntity();

            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setRole("ROLE_USER");

            userRepository.save(userEntity);
        } else {

            role = existData.getRole();

            // 만일 첫 로그인이 아닐 경우 존재하면 업데이트 해주기??
            existData.setEmail(oAuth2Response.getEmail());

            userRepository.save(existData);
        }



        // 나머지 구현 후 리턴 해주기

        // role 값의 겨우 social 로그인시 role 값이 없기 때문에 우리가 넣어 주어야 한다.
        return new CustomOAuth2User(oAuth2Response, role);
    }
}
