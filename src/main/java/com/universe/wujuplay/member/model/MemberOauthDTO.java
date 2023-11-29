package com.universe.wujuplay.member.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class MemberOauthDTO implements OAuth2User {

    private OAuth2User oauth2User;

    public MemberOauthDTO(OAuth2User oauth2User) {
        if (oauth2User == null) {
            throw new IllegalArgumentException("oauth2User must not be null");
        }
        this.oauth2User = oauth2User;
    }

    public String getNickname() {
        Map<String, Object> properties = oauth2User.getAttribute("properties");
        return properties != null ? (String) properties.get("profileNickname") : null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getName();
    }

    @Override
    public String toString() {
        return "MemberOauthDTO{" +
                "oauth2User=" + oauth2User +
                '}';
    }

}
