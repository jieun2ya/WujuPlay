package com.universe.wujuplay.meet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.review.model.FileEntity;
import com.universe.wujuplay.review.model.ReviewDTO;
import com.universe.wujuplay.review.model.ReviewEntity;
import com.universe.wujuplay.sports.model.SportsEntity;
import lombok.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MeetDTO {

    private Long meetId;
    private String meetName;
    private String meetInfo;
    private String meetDate;
    private MemberEntity leader;
    private int maxNumber;
    private Integer currNumber;
    private LocationEntity locationId;

    private SportsEntity sportsId;
    private int active;
    private int visible;
    private int mergeYn;
    private Timestamp regdate;
    private Timestamp lastUpdated;
    private int merged;


    public static MeetEntity toEntity(MeetDTO meetDTO) {
        return MeetEntity.builder()
                .meetName(meetDTO.getMeetName())
                .meetInfo(meetDTO.getMeetInfo())
                .meetDate(Timestamp.valueOf(meetDTO.getMeetDate()))
                .maxNumber(meetDTO.getMaxNumber())
                .mergeYn(meetDTO.getMergeYn())
                .leader(meetDTO.getLeader())
                .sportsId(meetDTO.getSportsId())
                .currNumber(meetDTO.getCurrNumber())
                .active(meetDTO.getActive())
                .visible(meetDTO.getVisible())
                .locationId(meetDTO.getLocationId())
                .merged(meetDTO.getMerged())
                .build();
    }


}
