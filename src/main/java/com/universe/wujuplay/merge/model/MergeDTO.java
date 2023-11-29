package com.universe.wujuplay.merge.model;

import com.universe.wujuplay.meet.model.MeetDTO;
import com.universe.wujuplay.meet.model.MeetEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MergeDTO {

    private Long mergeId;
    private MeetDTO enrolling;
    private MeetDTO hosting;
    private int mergeYn;
    private MeetDTO resultMeet;
    private LocalDateTime regdate;
    private LocalDateTime acceptDate;


    public static MergeEntity toEntity(MergeDTO mergeDTO){
        return MergeEntity.builder()
                .mergeId(mergeDTO.getMergeId())
                .enrolling(MeetDTO.toEntity(mergeDTO.getEnrolling()))
                .hosting(MeetDTO.toEntity(mergeDTO.getHosting()))
                .mergeYn(mergeDTO.getMergeYn())
                .resultMeet(MeetDTO.toEntity(mergeDTO.getResultMeet()))
                .regdate(mergeDTO.getRegdate())
                .acceptDate(mergeDTO.getAcceptDate())
                .build();
    }

}
