package com.universe.wujuplay.member.service;

import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class MemberCustomDetailService implements UserDetailsService {

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    public MemberCustomDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String accountEmail) throws UsernameNotFoundException {
        MemberEntity member = memberRepository.findByAccountEmail(accountEmail)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보가 일치하지 않습니다."));
        GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole());
        return new CustomMemberDetails(member.getMemberId(), member.getAccountEmail(), member.getPassword(), Collections.singleton(authority));
    }

}
