package com.universe.wujuplay.member.repository;

import com.universe.wujuplay.member.model.MemberDetailEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberDetailRepository extends JpaRepository<MemberDetailEntity, MemberEntity> {
    Optional<MemberDetailEntity> findByMemberEntity_MemberId(Long memberId);

    // 모임연 횟수 증가시키기
    @Transactional
    @Modifying
    @Query("UPDATE MemberDetailEntity m SET m.openNumber = m.openNumber + 1 WHERE m.memberEntity.memberId = :memberId")
    void incrementOpenNumber(@Param("memberId") Long memberId);

    // 모임*참여* 횟수 증가시키기
    @Transactional
    @Modifying
    @Query("UPDATE MemberDetailEntity m SET m.playNumber = m.playNumber + 1 WHERE m.memberEntity.memberId = :memberId")
    void incrementPlayNumber(@Param("memberId") Long memberId);

    // 모임 참여 횟수 감소시키기
    @Transactional
    @Modifying
    @Query("UPDATE MemberDetailEntity m SET m.playNumber = m.playNumber - 1 WHERE m.memberEntity.memberId = :memberId AND m.playNumber > 0")
    void decrementPlayNumber(@Param("memberId") Long memberId);

}
