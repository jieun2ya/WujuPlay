package com.universe.wujuplay.member.controller;

import com.universe.wujuplay.config.jwt.TokenProvider;
import com.universe.wujuplay.config.jwt.TokenUtils;
import com.universe.wujuplay.member.model.*;
import com.universe.wujuplay.member.model.MemberTokenInfo.TokenInfo;
import com.universe.wujuplay.member.repository.MemberRepository;
import com.universe.wujuplay.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/auth")
@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider jwtTokenProvider;

    @GetMapping("/signup")
    public String showSignUp() {
        return "auth/signUp";
    }

    @PostMapping("/signup")
    public String signUp(@Validated MemberRequest memberRequest) {
        memberService.register(memberRequest);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(HttpServletResponse response, MemberLoginDTO memberLoginDTO, Model model) {
        TokenInfo tokenInfo = memberService.login(memberLoginDTO, response);
        if (tokenInfo != null) {
            return "redirect:/meet/main";
        } else {
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/kakao/callback")
    public String kakaoCallback(String code, Model model, HttpServletResponse response, HttpServletRequest request) { //Data를 리턴해주는 컨트롤러 함수

        String accessToken = memberService.requestAccessToken(code);
        KakaoProfile kakaoProfile = memberService.requestUserInfo(accessToken);

        if (!memberService.profileNicknameExist(kakaoProfile.getProperties().getNickname())) {
            model.addAttribute("profileNickname", kakaoProfile.getProperties().getNickname());
            model.addAttribute("socialAuthId", kakaoProfile.getId());
            return "auth/kakaoSignup";
        } else {
            memberService.kakaoLogin(code, accessToken, kakaoProfile, response);
            return "redirect:/meet/main";
        }
    }

    @GetMapping("/kakao/signup")
    public String signupKakao(Model model) {
        System.out.println(" 여기 도착함");
        return "auth/kakaoSignup";
    }

    @PostMapping("/kakao/signup")
    public String processKakaoSignup(@ModelAttribute MemberKakaoRegisterDTO registerDTO) {
        memberService.saveKakao(registerDTO);
        return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트 예시
    }

    @GetMapping("/signupDetail")
    public String showSignupDetailPage(HttpServletRequest request, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        } else {
            Long id = memberService.getMemberIdFromAccessTokenCookie(request);
            MemberEntity loginMemberEntity = memberService.findById(id);
            model.addAttribute("memberId", loginMemberEntity.getMemberId());
            return "auth/signUpDetail";
        }
    }

    @PostMapping("/signupDetail")
    public String handleSignupDetailForm(@ModelAttribute MemberDetailDTO dto, HttpServletRequest request) {
        String accessToken = TokenUtils.extractAccessTokenFromRequest(request);

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            // Retrieve additional details based on the access token
            Long memberId = jwtTokenProvider.getMemberIdFromToken(accessToken);
            MemberEntity member = memberService.findById(memberId);

            // Set the additional details and save
            dto.setMemberEntity(member);
            memberService.saveMemberDetail(dto);

            return "redirect:/meet/main";
        } else {
            return "redirect:/login?error=true";

        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String redirectPath = "/auth/login"; // 로그아웃 후 리다이렉션할 경로 설정

        // 로그아웃 로직 수행
        memberService.logout(request, response);
        return "redirect:" + redirectPath;
    }

    @ResponseBody
    @PostMapping(value = "/check-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> checkEmail(@RequestBody String accountEmail) {
        boolean exist = memberService.accountEmailExist(accountEmail);// Your logic to check if the email exists in the database;
        Map<String, Object> response = new HashMap<>();
        response.put("exist", exist);

        return response;
    }

    @ResponseBody
    @PostMapping(value = "/check-nickName", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> checkNickName(@RequestBody String profileNickname) {
        boolean exist = memberService.profileNicknameExist(profileNickname);// Your logic to check if the email exists in the database;
        Map<String, Object> response = new HashMap<>();
        response.put("exist", exist);

        return response;
    }


}
