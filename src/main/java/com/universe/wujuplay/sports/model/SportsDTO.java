package com.universe.wujuplay.sports.model;

import com.universe.wujuplay.meet.model.MeetDTO;
import com.universe.wujuplay.meet.model.MeetEntity;
import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SportsDTO {

    private Long sportsId;

    private String name;

    public SportsEntity toEntity(SportsDTO sportsDTO) {
        return SportsEntity.builder()
                .sportsId(sportsDTO.sportsId)
                .name(sportsDTO.name)
                .build();
    }
}
