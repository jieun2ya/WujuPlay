package com.universe.wujuplay.member.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Entity
@Table(name = "MEMBER")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "PLATFORM_TYPE", nullable = true)
    private String platformType;

    @Column(name = "ACCOUNT_EMAIL")
    private String accountEmail;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "PROFILE_NICKNAME")
    private String profileNickname;

    @Column(name = "NAME")
    private String name;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "ROLE")
    private String role; //ROLE_USER, ROLE_ADMIN

    @Column(name = "ADDRESS")
    private String address;

    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    private Timestamp createDate;

    @UpdateTimestamp
    @Column(name = "UPDATE_DATE")
    private Timestamp updateDate;

    @Column(name = "SOCIAL_AUTH_ID")
    private Long socialAuthId;

    public void setPassword(String password) {
        this.password = password;
    }

    @Builder
    public MemberEntity(String accountEmail, String password) {
        this.accountEmail = accountEmail;
        this.password = password;
    }

    public static MemberResponse toResponseDTO(MemberEntity memberEntity) {
        return MemberResponse.builder()
                .memberId(memberEntity.getMemberId())
                .platformType(memberEntity.getPlatformType())
                .accountEmail(memberEntity.getAccountEmail())
                .password(memberEntity.getPassword())
                .name(memberEntity.getName())
                .profileNickname(memberEntity.getProfileNickname())
                .gender(memberEntity.getGender())
                .role(String.valueOf(memberEntity.getRole()))
                .address(memberEntity.getAddress())
                .createDate(memberEntity.getCreateDate())
                .updateDate(memberEntity.getUpdateDate())
                .socialAuthId(memberEntity.getSocialAuthId())
                .build();
    }

    public void update(String name, String profileNickname, String gender, String address) {
        this.name = name;
        this.profileNickname = profileNickname;
        this.gender = gender;
        this.address = address;
    }

}

