package com.universe.wujuplay.config.oauth;

import com.universe.wujuplay.config.exception.DuplicateEmailException;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {


    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsService userDetailsService;

    @Autowired
    public PrincipalOauth2UserService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsService = userDetailsService;
    }

    //구글로 부터 받은 userRequest데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //구글 로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> 코드를 리턴(0ath-client library) -> access token 요청
        //userRequest 정보 -> loadUser함수 호출 -> 회원정보 받기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글 사용자의 정보 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println("attributes = " + attributes);

        String accountEmail = (String) attributes.get("accountEmail");
        String platformType = userRequest.getClientRegistration().getClientId(); //Google

        Optional<MemberEntity> existingMember = memberRepository.findByAccountEmail(accountEmail);
        if (existingMember.isPresent()) {
            // 중복된 이메일이 이미 존재하는 경우 에러 처리
            throw new DuplicateEmailException("Email address is already registered.");
        }
        return super.loadUser(userRequest);
    }
}
