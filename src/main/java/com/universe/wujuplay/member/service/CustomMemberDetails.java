package com.universe.wujuplay.member.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class CustomMemberDetails extends org.springframework.security.core.userdetails.User {
    private final Long memberId;

    public CustomMemberDetails(Long memberId, String AccountEmail, String password, Collection<? extends GrantedAuthority> authorities) {
        super(AccountEmail, password, authorities);
        this.memberId = memberId;
    }

}
