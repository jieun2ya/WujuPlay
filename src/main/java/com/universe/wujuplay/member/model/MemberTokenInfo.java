package com.universe.wujuplay.member.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class MemberTokenInfo {

    @Builder
    @Getter
    @AllArgsConstructor
    @ToString
    public static class TokenInfo {
        private String accessToken;
        private Long accessTokenExpirationTime;
    }
}
