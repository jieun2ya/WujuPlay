package com.universe.wujuplay.config;

import com.universe.wujuplay.config.jwt.TokenAuthenticationFilter;
import com.universe.wujuplay.config.jwt.TokenProvider;
import com.universe.wujuplay.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity//스프링 시큐리티 필터가 스프링필터체인에 등록이 됩니다.
@RequiredArgsConstructor
public class SecurityConfig {


    //    private final MemberCustomDetailService customDetailService;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers(
                "/h2-console/**",
                "/api-document/**",
                "/swagger-ui/**",
                "/static/**",
                "/css/**",
                "/js/**",
                "/image/**"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> auth
                        .antMatchers("/auth/login",
                                "/auth/signup",
                                "/auth/logout",
                                "auth/kakao/callback",
                                "/meet/**",
                                "/auth/**",
                                "/auth/kakao/signup",
                                "/auth/social/login",
                                "/review/**",
                                "/index",
                                "/check-email",
                                "/check-nickName",
                                "/"


                        ).permitAll() // 로그인, 회원가입, 유저 페이지는 인증 없이 접근 허용
                        .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN") // '/test' 경로는 'ROLE_USER' 권한을 가진 사용자만 접근 가능
                        .antMatchers("/auth/kakao/callback").permitAll() // Kakao 콜백 URL 허용
                        .antMatchers("/auth/kakao/callback/**").permitAll() // Kakao 콜백 URL 허용
                        .anyRequest().authenticated() // 나머지 요청은 모두 인증 필요
                )
                .formLogin().disable()
                .csrf(csrf -> csrf.disable()) // H2 콘솔 사용 시 CSRF 비활성화 필요
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login")
                )
                .headers(headers -> headers.frameOptions().disable()) // H2 콘솔은 iframe을 사용하기 때문에 이를 허용해야 함
                // beforeFilter가 실행되기 이전에 Filter을 먼저 실행시키도록 설정하는 메소드
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider, memberRepository), UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(new ExceptionHandlerFilter(), JwtTokenFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }


}
