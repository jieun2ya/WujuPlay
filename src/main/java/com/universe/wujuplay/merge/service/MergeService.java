package com.universe.wujuplay.merge.service;


import com.universe.wujuplay.chat.model.ChatEntity;
import com.universe.wujuplay.chat.repository.ChatRepository;
import com.universe.wujuplay.meet.model.MeetDTO;
import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.meet.model.MeetMembersEntity;
import com.universe.wujuplay.meet.repository.MeetMembersRepository;
import com.universe.wujuplay.meet.repository.MeetRepository;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.merge.model.MergeEntity;
import com.universe.wujuplay.merge.repository.MergeRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MergeService {

    private final MergeRepository mergeRepository;

    private final MeetRepository meetRepository;

    private final MeetMembersRepository meetMembersRepository;

    private final ChatRepository chatRepository;


    public MeetEntity mergeProcess(long hostId, long enrollingId){
        MeetEntity hosting = meetRepository.findById(hostId).get();
        MeetEntity enrolling = meetRepository.findById(enrollingId).get();
        hosting.setActive(0);
        hosting.setVisible(0);
        hosting.setMerged(1);
        enrolling.setVisible(0);
        enrolling.setActive(0);
        enrolling.setMerged(1);
        meetRepository.save(hosting);
        meetRepository.save(enrolling);
        return createNewMeet(hosting, enrolling);
    }

    public MeetEntity createNewMeet(MeetEntity hosting, MeetEntity enrolling){
        List<MeetMembersEntity> totalMembers = meetMembersRepository
            .findByMeetOrMeet(hosting, enrolling);
        MeetDTO hostingDTO = MeetEntity.toDTO(hosting);
        int isVisible = 1;
        if (hostingDTO.getMaxNumber() == totalMembers.size()){
            isVisible = 0;
        }
        MeetDTO newMeet = MeetDTO.builder()
            .meetName(hostingDTO.getMeetName())
            .meetInfo(hostingDTO.getMeetInfo())
            .meetDate(hostingDTO.getMeetDate())
            .leader(hostingDTO.getLeader())
            .maxNumber(hostingDTO.getMaxNumber())
            .currNumber(totalMembers.size())
            .locationId(hostingDTO.getLocationId())
            .sportsId(hostingDTO.getSportsId())
            .active(1)
            .visible(isVisible)
            .mergeYn(hostingDTO.getMergeYn())
            .build();

        MeetEntity newMeetEntity = meetRepository.save(MeetDTO.toEntity(newMeet));
        ChatEntity chat = ChatEntity.builder().name(newMeetEntity.getMeetName()).meet(newMeetEntity).active(1).build();
        chatRepository.save(chat);
        fillMeetMembers(totalMembers, newMeetEntity);
        acceptMergeRequest(hosting, enrolling, newMeetEntity);
        return newMeetEntity;
    }

    public void fillMeetMembers(List<MeetMembersEntity> totalMembers, MeetEntity newMeetEntity) {
        for (MeetMembersEntity member : totalMembers){
            MeetMembersEntity newMember = MeetMembersEntity.builder()
                                    .memberId(member.getMemberId())
                                    .meet(newMeetEntity).build();
            // meetMembersRepository.delete(member); 로그에 필요할수도있으니 삭제는 보류
            meetMembersRepository.save(newMember);
        }
    }



    public void acceptMergeRequest(MeetEntity hosting, MeetEntity enrolling, MeetEntity newMeet) {
        MergeEntity mergeEntity = mergeRepository.findByHostingAndEnrolling(hosting, enrolling).get();
        mergeEntity.setMergeYn(1);
        mergeEntity.setAcceptDate(LocalDateTime.now());
        mergeEntity.setResultMeet(newMeet);
        mergeRepository.save(mergeEntity);
    }

    public List<MeetDTO> listMergeRequests(long hostId) {
        return meetRepository.findMeetsByHostingMeetId(hostId).get()
            .stream()
            .map(meetEntity ->MeetEntity.toDTO(meetEntity))
            .toList();
    }

    public MeetDTO findHost(long hostId) {
        return MeetEntity.toDTO(meetRepository.findById(hostId).get());
    }

    public MeetDTO findEnrolling(long requestId) {
        return MeetEntity.toDTO(meetRepository.findById(requestId).get());
    }

    public List<MeetEntity> findMergeable(long hostId, MemberEntity member){
        List<MeetEntity> resultSet = new ArrayList<>();
        MeetEntity myMeet = meetRepository.findById(hostId).get();
        List<MeetEntity> mergeableMeets = meetRepository.findMergeable(myMeet.getLocationId().getLocationId(),
            myMeet.getSportsId().getSportsId(),
            member.getMemberId(),
            myMeet.getMeetDate());


        for (MeetEntity meet : mergeableMeets){
            if (meet.getMaxNumber() - meet.getCurrNumber() >= myMeet.getCurrNumber()){
                boolean isAlreadyRequested = mergeRepository.findByHostingAndEnrolling(meet, myMeet).isPresent();
                boolean isAlreadyRequesting = mergeRepository.findByHostingAndEnrolling(myMeet, meet).isPresent();
                if (!isAlreadyRequested && !isAlreadyRequesting){
                    resultSet.add(meet);
                }
            }
        }
        return resultSet;
    }

    public void appendRequest(long hostId, long requestId) {
        MeetEntity host = meetRepository.findById(hostId).get();
        MeetEntity enrolling = meetRepository.findById(requestId).get();
        MergeEntity mergeEntity = MergeEntity.builder().hosting(host).enrolling(enrolling).mergeYn(0).build();
        mergeRepository.save(mergeEntity);
    }

    public List<MeetMembersEntity> getNewMeetMembers(MeetEntity newMeetEntity) {
        return meetMembersRepository.findAllByMeet(newMeetEntity);
    }
}
