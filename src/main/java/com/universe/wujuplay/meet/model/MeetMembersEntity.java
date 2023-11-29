package com.universe.wujuplay.meet.model;

import com.universe.wujuplay.member.model.MemberEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "MEET_MEMBERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetMembersEntity {

    @Id
    @Column(name = "MEMBERS_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membersId;

    @ManyToOne
    @JoinColumn(name = "MEET_ID")
    private MeetEntity meet;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID") // TODO : JOIN MEMBER 바꾸기
    private MemberEntity memberId;

    @Builder
    public MeetMembersEntity(Long membersId, MeetEntity meet, MemberEntity memberId) {
        this.membersId = membersId;
        this.meet = meet;
        this.memberId = memberId;
    }
    @Builder
    public MeetMembersEntity(MeetEntity meet, MemberEntity memberId){
        this.meet = meet;
        this.memberId = memberId;
    }
}
