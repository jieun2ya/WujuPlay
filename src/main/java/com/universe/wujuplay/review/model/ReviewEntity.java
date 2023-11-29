package com.universe.wujuplay.review.model;

import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "REVIEW")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntity {

    @Id
    @Column(name = "REVIEW_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "WRITER")
    private MemberEntity writer;

    @CreationTimestamp
    @Column(name = "REGDATE")
    private Timestamp regdate;

    @UpdateTimestamp
    @Column(name = "LAST_UPDATED")
    private Timestamp lastUpdated;

    @ManyToOne
    @JoinColumn(name = "MEET_ID")
    private MeetEntity meetEntity;

    @Column(name = "PLACE_RATE")
    private int placeRate;

    // 다중 파일 업로드
    @OneToMany(mappedBy = "reviewEntity", cascade = CascadeType.ALL)
    private List<FileEntity> fileEntityList = new ArrayList<>();

    @Builder
    public ReviewEntity(long reviewId, String title, String content, MemberEntity writer, MeetEntity meetEntity, int placeRate) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.meetEntity = meetEntity;
        this.placeRate = placeRate;
    }

    public void update(String title, String content, int placeRate) {
        this.title = title;
        this.content = content;
        this.placeRate = placeRate;
    }
}
