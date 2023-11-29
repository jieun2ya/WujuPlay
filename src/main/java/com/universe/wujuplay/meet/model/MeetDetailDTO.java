package com.universe.wujuplay.meet.model;

import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.sports.model.SportsEntity;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetDetailDTO {

    private MeetDTO meet;
    private List<MeetMembersDTO> meetMembers;
    private String attendYN;
    private Long loginMemberId;


}
