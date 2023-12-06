package com.universe.wujuplay.meet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
