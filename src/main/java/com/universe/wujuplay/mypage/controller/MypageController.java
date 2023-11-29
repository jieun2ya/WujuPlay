package com.universe.wujuplay.mypage.controller;


import com.universe.wujuplay.chat.model.ChatDTO;
import com.universe.wujuplay.meet.model.MeetDTO;
import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.meet.service.MeetService;
import com.universe.wujuplay.member.model.MemberDetailDTO;
import com.universe.wujuplay.member.model.MemberDetailEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.model.MemberResponse;
import com.universe.wujuplay.member.service.MemberService;
import com.universe.wujuplay.message.service.MessageService;
import com.universe.wujuplay.mypage.model.ResponseMeetDTO;
import com.universe.wujuplay.review.model.ResponseReviewDTO;
import com.universe.wujuplay.review.service.ReviewService;
import com.universe.wujuplay.sports.model.SportsEntity;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/mypage")
@Controller
@RequiredArgsConstructor
public class MypageController {

    private final MeetService meetService;
    private final MemberService memberService;
    private final ReviewService reviewService;
    private final MessageService messageService;


    // 마이페이지
    @GetMapping
    public String mypage(HttpServletRequest request, Model model) {
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        MemberEntity loginMemberEntity = memberService.findById(memberId);
        List<MeetDTO> myMeet = meetService.myMeet(loginMemberEntity);
        model.addAttribute("myMeet", myMeet);
        model.addAttribute("memberId", memberId);
        return "mypage/mypage";
    }

    // 참여 중인 모임
    @GetMapping("/myenrolled")
    public String myenrolled(HttpServletRequest request, Model model) {
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        MemberEntity loginMemberEntity = memberService.findById(memberId);
        List<MeetDTO> myMeet = meetService.myEnrolledMeet(loginMemberEntity);
        model.addAttribute("myMeet", myMeet);
        model.addAttribute("memberId", memberId);
        return "mypage/mypage";
    }

    // 연 모임
    @GetMapping("/myhosting")
    public String myhosting(HttpServletRequest request, Model model) {
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        MemberResponse memberResponse = MemberEntity.toResponseDTO(memberService.findById(memberId));
        List<MeetDTO> myMeet = meetService.hostingMeets(memberResponse);
        model.addAttribute("myMeet", myMeet);
        model.addAttribute("memberId", memberId);
        return "mypage/mypage";
    }

    // 내가 모집 중
    @GetMapping("/hosting")
    public String hosting(Model model,HttpServletRequest request) {
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        MemberEntity loginMemberEntity = memberService.findById(memberId);
        model.addAttribute("hosting", meetService.hostingMeets(MemberEntity.toResponseDTO(loginMemberEntity)));
        model.addAttribute("memberId", memberId);
        return "mypage/hosting";
    }

    // 완료된 내 모임
    @GetMapping("/previousMeet")
    public String previousMeet(HttpServletRequest request, Model model) {

        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);

        List<ResponseMeetDTO> responseMeetDTOList = meetService.previousMeet(memberId);

        model.addAttribute("previousMeet", responseMeetDTOList);
        model.addAttribute("memberId", memberId);
        return "mypage/previousMeet";
    }

    // 내 정보 보기+수정페이지
    @GetMapping("/myinfo")
    public String myinfo(HttpServletRequest request, Model model) {

        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);

        MemberEntity loginMemberEntity = memberService.findById(memberId);
        MemberResponse memberResponse = MemberEntity.toResponseDTO(loginMemberEntity);

        model.addAttribute("member", memberResponse);

        MemberDetailDTO memberDetailDTO = null;
        MemberDetailEntity memberDetailEntity = memberService.findDetailByMemberId(memberId);
        if (memberDetailEntity != null) {
            memberDetailDTO = MemberDetailDTO.from(memberDetailEntity);
        }
        System.out.println("memberDetail" + memberDetailDTO);
        model.addAttribute("memberDetail", memberDetailDTO);
        model.addAttribute("memberId", memberId);
        return "mypage/myinfo";
    }

    // 내 정보 수정 처리
    @PostMapping("/myinfo")
    public String updateInfo(HttpServletRequest request, @ModelAttribute("member") MemberResponse memberResponse, @ModelAttribute("memberDetail") MemberDetailDTO memberDetailDTO) {

        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);

        memberService.update(memberId, memberResponse);
        memberService.updateDetail(memberId, memberDetailDTO);

        return "redirect:/mypage/myinfo";
    }

}
