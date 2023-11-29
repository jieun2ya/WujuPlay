package com.universe.wujuplay.review.model;

import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class ResponseReviewDTO {

    private long reviewId;
    private String title;
    private MemberEntity writer;
    private MeetEntity meetEntity;
    private FileEntity fileEntity;

    @Builder
    public ResponseReviewDTO(long reviewId, String title, MemberEntity writer, MeetEntity meetEntity, FileEntity fileEntity) {
        this.reviewId = reviewId;
        this.title = title;
        this.writer = writer;
        this.meetEntity = meetEntity;
        this.fileEntity = fileEntity;
    }
}
