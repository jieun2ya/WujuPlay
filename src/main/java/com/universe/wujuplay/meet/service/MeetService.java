package com.universe.wujuplay.meet.service;

import com.universe.wujuplay.chat.model.ChatEntity;
import com.universe.wujuplay.chat.repository.ChatRepository;
import com.universe.wujuplay.meet.model.*;
import com.universe.wujuplay.meet.repository.LocationRepository;
import com.universe.wujuplay.meet.repository.MeetMembersRepository;
import com.universe.wujuplay.meet.repository.MeetRepository;
import com.universe.wujuplay.member.model.MemberDetailEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.model.MemberResponse;
import com.universe.wujuplay.member.repository.MemberDetailRepository;
import com.universe.wujuplay.member.repository.MemberRepository;
import com.universe.wujuplay.mypage.model.ResponseMeetDTO;
import com.universe.wujuplay.review.model.ReviewEntity;
import com.universe.wujuplay.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


@RequiredArgsConstructor
@Service
public class MeetService {

    private final MeetRepository meetRepository;
    private final MeetMembersRepository meetMembersRepository;
    private final ReviewRepository reviewRepository;
    private final LocationRepository locationRepository;
    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final ChatRepository chatRepository;

    // 새로 모임 열기
    public MeetDTO save(MeetEntity Meetentity, MemberEntity memberEntity) {
        MeetEntity entity = meetRepository.save(Meetentity);
        // 모임 정보 추가하고
        meetMembersRepository.save(new MeetMembersEntity(entity, memberEntity));
        // 모임연 회원 모임횟수 +1해주기
        memberDetailRepository.incrementOpenNumber(memberEntity.getMemberId());
        //채팅방 만들기
        ChatEntity chat = ChatEntity.builder().name(entity.getMeetName()).meet(entity).active(1).build();
        chatRepository.save(chat);


        return MeetEntity.toDTO(entity);
    }

    // mypage의 전체 내 모임
    public List<MeetDTO> myMeet(MemberEntity memberEntity) {

        List<MeetEntity> meetEntityList = meetMembersRepository.findActiveMeetEntityByMemberId(memberEntity.getMemberId());
        List<MeetDTO> meetDTOList = new ArrayList<>();
        for (MeetEntity meetEntity : meetEntityList) {
            meetDTOList.add(MeetEntity.toDTO(meetEntity));
        }

        return meetDTOList;
    }

    // mypage의 내가 참여한 모임
    public List<MeetDTO> myEnrolledMeet(MemberEntity memberEntity) {

        List<MeetEntity> meetEntityList = meetMembersRepository.findMeetIdsByMemberIdNotLeader(memberEntity.getMemberId());
        List<MeetDTO> meetDTOList = new ArrayList<>();
        for (MeetEntity meetEntity : meetEntityList) {
            meetDTOList.add(MeetEntity.toDTO(meetEntity));
        }

        return meetDTOList;
    }

    public List<MeetDTO> hostingMeets(MemberResponse memberResponse){
        MemberEntity memberSignedIn = MemberResponse.toEntity(memberResponse);
        return meetRepository.findByLeaderAndActiveOrderByMeetDate(memberSignedIn, 1)
            .get()
            .stream()
            .map(MeetEntity::toDTO)
            .toList();
    }

//     mypage의 완료된 내 모임
    public List<ResponseMeetDTO> previousMeet(Long memberId) {

        List<MeetEntity> meetEntityList = meetMembersRepository.findInactiveMeetEntityByMemberId(memberId);
        List<ResponseMeetDTO> responseMeetDTOList = new ArrayList<>();
        for (MeetEntity meetEntity : meetEntityList) {

            // reviewIsNull
            ReviewEntity reviewEntity = reviewRepository.findByWriter_MemberIdAndMeetEntity_MeetId(memberId, meetEntity.getMeetId());

            long reviewId;

            if (reviewEntity == null) {
                reviewId = 0;
            } else {
                reviewId = reviewEntity.getReviewId();
            }

            LocationEntity locationEntity = locationRepository.getReferenceById(meetEntity.getLocationId().getLocationId());

            ResponseMeetDTO responseMeetDTO = ResponseMeetDTO.builder()
                                                .meetId(MeetEntity.toDTO(meetEntity).getMeetId())
                                                .meetName(MeetEntity.toDTO(meetEntity).getMeetName())
                                                .meetDate(MeetEntity.toDTO(meetEntity).getMeetDate())
                                                .locationEntity(locationEntity)
                                                .reviewIsNull(reviewId)
                                                .build();

            responseMeetDTOList.add(responseMeetDTO);
        }
        return responseMeetDTOList;
    }

