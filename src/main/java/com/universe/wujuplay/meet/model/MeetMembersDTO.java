package com.universe.wujuplay.meet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
public class MeetMembersDTO {

    private Long memberId;
    private String profileNickname;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String mbti;
    private String sportsCareer;
    private int playNumber;
    private int openNumber;
    private String interestSports;


}
