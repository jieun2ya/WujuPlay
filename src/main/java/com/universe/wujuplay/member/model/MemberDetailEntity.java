package com.universe.wujuplay.member.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "MEMBER_DETAIL")
public class MemberDetailEntity {

    @Id
    @Column(name = "memberdetail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberDetailID;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;
    //private Long memberId;

    private String phone;
    private Date birthday;
    private String mbti;
    private String sportsCareer;
    private Integer playNumber;
    private Integer openNumber;
    private String interestSports;

    public void update(String phone, String mbti, String sportsCareer, String interestSports) {
        this.phone = phone;
        this.mbti = mbti;
        this.sportsCareer = sportsCareer;
        this.interestSports = interestSports;
    }

}