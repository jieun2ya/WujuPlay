package com.universe.wujuplay.review.model;

import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class ReviewDTO {

    private long reviewId;
    private String title;
    private String content;
    private MemberEntity writer;
    private Date regdate;
    private Date lastUpdated;
    private MeetEntity meetEntity;
    private int placeRate;

    @Builder
    public ReviewDTO(long reviewId, String title, String content, MemberEntity writer, Date regdate, Date lastUpdated, MeetEntity meetEntity, int placeRate) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.regdate = regdate;
        this.lastUpdated = lastUpdated;
        this.meetEntity = meetEntity;
        this.placeRate = placeRate;
    }

    public static ReviewDTO from(ReviewEntity reviewEntity) {
        final Long reviewId = reviewEntity.getReviewId();
        final String title = reviewEntity.getTitle();
        final String content = reviewEntity.getContent();
        final MemberEntity writer = reviewEntity.getWriter();
        final Date regdate = reviewEntity.getRegdate();
        final Date lastUpdated = reviewEntity.getLastUpdated();
        final MeetEntity meetEntity = reviewEntity.getMeetEntity();
        final int placeRate = reviewEntity.getPlaceRate();

        return new ReviewDTO(reviewId, title, content, writer, regdate, lastUpdated, meetEntity, placeRate);
    }

    public ReviewEntity toEntity() {
        return ReviewEntity.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .meetEntity(meetEntity)
                .placeRate(placeRate)
                .build();
    }
}
