package com.universe.wujuplay.merge.controller;


import com.universe.wujuplay.meet.model.MeetDTO;
import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.meet.model.MeetMembersEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.service.MemberService;
import com.universe.wujuplay.merge.service.MergeService;
import com.universe.wujuplay.message.service.MessageService;
import com.universe.wujuplay.serverSide.NotificationService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/merge")
public class MergeController {

    private final MergeService mergeService;
    private final MemberService memberService;
    private final MessageService messageService;
    private final NotificationService notificationService;

    @GetMapping("/requests")
    public String listMergeRequests(@RequestParam(name = "hostId") long hostId, Model model){
        model.addAttribute("requests", mergeService.listMergeRequests(hostId));
        return "mypage/requests-page";
//        System.out.println(mergeService.listMergeRequests(hostId));
//        return mergeService.listMergeRequests(hostId);
    }

    @PostMapping("/accept")
    public String mergeMeets(@RequestParam(name = "hostId") long hostId, @RequestParam(name = "requestId") long requestId, Model model){
        System.out.println("host: " + hostId + "enrolling=" + requestId);
        MeetEntity newMeetEntity = mergeService.mergeProcess(hostId, requestId);
        List<MeetMembersEntity> totalMembers = mergeService.getNewMeetMembers(newMeetEntity);
        for (MeetMembersEntity member : totalMembers){
            messageService.enterChat(newMeetEntity.getMeetId(), member.getMemberId());
            messageService.setPauseWhenJoining(newMeetEntity.getMeetId(), member.getMemberId());
        }
        return "merge/mergeComplete";
    }

    @GetMapping("/pending")
    public String pendMerge(@RequestParam(name = "hostMyMeetId") long hostId, @RequestParam(name = "requestId") long requestId, Model model){
        MeetDTO host = mergeService.findHost(hostId);
        MeetDTO enrolling = mergeService.findEnrolling(requestId);
        model.addAttribute("host", host);
        model.addAttribute("enrolling", enrolling);
        return "merge/mergeDetails";
    }

    @GetMapping("/find")
    public String findMergeables(@RequestParam(name = "hostId") long hostId, HttpServletRequest request, Model model){
        Long id = memberService.getMemberIdFromAccessTokenCookie(request);
        MemberEntity member = memberService.findById(id);
        List<MeetEntity> mergeableMeets = mergeService.findMergeable(hostId, member);
        model.addAttribute("meets", mergeableMeets);
        return "mypage/mergeableMeets-page";
//        return mergeableMeets;
    }

    @GetMapping("/suggest")
    public String suggestMerge(@RequestParam(name = "hostId") long hostId, @RequestParam(name = "enrollingMyMeetId") long requestId, Model model){
        MeetDTO host = mergeService.findHost(hostId);
        MeetDTO enrolling = mergeService.findEnrolling(requestId);
        model.addAttribute("host", host);
        model.addAttribute("enrolling", enrolling);
        return "merge/suggestDetails";
    }

    @PostMapping("/enrol")
    public String requestMerge(@RequestParam(name = "hostId") long hostId, @RequestParam(name = "requestId") long requestId, Model model){
        MeetDTO host = mergeService.findHost(hostId);
        mergeService.appendRequest(hostId, requestId);
        notificationService.notifyMergeRequests(host.getLeader().getMemberId(), host.getMeetName());
        return "merge/suggested";
    }
}
