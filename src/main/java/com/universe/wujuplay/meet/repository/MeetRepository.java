package com.universe.wujuplay.meet.repository;

import com.universe.wujuplay.meet.model.LocationEntity;
import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.universe.wujuplay.sports.model.SportsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MeetRepository extends JpaRepository<MeetEntity, Long> {


    Optional<List<MeetEntity>> findAllBySportsIdAndActiveAndVisibleOrderByMeetDate(Long sportsId, int active, int visible);

    Optional<List<MeetEntity>> findAllByActiveAndVisibleOrderByMeetDate(int active, int visible);

    Optional<List<MeetEntity>> findByLeaderAndActiveOrderByMeetDate(MemberEntity member, int active);

    @Query(value = "select * from meet m1 join merge m2 on m1.meet_id = m2.enrolling where m2.hosting = :hostingId AND m2.merge_yn = 0", nativeQuery = true)
    Optional<List<MeetEntity>> findMeetsByHostingMeetId(@Param("hostingId") long hostingId);

    // locationId에 열려있는 모임갯수 조회
    @Query(value = "SELECT COUNT(*) FROM Meet m WHERE m.location_Id = :locationId AND m.active = 1 AND m.visible = 1", nativeQuery = true)
    int findMeetCountByLocationId(@Param("locationId") Long locationId);

    // locationId에 열려있는 모임loist 조회
    Optional<List<MeetEntity>> findByLocationIdAndActiveAndVisibleOrderByMeetDate(LocationEntity locationId, int active, int visible);


    // 모임 참여자수 증가
    @Transactional
    @Modifying
    @Query("UPDATE MeetEntity m SET m.currNumber = m.currNumber + 1 WHERE m.meetId = :meetId")
    void incrementCurrNumber(@Param("meetId") Long meetId);

    // 모임 참여자수 감소
    @Transactional
    @Modifying
    @Query("UPDATE MeetEntity m SET m.currNumber = m.currNumber - 1 WHERE m.meetId = :meetId")
    void decrementCurrNumber(@Param("meetId") Long meetId);


    // meet 전체목록 페이징 처리하여 가져오기 active 값이 0인 아그들만
//    @Query("SELECT m FROM MeetEntity m WHERE m.active = 1")
//    Page<MeetEntity> findByActiveIs(Pageable pageable);
    @Query("SELECT m FROM MeetEntity m WHERE m.active = 1 AND m.visible = 1 order by meetDate")
    Page<MeetEntity> findByActiveAndVisibleOrderByMeetId(Pageable pageable);


    @Query(value = "select * from meet where active = 1 and visible = 1 and merge_yn = 1"
        + " and location_id = :locationId and sports_id = :sportsId and leader <> :memberId"
        + " and meet_date between DATE_SUB(:meetDate, INTERVAL 1 HOUR) and DATE_ADD(:meetDate, INTERVAL 1 HOUR);"
        , nativeQuery = true)
    List<MeetEntity> findMergeable(long locationId, long sportsId, long memberId, Timestamp meetDate);

    // 모임목록 검색해서 가져오기
    @Query("SELECT m FROM MeetEntity m WHERE " +
            "((:searchType = '1' AND m.meetName LIKE %:keyword%) OR " +
            "(:searchType = '2' AND m.sportsId.sportsId = :sportsId))" +
            "AND " +
            "((:searchPeriod = 'all' OR " +
            "(:searchPeriod = 'aWeek' AND m.meetDate >= :aWeekAgo) OR " +
            "(:searchPeriod = 'aMonth' AND m.meetDate >= :aMonthAgo) OR " +
            "(:searchPeriod = 'custom' AND m.meetDate BETWEEN :customStartDate AND :customEndDate))) " +
            "AND m.active = 1 AND m.visible = 1 " +
            "ORDER BY m.meetDate")
    List<MeetEntity> searchMeets(
            @Param("searchPeriod") String searchPeriod,
            @Param("customStartDate") Timestamp customStartDate,
            @Param("customEndDate") Timestamp customEndDate,
            @Param("searchType") String searchType,
            @Param("sportsId") Long sportsId,
            @Param("keyword") String keyword,
            @Param("aWeekAgo") Timestamp aWeekAgo,
            @Param("aMonthAgo") Timestamp aMonthAgo
    );

    // regdate를 기준으로 내림차순 정렬하여 최상위 5개를 가져오기
    List<MeetEntity> findTop5ByActiveAndVisibleOrderByRegdateDescMeetDateDesc(int active, int visible);

}
