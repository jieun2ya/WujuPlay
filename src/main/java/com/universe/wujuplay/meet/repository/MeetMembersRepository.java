package com.universe.wujuplay.meet.repository;

import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.meet.model.MeetMembersDTO;
import com.universe.wujuplay.meet.model.MeetMembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface MeetMembersRepository extends JpaRepository<MeetMembersEntity, Long> {

    // 전체 내 모임
    @Query("select mm.meet from MeetMembersEntity mm where mm.memberId.memberId = :memberId and mm.meet.active = 1 and mm.meet.merged = 0 order by mm.meet.meetDate asc")
    List<MeetEntity> findActiveMeetEntityByMemberId(@Param("memberId") Long memberId);
    //List<MeetEntity> findActiveMeetEntityByMemberId(MemberEntity memberEntity);

    // 참여 중인 내 모임
    @Query("SELECT mm.meet FROM MeetMembersEntity mm WHERE mm.memberId.memberId = :memberId AND mm.meet.leader.memberId != :memberId and mm.meet.active = 1 and mm.meet.merged = 0 order by mm.meet.meetDate asc")
    List<MeetEntity> findMeetIdsByMemberIdNotLeader(@Param("memberId") Long memberId);

    // 완료된 내 모임
    @Query("select mm.meet from MeetMembersEntity mm where mm.memberId.memberId = :memberId and mm.meet.active = 0 and mm.meet.merged = 0 order by mm.meet.meetDate asc")
    List<MeetEntity> findInactiveMeetEntityByMemberId(@Param("memberId") Long memberId);

    List<MeetMembersEntity> findByMeetOrMeet(MeetEntity hosting, MeetEntity enrolling);

    // 해당 모임에 참여한 멤버정보 찾기
    List<MeetMembersEntity> findByMeetMeetId(Long meetId);

    // memberId로 member+memberdetail 정보 조회
    @Query("SELECT new com.universe.wujuplay.meet.model.MeetMembersDTO(" +
            "m.memberId, " +
            "m.profileNickname, " +
            "m.gender, " +
            "md.birthday, " +
            "md.mbti, " +
            "md.sportsCareer, " +
            "md.playNumber, " +
            "md.openNumber, " +
            "md.interestSports) " +
            "FROM MemberEntity m " +
            "JOIN MemberDetailEntity md ON m.memberId = md.memberEntity.memberId " +
            "WHERE m.memberId = :memberId")
    MeetMembersDTO findMemberDetails(@Param("memberId") Long memberId);
    
    // meetMembers 테이블에서 해당회원 정보 삭제
    @Transactional
    @Modifying
    @Query("DELETE FROM MeetMembersEntity mm WHERE mm.meet.meetId = :meetId AND mm.memberId.memberId = :memberId")
    void deleteByMeetIdAndMemberId(@Param("meetId") Long meetId, @Param("memberId") Long memberId);

    // 채팅방에 참가한 모든 유저 리턴
    @Query(value = "select mm.* from meet m join chat c on m.meet_id = c.meet_id join meet_members mm on mm.meet_id = m.meet_id where c.chat_room_id = :chatId", nativeQuery = true)
    Optional<List<MeetMembersEntity>> findMembersByChatRoomId(long chatId);

    // 조인되어 새롭게 생성된 모임의 모든 유저 리턴
    List<MeetMembersEntity> findAllByMeet(MeetEntity newMeetEntity);
}