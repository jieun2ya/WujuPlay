package com.universe.wujuplay.member.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailDTO {
    private Long memberDetailID;
    private MemberEntity memberEntity;
    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String mbti;
    private String sportsCareer;
    private int playNumber;
    private int openNumber;
    private String interestSports;

    public static MemberDetailEntity toDetailEntity(MemberDetailDTO memberDetailDTO) {
        MemberDetailEntity memberDetailEntity = new MemberDetailEntity();
        memberDetailEntity.setMemberEntity(memberDetailDTO.getMemberEntity());
        memberDetailEntity.setPhone(memberDetailDTO.getPhone());
        memberDetailEntity.setBirthday(memberDetailDTO.getBirthday());
        memberDetailEntity.setMbti(memberDetailDTO.getMbti());
        memberDetailEntity.setInterestSports(memberDetailDTO.getInterestSports());
        memberDetailEntity.setOpenNumber(memberDetailDTO.getOpenNumber());
        memberDetailEntity.setPlayNumber(memberDetailDTO.getPlayNumber());
        memberDetailEntity.setSportsCareer(memberDetailDTO.getSportsCareer());
        return memberDetailEntity;
    }

    public static MemberDetailDTO from(MemberDetailEntity memberDetailEntity) {
        final Long memberDetailID = memberDetailEntity.getMemberDetailID();
        final MemberEntity memberEntity = memberDetailEntity.getMemberEntity();
        final String phone = memberDetailEntity.getPhone();
        final Date birthday = memberDetailEntity.getBirthday();
        final String mbti = memberDetailEntity.getMbti();
        final String sportsCareer = memberDetailEntity.getSportsCareer();
        final int openNumber = memberDetailEntity.getOpenNumber();
        final int playNumber = memberDetailEntity.getPlayNumber();
        final String interestSports = memberDetailEntity.getInterestSports();
        return new MemberDetailDTO(memberDetailID, memberEntity, phone, birthday, mbti, sportsCareer, openNumber, playNumber, interestSports);
    }

}
