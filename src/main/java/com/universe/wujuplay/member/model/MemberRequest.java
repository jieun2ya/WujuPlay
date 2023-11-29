package com.universe.wujuplay.member.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequest {

    private Long memberId;
    private String accountEmail;
    private String password;
    private String name;
    private String profileNickname;
    private String gender;
    private String address;
    private String role;


    @Builder
    public static MemberEntity toEntity(MemberRequest memberRequest) {
        return MemberEntity.builder()
                .memberId(memberRequest.getMemberId())
                .accountEmail(memberRequest.getAccountEmail())
                .password(memberRequest.getPassword())
                .name(memberRequest.getName())
                .profileNickname(memberRequest.getProfileNickname())
                .gender(memberRequest.getGender())
                .role(memberRequest.getRole())
                .address(memberRequest.getAddress())
                .build();
    }
}