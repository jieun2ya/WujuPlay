package com.universe.wujuplay.meet.model;


import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.sports.model.SportsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@DynamicInsert
@Getter
@NoArgsConstructor
@Entity
@Table(name = "MEET")
@Builder
@AllArgsConstructor
public class MeetEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meet_id")
    private Long meetId;

    private String meetName;

    private String meetInfo;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss a", timezone = "Asia/Seoul")
    private Timestamp meetDate;

    @ManyToOne
    @JoinColumn(name = "leader")
    private MemberEntity leader;

    private int maxNumber;

    @Column(name = "curr_number", nullable = false, columnDefinition = "int default 1")
    private Integer currNumber;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity locationId;

    @ManyToOne
    @JoinColumn(name = "sports_id")
    private SportsEntity sportsId;

    private int active;

    private int visible;

    private int mergeYn;

    @CreationTimestamp
    private Timestamp regdate;
    @UpdateTimestamp
    private Timestamp lastUpdated;
    @Column(name = "MERGED", columnDefinition = "int default 0")
    private int merged;


    @Builder
    public MeetEntity(Long meetId, String meetName, String meetInfo, Timestamp meetDate, MemberEntity leader, int maxNumber, int currNumber, LocationEntity locationId, SportsEntity sports, int active, int visible, int mergeYn, Timestamp regdate, Timestamp lastUpdated, int merged) {
        this.meetId = meetId;
        this.meetName = meetName;
        this.meetInfo = meetInfo;
        this.meetDate = meetDate;
        this.leader = leader;
        this.maxNumber = maxNumber;
        this.currNumber = currNumber;
        this.locationId = locationId;
        this.sportsId = sports;
        this.active = active;
        this.visible = visible;
        this.mergeYn = mergeYn;
        this.regdate = regdate;
        this.lastUpdated = lastUpdated;
        this.merged = merged;
    }

    public static MeetDTO toDTO(MeetEntity meetEntity){
        return MeetDTO.builder()
            .meetId(meetEntity.getMeetId())
            .meetName(meetEntity.getMeetName())
            .meetInfo(meetEntity.getMeetInfo())
            .meetDate(meetEntity.getMeetDate().toString())
            .leader(meetEntity.getLeader())
            .maxNumber(meetEntity.getMaxNumber())
            .currNumber(meetEntity.getCurrNumber())
            .locationId(meetEntity.getLocationId())
            .sportsId(meetEntity.getSportsEntity())
            .active(meetEntity.getActive())
            .visible(meetEntity.getVisible())
            .mergeYn(meetEntity.getMergeYn())
            .regdate(meetEntity.getRegdate())
            .lastUpdated(meetEntity.getLastUpdated())
            .merged(meetEntity.getMerged())
            .build();
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public void setMerged(int merged) {
        this.merged = merged;
    }

    public void setSelectedSport(SportsEntity sportsEntity) {
        this.sportsId = sportsEntity;
    }

    public SportsEntity getSportsEntity() {
        return sportsId;
    }

    public void update(String meetName, String meetInfo, Timestamp meetDate, int maxNumber, SportsEntity sportsId, int mergeYn, Timestamp lastUpdated, int visible) {
        this.meetName = meetName;
        this.meetInfo = meetInfo;
        this.meetDate = meetDate;
        this.maxNumber = maxNumber;
        this.sportsId = sportsId;
        this.mergeYn = mergeYn;
        this.lastUpdated = lastUpdated;
        this.visible = visible;
    }

}