    // 전달받은 meetId에 해당 하는 모임정보 가져오기
    public MeetEntity findById(Long meetId) {
        MeetEntity meetEntity = meetRepository.findById(meetId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + meetId));

        return meetEntity;
    }


    // 전달받은 locationId에 해당하는 모임정보 리스트 가져오기
    public List<MeetDTO> findByLocationId(LocationDTO locationDTO) {

        Optional<List<MeetEntity>> list = meetRepository.findByLocationIdAndActiveAndVisibleOrderByMeetDate(LocationDTO.toEntity(locationDTO),1,1);

        return list.get()
                    .stream()
                    .map(meetEntity -> MeetEntity.toDTO(meetEntity))
                    .toList();
    }

    // 전달받은 memberId memberDetail 등록되어 있는지 확인
    public boolean checkMemberDetail(Long memberId) {
        Optional<MemberDetailEntity> memberDetail = memberDetailRepository.findByMemberEntity_MemberId(memberId);
        if (memberDetail.isPresent()) {
            //값이 있는경우
            return true;
        } else {
            //값이 없는경우
            return false;
        }

    }

    // 모임정보가져오기(+멤버정보)
    public Map<String,Object> meetDetail(long meetId, Long loginId) {
        Map<String,Object> maps = new HashMap<>();
        // meetId로 해당 모임 찾기
        Optional<MeetEntity> meetEntity = meetRepository.findById(meetId);
        if (meetEntity.isPresent()) {
            Map<String,Object> map = new HashMap<>();
            maps.put("meet",meetEntity.get());
        }
        // 해당모임에 참여한 멤버정보 찾아오자
        List<MeetMembersEntity> mmEntity = meetMembersRepository.findByMeetMeetId(meetId);
        List<MeetMembersDTO> mmList = new ArrayList<>();
        System.out.println("size:"+mmEntity.size());
        for(MeetMembersEntity m: mmEntity){
            MeetMembersDTO dto = meetMembersRepository.findMemberDetails(m.getMemberId().getMemberId());
            if(dto != null){
                mmList.add(dto);
                System.out.println(dto.getProfileNickname());
            }

            // 로그인한 회원이 해당모임에 참여했는지 확인
            if(m.getMemberId().getMemberId() == loginId){
                Map<String,Object> map = new HashMap<>();
                maps.put("attendYN","Y");
            }
        }

        maps.put("meetMembers",mmList);
        return maps;
    }

    // 모임참여
    public void meetMemberAdd(long meetId, MemberEntity loginMember) {
        Optional<MeetEntity> entity = meetRepository.findById(meetId);
        MeetMembersEntity mmEntity = new MeetMembersEntity(entity.get(),loginMember);
        // 모임 visible 상태 변경
        if(entity.isPresent() && (entity.get().getMaxNumber() - entity.get().getCurrNumber() == 1)){
            meetRepository.updateVisible(meetId,0);
        }

        // meetMembers table 값추가
        meetMembersRepository.save(mmEntity);
        // meet table currNumber 값 증가시켜주기
        meetRepository.incrementCurrNumber(meetId);
        // memberDetail table palyNumber 값 증가시켜주기
        memberDetailRepository.incrementPlayNumber(loginMember.getMemberId());
    }
    
    // 모임나가기
    public void meetMemberDelete(long meetId, Long memberId) {
        Optional<MeetEntity> meetEntity = meetRepository.findById(meetId);
        // 모임 visible 상태 변경
        if(meetEntity.get().getVisible() == 0){
            meetRepository.updateVisible(meetId,1);
        }
        // meetMember에서 삭제
        meetMembersRepository.deleteByMeetIdAndMemberId(meetId, memberId);
        // 모임 참여자수 감소
        meetRepository.decrementCurrNumber(meetId);
        // 모임 참여 횟수 감소시키기
        memberDetailRepository.decrementPlayNumber(memberId);
    }

    // 페이징 처리하여 전체 meet list 가져오기
    public Page<MeetEntity> meetList(Pageable pageable) {
        return  meetRepository.findByActiveAndVisibleOrderByMeetId(pageable);
    }

    // meet 내용 수정
    public void updateMeet(Long meetId, MeetEntity updatedMeet) {
        // 기존 모임 엔티티를 가져옴
        MeetEntity existingMeet = meetRepository.findById(meetId).orElse(null);
        System.out.println("기존 모임 엔티티를 가져옴"+existingMeet.getMeetId());

        int visible = existingMeet.getVisible();
        // 현재인원이  변경할 모집인원과 동일할시 visible 비공개로 변경 값 0으로
        if(updatedMeet.getMaxNumber() == existingMeet.getCurrNumber()){
            visible = 0;
        }
        // 현재 visible 값이 비공개(0)인데 현재인원이 변경할 모집인원과 동일하지 않을시 공개(1)로 값변경
        if(visible == 0 && updatedMeet.getMaxNumber() > existingMeet.getCurrNumber()){
            visible = 1;
        }

        if (existingMeet != null) {
            existingMeet.update(updatedMeet.getMeetName()
                    ,updatedMeet.getMeetInfo()
                    ,updatedMeet.getMeetDate()
                    ,updatedMeet.getMaxNumber()
                    ,updatedMeet.getSportsId()
                    ,updatedMeet.getMergeYn()
                    ,new Timestamp(System.currentTimeMillis())
                    ,visible);
            meetRepository.save(existingMeet);
        }
    }

    // 모임모아보기 검색
    public List<MeetEntity> searchMeetList(SearchDTO searchDTO) {
        // 현재 날짜 및 시간을 Timestamp로 변환
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        LocalDateTime currentDateTime = currentTimestamp.toLocalDateTime();

        // 일주일 후의 날짜 계산
        LocalDateTime aWeekLaterDateTime = currentDateTime.plusWeeks(1);
        Timestamp aWeekAgo = Timestamp.valueOf(aWeekLaterDateTime);

        // 한 달 후의 날짜 계산
        LocalDateTime aMonthLaterDateTime = currentDateTime.plusMonths(1);
        Timestamp aMonthAgo = Timestamp.valueOf(aMonthLaterDateTime);

        Timestamp startDate = new Timestamp(System.currentTimeMillis());
        Timestamp endDate = new Timestamp(System.currentTimeMillis());

        if(searchDTO.getStartDate() != "" || searchDTO.getEndDate() != ""){
            startDate = Timestamp.valueOf(searchDTO.getStartDate()+" 00:00:00");
            endDate = Timestamp.valueOf(searchDTO.getEndDate()+" 00:00:00");
            System.out.println("startDate"+searchDTO.getStartDate()+" 00:00:00");
            System.out.println("endDate"+searchDTO.getEndDate()+" 00:00:00");
        }

        // Calendar를 사용하여 1일을 더함
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endDate.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        // 1일이 추가된 날짜를 새로운 Timestamp로 생성
        Timestamp endDatePlusOneDay = new Timestamp(calendar.getTimeInMillis());

        // 이제 endDatePlusOneDay 변수를 사용하면 됩니다.
        // 예시로 출력
        System.out.println("원래 endDate: " + endDate);
        System.out.println("endDate에 1일 추가: " + endDatePlusOneDay);
        System.out.println("1개월::" + aMonthAgo);
        System.out.println("1주::" + aWeekAgo);


        return meetRepository.searchMeets(
                searchDTO.getSearchPeriod(),
                startDate,
                endDatePlusOneDay,
                searchDTO.getSearchType(),
                Long.parseLong(searchDTO.getSportsId()),
                searchDTO.getKeyword(),
                aWeekAgo,
                aMonthAgo,
                currentTimestamp
        );
    }

    // regdate를 기준으로 내림차순 정렬하여 최상위 5개를 가져옵니다.
    public List<MeetEntity> getMeetTop5() {
        List<MeetEntity> top5Meetings = meetRepository.findTop5ByActiveAndVisibleOrderByRegdateDescMeetDateDesc(1, 1);
        return top5Meetings;
    }
}
