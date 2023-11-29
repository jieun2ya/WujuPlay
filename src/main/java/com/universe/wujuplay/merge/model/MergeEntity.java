package com.universe.wujuplay.merge.model;

import com.universe.wujuplay.meet.model.MeetEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice.Local;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "MERGE")
public class MergeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mergeId;
    @ManyToOne
    @JoinColumn(name = "enrolling")
    private MeetEntity enrolling;
    @ManyToOne
    @JoinColumn(name = "hosting")
    private MeetEntity hosting;
    @Column(name = "merge_yn")
    private int mergeYn;
    @ManyToOne
    @JoinColumn(name = "result_meet")
    private MeetEntity resultMeet;
    @CreationTimestamp
    private LocalDateTime regdate;
    private LocalDateTime acceptDate;

    public static MergeDTO toDTO(MergeEntity mergeEntity){
        return MergeDTO.builder()
            .mergeId(mergeEntity.getMergeId())
            .enrolling(MeetEntity.toDTO(mergeEntity.getEnrolling()))
            .hosting(MeetEntity.toDTO(mergeEntity.getHosting()))
            .mergeYn(mergeEntity.getMergeYn())
            .resultMeet(MeetEntity.toDTO(mergeEntity.getResultMeet()))
            .regdate(mergeEntity.getRegdate())
            .acceptDate(mergeEntity.getAcceptDate())
            .build();
    }


    public void setMergeYn(int mergeYn) {
        this.mergeYn = mergeYn;
    }

    public void setAcceptDate(LocalDateTime acceptDate) {
        this.acceptDate = acceptDate;
    }

    public void setResultMeet(MeetEntity resultMeet) {
        this.resultMeet = resultMeet;
    }
}
