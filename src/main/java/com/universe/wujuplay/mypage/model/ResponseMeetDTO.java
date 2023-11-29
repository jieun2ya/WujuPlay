package com.universe.wujuplay.mypage.model;

import com.universe.wujuplay.meet.model.LocationEntity;
import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.review.model.ReviewEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@Data
public class ResponseMeetDTO {

    private Long meetId;
    private String meetName;
    private String meetDate;
    private LocationEntity locationEntity;
    private ReviewEntity reviewEntity;
    private Long reviewIsNull;

    @Builder
    public ResponseMeetDTO(Long meetId, String meetName, String meetDate, LocationEntity locationEntity, ReviewEntity reviewEntity, Long reviewIsNull) {
        this.meetId = meetId;
        this.meetName = meetName;
        this.meetDate = meetDate;
        this.locationEntity = locationEntity;
        this.reviewEntity = reviewEntity;
        this.reviewIsNull = reviewIsNull;
    }
}
