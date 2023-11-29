package com.universe.wujuplay.config.jwt;

import com.universe.wujuplay.member.model.MemberTokenInfo;
import com.universe.wujuplay.member.service.CustomMemberDetails;
import com.universe.wujuplay.member.service.MemberCustomDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;  // 30분
    private final MemberCustomDetailService userDetailsService;
    private final Key key;

    public TokenProvider(MemberCustomDetailService userDetailsService, @Value("${jwt.secret}") String secretKey) {
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public MemberTokenInfo.TokenInfo generateToken(Authentication authentication) {
        CustomMemberDetails memberDetails = (CustomMemberDetails) authentication.getPrincipal();
        Long memberId = memberDetails.getMemberId();
        String memberEmail = extractUsername(authentication);
        String authorities = extractAuthorities(authentication);
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = buildToken(memberEmail, authorities, accessTokenExpiresIn, memberId);
        return new MemberTokenInfo.TokenInfo(accessToken, ACCESS_TOKEN_EXPIRE_TIME);
    }

    private String extractUsername(Authentication authentication) {
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    private String extractAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private String buildToken(String subject, String authorities, Date expiration, Long memberId) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(AUTHORITIES_KEY, authorities)
                .claim("memberId", memberId) // userId 클레임 추가
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = extractClaims(accessToken, "Failed to extract authentication info from access token");
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(claims);

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public String getSubject(String token) {
        Claims claims = extractClaims(token, "Failed to extract subject from token");
        return claims.getSubject();
    }

    public Long getMemberIdFromToken(String token) {
        Claims claims = extractClaims(token, "Failed to extract membmer ID from token");
        Number memberId = claims.get("memberId", Number.class); // 클레임에서 userId 추출
        return memberId != null ? memberId.longValue() : null;
    }

    private Claims extractClaims(String token, String errorMessage) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            throw new RuntimeException(errorMessage, e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            log.info("Invalid JWT Token: {}", e.getMessage());
            return false;
        }
    }

    public String createAccessToken(String accountEmail, Collection<? extends GrantedAuthority> authorities, Long memberId) {
        // 사용자 이메일을 기반으로 userId 조회
        return buildToken(accountEmail, extractAuthoritiesString(authorities), new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME), memberId);
    }

    public Collection<GrantedAuthority> getRoles(String token) {
        Claims claims = extractClaims(token, "Failed to extract roles from token");
        return extractAuthorities(claims);
    }

    private String extractAuthoritiesString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    }

    private Collection<GrantedAuthority> extractAuthorities(Claims claims) {
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}

