package com.universe.wujuplay.member.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class MemberKakaoRegisterDTO {

    private Long socialAuthId;
    private String platformType;
    private String accountEmail;
    private String profileNickname;
    private String name;
    private String gender;
    private String address;
    private String role;

    @Builder
    public static MemberEntity toEntity(MemberKakaoRegisterDTO memberKakaoRegisterDTO) {
        return MemberEntity.builder()
                .socialAuthId(memberKakaoRegisterDTO.getSocialAuthId())
                .accountEmail(memberKakaoRegisterDTO.getAccountEmail())
                .platformType(memberKakaoRegisterDTO.getPlatformType())
                .name(memberKakaoRegisterDTO.getName())
                .profileNickname(memberKakaoRegisterDTO.getProfileNickname())
                .gender(memberKakaoRegisterDTO.getGender())
                .role(memberKakaoRegisterDTO.getRole())
                .address(memberKakaoRegisterDTO.getAddress())
                .build();
    }
}

