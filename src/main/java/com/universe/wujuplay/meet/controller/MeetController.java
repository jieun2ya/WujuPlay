package com.universe.wujuplay.meet.controller;

import com.universe.wujuplay.meet.model.LocationDTO;
import com.universe.wujuplay.meet.model.LocationEntity;
import com.universe.wujuplay.meet.model.MeetDTO;
import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.meet.model.*;
import com.universe.wujuplay.meet.service.LocationService;
import com.universe.wujuplay.meet.service.MeetService;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.service.MemberService;
import com.universe.wujuplay.message.service.MessageService;
import com.universe.wujuplay.sports.model.SportsEntity;
import com.universe.wujuplay.sports.service.SportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/meet")
@Controller
public class MeetController {

    private final MeetService meetService;
    private final LocationService locationService;
    private final SportsService sportService;
    private final MessageService messageService;
    private final MemberService memberService;

    @GetMapping("/main")
    public String meetMain(Model model, HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long loginMemberId = 0L;

        if (authentication != null && authentication.isAuthenticated()) {
            // 사용자가 인증되어 있으면 ID를 가져옴
            loginMemberId = memberService.getMemberIdFromAccessTokenCookie(request);
        }
        model.addAttribute("memberId", loginMemberId);

        List<MeetEntity> meetList = meetService.getMeetTop5();
        if (meetList != null) {
            for (MeetEntity meetEntity : meetList) {
                System.out.println(meetEntity.toString());
            }
        } else {
            System.out.println("Meet list is null");
        }
        model.addAttribute("meetList", meetList);
        List<SportsEntity> sportsData = sportService.getAllSports();
        model.addAttribute("sportsData", sportsData);
        model.addAttribute("meet", new MeetEntity());

        return "meet/main";
    }

    // 모임열기
    @PostMapping("/add")
    public String meetAdd(@Validated MeetDTO meetDTO, HttpServletRequest request, Model model){
        meetDTO.setMeetDate(meetDTO.getMeetDate().replaceAll("T", " ")+":00");
        meetDTO.setActive(1); meetDTO.setVisible(1); meetDTO.setCurrNumber(1);

        Long id = memberService.getMemberIdFromAccessTokenCookie(request);
        model.addAttribute("memberId", id);
        MemberEntity loginMember = memberService.findById(id);
        meetDTO.setLeader(loginMember);

        // memberDetail 있는지 check
        if(!meetService.checkMemberDetail(id)){
            return "redirect:/auth/signupDetail";
        }
        MeetDTO savedDTO = meetService.save(MeetDTO.toEntity(meetDTO),loginMember);

        //채팅방에 참가 메시지 출력
        messageService.enterChat(savedDTO.getMeetId(), loginMember);
        messageService.setPauseWhenJoining(savedDTO.getMeetId(), loginMember);


        return "redirect:/meet/main";
    }

