package com.universe.wujuplay.message.controller;


import com.universe.wujuplay.meet.model.MeetMembersEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.model.MemberResponse;
import com.universe.wujuplay.member.service.MemberService;
import com.universe.wujuplay.message.model.MessageDTO;
import com.universe.wujuplay.message.service.MessageService;
import com.universe.wujuplay.serverSide.NotificationService;
import java.net.http.HttpRequest;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MessageController {


    private final MessageService messageService;

    private final NotificationService notificationService;

    private final MemberService memberService;


    // admin 전용
    // 메시지 전체 로그 보기(list)
    @GetMapping("/message/{chatRoomId}/adminchatlog")
    private List<MessageDTO> showEntireLog(@PathVariable long chatRoomId){
       return messageService.showEntireLog(chatRoomId);
    }

    @GetMapping("/message/enterChatRoom")
    @ResponseBody
    public String deletePause(@RequestParam(name = "chatRoomId") long chatRoomId, HttpServletRequest request){
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        MemberEntity member = memberService.findById(memberId);
        messageService.deletePause(chatRoomId, member);
        return "Enter success";
    }


    @GetMapping("/message/leaveChatRoom")
    @ResponseBody
    public String setPause(@RequestParam(name = "chatRoomId") long chatRoomId, HttpServletRequest request){
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        MemberEntity member = memberService.findById(memberId);
        messageService.setPause(chatRoomId, member);
        return "Leave success";
    }

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
//    @MessageMapping(value = "/message/enter")
//    public void enter(MessageDTO message){
//        messageService.enterChat(message);
//    }


    //"/pub/chat/sends"
    @MessageMapping(value = "/message/send")
    public void message(MessageDTO message){
        List<MeetMembersEntity> participants = messageService.sendMsg(message);
        if (participants != null){
            participants.forEach(user -> notificationService.notify(user.getMemberId().getMemberId(), message.getChatRoomId()));
        }
    }

}
