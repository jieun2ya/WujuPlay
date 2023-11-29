package com.universe.wujuplay.member.model;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private Long memberId;
    private String platformType;
    private String accountEmail;
    private String password;
    private String name;
    private String profileNickname;
    private String gender;
    private String role;
    private String address;
    private Timestamp createDate;
    private Timestamp updateDate;
    private Long socialAuthId;

    public static MemberEntity toEntity(MemberResponse memberResponse) {
        return MemberEntity.builder()
                .memberId(memberResponse.getMemberId())
                .platformType(memberResponse.getPlatformType())
                .accountEmail(memberResponse.getAccountEmail())
                .name(memberResponse.getName())
                .profileNickname(memberResponse.getProfileNickname())
                .gender(memberResponse.getGender())
                .address(memberResponse.getAddress())
                .createDate(memberResponse.getCreateDate())
                .updateDate((memberResponse.getUpdateDate()))
                .socialAuthId((memberResponse.getSocialAuthId()))
                .build();
    }
}