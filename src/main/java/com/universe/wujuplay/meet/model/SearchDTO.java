package com.universe.wujuplay.meet.model;

import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.sports.model.SportsEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SearchDTO {

    private String searchPeriod;
    private String searchType;
    private String sportsId;
    private String keyword;
    private String startDate;
    private String endDate;




}
