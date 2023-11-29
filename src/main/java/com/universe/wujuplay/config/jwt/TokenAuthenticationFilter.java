package com.universe.wujuplay.config.jwt;

import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends GenericFilterBean {
    private final TokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String accessToken = resolveToken(httpRequest, "accessToken");

        if (isTokenValid(accessToken)) {
            processValidAccessToken(accessToken, httpRequest);
            ;
        }
        chain.doFilter(request, response);
    }

    private boolean isTokenValid(String accessToken) {
        return StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken);
    }

    private void processValidAccessToken(String accessToken, HttpServletRequest httpRequest) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String memberEmail = jwtTokenProvider.getSubject(accessToken);
        Optional<MemberEntity> user = memberRepository.findByAccountEmail(memberEmail);
        user.ifPresent(member -> httpRequest.setAttribute("memberId", member.getMemberId()));
    }


    private String resolveToken(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
