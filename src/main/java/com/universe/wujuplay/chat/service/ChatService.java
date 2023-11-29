package com.universe.wujuplay.chat.service;

import com.universe.wujuplay.chat.model.ChatDTO;
import com.universe.wujuplay.chat.model.ChatEntity;
import com.universe.wujuplay.chat.repository.ChatRepository;
import com.universe.wujuplay.message.model.MessageDTO;
import com.universe.wujuplay.message.repository.MessageRepository;
import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ServerEndpoint(value="/chat")
public class ChatService {

    private Map<Integer, ChatDTO> chatRoomMap;
    @Autowired
    private ChatRepository chatRepository;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }


    public void createNewChat(ChatDTO newChat) {
        chatRepository.save(ChatDTO.toEntity(newChat));
    }

    public void deleteChat(long id) {
        chatRepository.deleteById(id);
    }

//    public List<MemberDTO> listMembers(int chatRoomId) {
//        // TODO: ...
//    }

    public List<ChatDTO> findChatByMember(long memberId){
        List<ChatEntity> chatList = chatRepository.findByMemberId(memberId);
        return chatList
            .stream()
            .map(ChatEntity::toDTO)
            .toList();
    }

    public List<ChatDTO> listChats() {
        List<ChatEntity> chatList = chatRepository.findAll();
        return chatList
            .stream()
            .map(ChatEntity::toDTO)
            .toList();
    }

    public void activate(long chatroomId) {
        ChatEntity selChat = chatRepository.findById(chatroomId).get();
        selChat.setActive(1);
        chatRepository.save(selChat);
    }

    public void inactivate(long chatroomId) {
        ChatEntity selChat = chatRepository.findById(chatroomId).get();
        selChat.setActive(0);
        chatRepository.save(selChat);
    }

    public ChatDTO findChatById(long chatroomId) {
        return ChatEntity.toDTO(chatRepository.findById(chatroomId).get());
    }
}
