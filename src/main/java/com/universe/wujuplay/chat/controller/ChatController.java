package com.universe.wujuplay.chat.controller;
import com.universe.wujuplay.chat.model.ChatDTO;
import com.universe.wujuplay.chat.service.ChatService;
import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.model.MemberResponse;
import com.universe.wujuplay.member.service.MemberService;
import com.universe.wujuplay.message.service.MessageService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chat")
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    private final MessageService messageService;

    private final MemberService memberService;



    // 특정 유저가 들어가있는 채팅방 목록, 유저채팅의 메인화면
    @GetMapping("/mychat")
    private String listChats(Model model, HttpServletRequest request){
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        MemberEntity member = memberService.findById(memberId);
        model.addAttribute("member", MemberEntity.toResponseDTO(member));
        return "chat/chatList";
    }

    //채팅방 정보 전달
    // stomp pub, sub용
    @GetMapping("/room/{roomId}")
    private ChatDTO roomDetails(@PathVariable long chatroomId, Model model){
        return chatService.findChatById(chatroomId);
    }


    //채팅방 입장
    @GetMapping("/chatroom/{chatroomId}")
    private String enterChatRoom(@PathVariable long chatroomId, Model model, HttpServletRequest request){
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        MemberEntity member = memberService.findById(memberId);
        model.addAttribute("member", MemberEntity.toResponseDTO(member));
        model.addAttribute("chatRoom", chatService.findChatById(chatroomId));
        model.addAttribute("chatLog", messageService.showLog(chatroomId, member));
        return "chat/chatRoomDetails";
    }

    @GetMapping("/unread")
    @ResponseBody
    public int getUnreadCounts(HttpServletRequest request, Model model){
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        MemberEntity member = memberService.findById(memberId);
        Map<ChatDTO, Integer> unreadCounts = messageService.countUnreadMessages(member);
        return unreadCounts.values().stream().mapToInt(v -> v).sum();
    }


    @GetMapping("/unreadmap")
    public String getUnreadMap(HttpServletRequest request, Model model){
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        MemberEntity member = memberService.findById(memberId);
        Map<ChatDTO, Integer> unreadCounts = messageService.countUnreadMessages(member);
        model.addAttribute("chatRoomsMap", unreadCounts);
        return "chat/chatmap-page";
//        return unreadCounts;
    }


    // 전체 (모든유저의) 채팅방 list, 관리자용
    @GetMapping("/list")
    private List<ChatDTO> listChats(){
        log.info("SHOW ALL ChatList {}", chatService.listChats());
        return chatService.listChats();
    }


    // 채팅방 생성
    @GetMapping("/create")
    private void createNewChat(MeetEntity meet){
        ChatDTO newChat = ChatDTO.builder().meet(meet).active(1).build();
        chatService.createNewChat(newChat);
    }

    // 채팅방 삭제
    @GetMapping("/delete/{id}")
    private void deleteChat(@PathVariable long id) {
        chatService.deleteChat(id);
    }


    // 채팅방에 있는 유저들 표시(meet_id로 meet조회해서 member_id 불러오기)
//    @GetMapping("{chatRoomId}/members")
//    private List<MemberDTO> listMembers(@PathVariable int chatRoomId){
//        return chatService.listMembers(chatRoomId);
//    }


    // 활성화
    @GetMapping("{id}/activate")
    private void activate(long id){
        chatService.activate(id);
    }

    //비활성화
    @GetMapping("{id}/inactivate")
    private void inactivate(long id){
        chatService.inactivate(id);
    }

}
