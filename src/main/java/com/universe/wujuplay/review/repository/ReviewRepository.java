package com.universe.wujuplay.review.repository;

import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.review.model.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    // 리뷰 검색
    @Query("SELECT r FROM ReviewEntity r " +
            "JOIN r.writer w " +
            "JOIN r.meetEntity m " +
            "JOIN m.locationId l " +
            "WHERE r.title LIKE %:keyword% " +
            "OR r.content LIKE %:keyword% " +
            "OR w.name LIKE %:keyword% " +
            "OR m.meetName LIKE %:keyword% " +
            "OR l.addressName LIKE %:keyword% " +
            "OR l.placeName LIKE %:keyword%" +
            "ORDER BY r.regdate DESC")
    List<ReviewEntity> findByKeyword(@Param("keyword") String keyword);

    // 리뷰 작성 여부
    ReviewEntity findByWriter_MemberIdAndMeetEntity_MeetId(Long memberId, Long meetId);

    // review 전체목록 정렬해서 가져오기
    List<ReviewEntity> findAllByOrderByRegdateDesc();
}
