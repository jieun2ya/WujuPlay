package com.universe.wujuplay.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universe.wujuplay.config.jwt.OAuthToken;
import com.universe.wujuplay.config.jwt.TokenProvider;
import com.universe.wujuplay.member.model.*;
import com.universe.wujuplay.member.model.MemberTokenInfo.TokenInfo;
import com.universe.wujuplay.member.repository.MemberDetailRepository;
import com.universe.wujuplay.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final TokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String infoUri;

    public MemberEntity register(MemberRequest memberRequest) {
        MemberEntity memberEntity = MemberRequest.toEntity(memberRequest);
        String encodedPassword = pwEncoder.encode(memberEntity.getPassword());
        memberEntity.setPassword(encodedPassword);
        memberEntity.setPlatformType("WUJUPLAY");
        memberEntity.setRole("ROLE_USER");
        return memberRepository.save(memberEntity);
    }

    public MemberEntity saveKakao(MemberKakaoRegisterDTO dto) {
        MemberEntity memberEntity = MemberKakaoRegisterDTO.toEntity(dto);
        UUID garbagePassword = UUID.randomUUID();
        String encodedPassword = pwEncoder.encode(garbagePassword.toString());
        memberEntity.setPassword(encodedPassword);
        memberEntity.setPlatformType("KAKAO");
        memberEntity.setRole("ROLE_USER");
        return memberRepository.save(memberEntity);
    }

    public TokenInfo kakaoLogin(String code, String accessToken, KakaoProfile kakaoProfile, HttpServletResponse response) {
        Long socialAuthId = kakaoProfile.getId();
        Optional<MemberEntity> member = memberRepository.findBySocialAuthId(socialAuthId);
        MemberEntity memberEntity = member.orElseThrow(() -> new UsernameNotFoundException("User not found with socialAuthId: " + socialAuthId));
        CustomMemberDetails memberDetails = new CustomMemberDetails(
                memberEntity.getMemberId(),
                memberEntity.getAccountEmail(),
                "", // 카카오 로그인에서는 비밀번호가 없으므로 빈 문자열 전달
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        // 응답에 토큰 쿠키 추가
        addTokenCookiesToResponse(tokenInfo, response);
        return tokenInfo;
    }

    public String requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        //Header Object 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //Body Object 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        //header and body를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        //HTTP 요청하기 - POST방식으로 - 그리고 response변수의 응답 받음
        ResponseEntity<String> response = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return oAuthToken.getAccess_token();
    }

    public KakaoProfile requestUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);
        //HTTP 요청하기 - POST방식으로 - 그리고 response변수의 응답 받음
        ResponseEntity<String> response = restTemplate.exchange(
                infoUri,
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return kakaoProfile;
    }

    public TokenInfo login(MemberLoginDTO memberLoginDTO, HttpServletResponse response) {
        //1. dto를 통해 시큐리티 인증 등록
        Authentication authentication = authenticateUser(memberLoginDTO);
        //2. 토큰 발급
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        //3. 쿠키에 저장
        addTokenCookiesToResponse(tokenInfo, response);
        return tokenInfo;
    }

    private Authentication authenticateUser(MemberLoginDTO memberLoginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = memberLoginDTO.toAuthentication();
        try {
            return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (Exception e) {
            throw new AuthenticationServiceException("이메일 또는 비밀번호가 틀렸습니다.");
        }
    }

    private void addTokenCookiesToResponse(TokenInfo tokenInfo, HttpServletResponse response) {
        Cookie accessTokenCookie = createCookie("accessToken", tokenInfo.getAccessToken(), 30 * 60 * 1000L);
        response.addCookie(accessTokenCookie);
    }

    private Cookie createCookie(String name, String value, Long maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge.intValue());
        return cookie;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            deleteCookies(request, response, "accessToken");
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                String memberEmail = userDetails.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                String email = (String) authentication.getPrincipal();
            } else {
                throw new IllegalStateException("Unexpected type of principal object");
            }
        }
    }

    private void deleteCookies(HttpServletRequest request, HttpServletResponse response, String... cookieNames) {
        String contextPath = request.getContextPath(); // 현재 웹 애플리케이션의 컨텍스트 경로
        String cookiePath = contextPath.isEmpty() ? "/" : contextPath;

        Arrays.asList(cookieNames).forEach(cookieName -> {
            Cookie cookie = new Cookie(cookieName, null);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            cookie.setPath(cookiePath); // 동적으로 경로 설정
            cookie.setMaxAge(0); // 쿠키를 즉시 만료시킵니다.
            response.addCookie(cookie);
        });
    }

    public Long getMemberIdFromAccessTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return jwtTokenProvider.getMemberIdFromToken(cookie.getValue());
                }
            }
        }

        return null;
    }


    public boolean accountEmailExist(String accountEmail) {
        Optional<MemberEntity> member1 = memberRepository.findByAccountEmail(accountEmail);
        return member1.isEmpty();
    }


    public boolean profileNicknameExist(String profileNickname) {
        Optional<MemberEntity> member = memberRepository.findByProfileNickname(profileNickname);
        return member.isPresent();
    }


    public MemberEntity findById(Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + memberId));
        return memberEntity;
    }

    public MemberDetailEntity findDetailByMemberId(Long memberId) {
        Optional<MemberDetailEntity> optionalMemberDetailEntity = memberDetailRepository.findByMemberEntity_MemberId(memberId);
        return optionalMemberDetailEntity.orElse(null);
    }

    public void saveMemberDetail(MemberDetailDTO dto) {
        MemberDetailEntity memberDetail = MemberDetailDTO.toDetailEntity(dto);
        memberDetailRepository.save(memberDetail); // MemberEntity를 저장
    }

    @Transactional
    public void update(Long memberId, MemberResponse memberResponse) {

        MemberEntity updateMemberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + memberId));
        updateMemberEntity.update(memberResponse.getName(), memberResponse.getProfileNickname(), memberResponse.getGender(), memberResponse.getAddress());
    }

    @Transactional
    public void updateDetail(Long memberId, MemberDetailDTO memberDetailDTO) {

        MemberDetailEntity updateMemberDetailEntity = memberDetailRepository.findByMemberEntity_MemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + memberId));
        updateMemberDetailEntity.update(memberDetailDTO.getPhone(), memberDetailDTO.getMbti(), memberDetailDTO.getSportsCareer(), memberDetailDTO.getInterestSports());

    }
}







