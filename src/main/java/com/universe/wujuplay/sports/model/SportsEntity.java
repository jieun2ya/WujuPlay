package com.universe.wujuplay.sports.model;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "SPORTS")
public class SportsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sportsId;

    private String name;

    private SportsDTO toDTO(SportsEntity sportsEntity){
        return SportsDTO.builder()
                .sportsId(sportsEntity.sportsId)
                .name(sportsEntity.name)
                .build();
    }
}