    // location table에 값 추가
    @PostMapping(value="/locationAdd", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> locationAdd(@RequestBody List<LocationDTO> request){
//        for (LocationDTO refinedPlace : request) {
//            System.out.println(refinedPlace); // 데이터 확인용
//        }
        try {
            List<Integer> countList = locationService.saveLocations(request);
            return ResponseEntity.ok(countList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    //필터적용안할시 filter = null
    @GetMapping("/list-meet/{filter}")
    public String listMeet(@PathVariable SportsEntity filter, Model model){
        model.addAttribute("searchResult",meetService.listMeet(filter));
        return "meet/listMeet";
    }

    // 해당 장소에 해당하는 meet(모임)list 가져오기
    @PostMapping(value="/listDetail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeetDTO>> listDetail(@RequestBody LocationDTO request){
        System.out.println("locationId"+request.getLocationId());
        LocationEntity entity = LocationDTO.toEntity(request);

//        for (LocationDTO refinedPlace : request) {
//            System.out.println(refinedPlace); // 데이터 확인용
//        }
        try {
            List<MeetDTO> meetList = meetService.findByLocationId(request);
            System.out.println("11111");
            return ResponseEntity.ok(meetList);
        } catch (Exception e) {
            System.out.println("22222");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // meet 상세정보 가져오기, 해당 모임 멤버 정보들까쥐~
    @GetMapping("/meetDetail/{meetId}")
    public String meetDetail(@PathVariable long meetId, HttpServletRequest request, Model model){
        System.out.println("meetDetail");
        System.out.println("meetID" + meetId);

        Long id = memberService.getMemberIdFromAccessTokenCookie(request);
        model.addAttribute("memberId", id);
        Long loginMemberId = 0L;

        if (id != null) {
            loginMemberId = id;
        }

        Map<String,Object> map = meetService.meetDetail(meetId,loginMemberId);
        model.addAttribute("meet",(MeetEntity)map.get("meet"));
        model.addAttribute("meetMembers",(List<MeetMembersDTO>)map.get("meetMembers"));
        model.addAttribute("attendYN",(String)map.get("attendYN"));
        model.addAttribute("loginMemberId",loginMemberId);
        System.out.println(map.get("meetMembers"));
        //System.out.println(((MeetDTO) map.get("meet")).getLeader().getMemberId());

        return "meet/meetView";
    }

    // 해당 모임에 참여되어있는지 확인
    @ResponseBody
    @GetMapping("checkMemberDetail")
    public String checkMemberDetail(HttpServletRequest request, Model model){
        Long loginMemberId = memberService.getMemberIdFromAccessTokenCookie(request);
        model.addAttribute("memberId", loginMemberId);
        System.out.println("loginId" + loginMemberId);

        if (loginMemberId == null) {
            System.out.println("dddd");
            return "3";
        }else{
            System.out.println("Ddd");
        }


        if(meetService.checkMemberDetail(loginMemberId)){
            return "1";
        }else {
            return "0";
        }
    }

    // 모임참여하기
    @GetMapping("/meetMemberAdd/{meetId}")
    public String meetMemberAdd(@PathVariable long meetId, HttpServletRequest request, Model model){

        Long id = memberService.getMemberIdFromAccessTokenCookie(request);
        model.addAttribute("memberId", id);
        MemberEntity loginMember = memberService.findById(id);

        meetService.meetMemberAdd(meetId,loginMember);
        messageService.enterChat(meetId,loginMember);
        messageService.setPauseWhenJoining(meetId, loginMember);
        return "redirect:/meet/meetDetail/"+ meetId;
    }

    // 모임나가기
    @ResponseBody
    @GetMapping("/meetMemberDelete/{meetId}")
    public void meetMemberDelete(@PathVariable long meetId, HttpServletRequest request, Model model){
        Long id = memberService.getMemberIdFromAccessTokenCookie(request);
        model.addAttribute("memberId", id);
        MemberEntity loginMember = memberService.findById(id);

        meetService.meetMemberDelete(meetId, loginMember.getMemberId());
        messageService.leaveChat(meetId, loginMember);
    }

    // mypage/hosting 에서 ajax통신에 필요해용
    @GetMapping("/info")
    public String meetInfos (@RequestParam(name = "hostId") long hostId, Model model){
        model.addAttribute("info", MeetEntity.toDTO(meetService.findById(hostId)));
        return "mypage/info-page";
//        return MeetEntity.toDTO(meetService.findById(hostId));
    }


    // meetList 전체 가져오기
    @GetMapping("/meetList")
    public String meetList(Model model,
                              @PageableDefault(page = 0, size = 10, sort = "meetId", direction = Sort.Direction.DESC) Pageable pageable
                                ,HttpServletRequest request){
        Long id = memberService.getMemberIdFromAccessTokenCookie(request);
        model.addAttribute("memberId", id);

        Page<MeetEntity> list = null;

        list = meetService.meetList(pageable);

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        List<SportsEntity> sportsData = sportService.getAllSports();
        model.addAttribute("sportsData", sportsData);
        model.addAttribute("searchDTO", new SearchDTO());
        //System.out.println("adress"+list.getContent().get(1).getLocationId().getPlaceName());

        return "meet/meetList";
    }

    @PostMapping("/search")
    public String searchMeetList(@ModelAttribute("searchDTO") SearchDTO searchDTO, Model model,HttpServletRequest request) {
        Long id = memberService.getMemberIdFromAccessTokenCookie(request);
        model.addAttribute("memberId", id);
        System.out.println(searchDTO);
        List<MeetEntity> list = meetService.searchMeetList(searchDTO);
        System.out.println("MeetEntity List:");
        list.forEach(meetEntity -> System.out.println(meetEntity));


        model.addAttribute("list", list);


        List<SportsEntity> sportsData = sportService.getAllSports();
        model.addAttribute("sportsData", sportsData);
        model.addAttribute("searchDTO",searchDTO);
        return "meet/meetSearchList";
    }

    // 모임세부정보 가져오기
    @ResponseBody
    @GetMapping("/meetDetailAjax/{meetId}")
    public MeetDetailDTO meetDetailAjax(@PathVariable long meetId, HttpServletRequest request, Model model){
        System.out.println("meetDetail");
        System.out.println("meetID" + meetId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long loginMemberId = 0L;

        if (authentication != null && authentication.isAuthenticated()) {
            // 사용자가 인증되어 있으면 ID를 가져옴
            loginMemberId = memberService.getMemberIdFromAccessTokenCookie(request);
        }
        model.addAttribute("memberId", loginMemberId);

        Map<String,Object> map = meetService.meetDetail(meetId,loginMemberId);
        MeetDetailDTO meetDetailDTO = new MeetDetailDTO(
                (MeetDTO) map.get("meet")
                ,(List<MeetMembersDTO>) map.get("meetMembers")
                ,(String) map.get("attendYN")
                ,loginMemberId);

        return meetDetailDTO;
    }

    @GetMapping("/meetUpdate/{meetId}")
    public String  meetUpdate(@PathVariable long meetId, Model model,HttpServletRequest request){
        Long id = memberService.getMemberIdFromAccessTokenCookie(request);
        model.addAttribute("memberId", id);

        MeetEntity meetEntity = MeetEntity.builder().build();
        meetEntity = meetService.findById(meetId);
        model.addAttribute("meetEntity",meetEntity);

        List<SportsEntity> sportsData = sportService.getAllSports();
        model.addAttribute("sportsData", sportsData);
        System.out.println(meetEntity.getMeetName());
        System.out.println(meetEntity.getCurrNumber());
        System.out.println(meetEntity);

        return "meet/meetUpdate";
    }

    @PostMapping("/meetUpdate")
    public String meetUpdate(@ModelAttribute("meet") MeetDTO meetDTO, HttpServletRequest request, Model model){
        Long id = memberService.getMemberIdFromAccessTokenCookie(request);
        System.out.println("updateId 내놔"+id);

        meetDTO.setMeetDate(meetDTO.getMeetDate().replaceAll("T", " ")+":00");
        System.out.println("meetUpdate");
        System.out.println("meetID"+meetDTO.getMeetId());
        System.out.println();
        meetService.updateMeet(meetDTO.getMeetId(),MeetDTO.toEntity(meetDTO));
        return "redirect:/meet/meetDetail/"+ meetDTO.getMeetId();
    }


}
