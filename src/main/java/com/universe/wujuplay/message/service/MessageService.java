package com.universe.wujuplay.message.service;

import com.universe.wujuplay.chat.model.ChatDTO;
import com.universe.wujuplay.chat.model.ChatEntity;
import com.universe.wujuplay.chat.repository.ChatRepository;
import com.universe.wujuplay.meet.model.MeetMembersEntity;
import com.universe.wujuplay.meet.repository.MeetMembersRepository;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.model.MemberResponse;
import com.universe.wujuplay.message.model.MessageDTO;
import com.universe.wujuplay.message.model.MessageEntity;
import com.universe.wujuplay.message.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    private final SimpMessageSendingOperations messagingTemplate;

    private final ChatRepository chatRepository;

    private final MeetMembersRepository meetMembersRepository;

    public List<MessageDTO> showLog(long chatRoomId, MemberEntity member) {
        Optional<MessageEntity> enterMsg = messageRepository.findEnterOrPauseMessage(chatRoomId, member, "enter");
        if (enterMsg.isPresent()) {
            List<MessageEntity> messagesAfterEnter = messageRepository
                .findAllByChatRoomIdAndDateAfterAndMessageTypeNot(chatRoomId, enterMsg.get().getDate(), "pause").get();
            return messagesAfterEnter
                .stream()
                .map(
                    MessageEntity::toDTO)
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public void updatePauseMsg(long chatRoomId, MemberResponse memberResponse){
        Optional<MessageEntity> pauseMsg = messageRepository.findEnterOrPauseMessage(chatRoomId,
            MemberResponse.toEntity(memberResponse), "pause");

        if (pauseMsg.isPresent()){
            pauseMsg.get().setDate(LocalDateTime.now());
        }
        else {
            MessageEntity newPause = MessageEntity.builder()
                .messageType("pause")
                .chatRoomId(chatRoomId)
                .sender(MemberResponse.toEntity(memberResponse))
                .build();
            messageRepository.save(newPause);
        }
    }


    public Map<ChatDTO, Integer> countUnreadMessages(MemberEntity member){
        List<ChatEntity> chatList = chatRepository.findByMemberId(member.getMemberId());
        List<ChatDTO> chatRooms = chatList.stream().map(ChatEntity::toDTO).toList();
        Map<ChatDTO, Integer> unreadMessageCounts = new HashMap<>();
        for (ChatDTO chat : chatRooms) {
            Optional<MessageEntity> pauseMsg = messageRepository.findEnterOrPauseMessage(chat.getChatRoomId(),
                member, "pause");
            if (pauseMsg.isPresent()) {
                List<MessageEntity> unread = messageRepository.findAllByChatRoomIdAndDateAfterAndMessageTypeNot(
                    chat.getChatRoomId(),
                    pauseMsg.get().getDate(),"pause").get();
                unreadMessageCounts.put(chat, unread.size());
            }
            else{
                unreadMessageCounts.put(chat, 0);
            }
        }
        return unreadMessageCounts;
    }

    public List<MeetMembersEntity> sendMsg(MessageDTO message) {
        message.setMessageType("message");
        messageRepository.save(MessageDTO.toEntity(message));
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatRoomId(), message);
        return meetMembersRepository.findMembersByChatRoomId(message.getChatRoomId()).orElse(null);
    }


    public void enterChat(long meetId, MemberEntity member) {
        ChatEntity chat = chatRepository.findByMeetMeetId(meetId);
        MessageEntity messageEntity = MessageEntity
            .builder()
            .chatRoomId(chat.getChatRoomId())
            .sender(member)
            .content(member.getProfileNickname() + "님이 모임에 참여하셨습니다.")
            .messageType("enter")
            .build();
        messageRepository.save(messageEntity);
        messagingTemplate.convertAndSend("/sub/chat/room/" + chat.getChatRoomId(), MessageEntity.toDTO(messageEntity));
    }

    public void leaveChat(long meetId, MemberEntity member) {
        ChatEntity chat = chatRepository.findByMeetMeetId(meetId);
        MessageEntity messageEntity = MessageEntity
            .builder()
            .chatRoomId(chat.getChatRoomId())
            .sender(member)
            .content(member.getProfileNickname() + "님이 모임을 떠나셨습니다.")
            .messageType("leave")
            .build();
        messageRepository.save(messageEntity);
        messagingTemplate.convertAndSend("/sub/chat/room/" + chat.getChatRoomId(), MessageEntity.toDTO(messageEntity));
    }

    public List<MessageDTO> showEntireLog(long chatRoomId) {
        List<MessageEntity> msgList = messageRepository.findAllByChatRoomId(chatRoomId).get();
        return msgList
            .stream()
            .map(
                MessageEntity::toDTO)
            .collect(Collectors.toList());

    }


    public void deletePause(long chatRoomId, MemberEntity member) {
        Optional<MessageEntity> pauseMsg = messageRepository.findByChatRoomIdAndSenderAndMessageType(
            chatRoomId,
            member,
            "pause");
        pauseMsg.ifPresent(messageRepository::delete);
    }

    public void setPause(long chatRoomId, MemberEntity member) {
        MessageEntity pauseMsg = MessageEntity.builder()
            .chatRoomId(chatRoomId)
            .sender(member)
            .messageType("pause")
            .build();
        messageRepository.save(pauseMsg);
    }

    public void setPauseWhenJoining(long meetId, MemberEntity member) {
        ChatEntity chat = chatRepository.findByMeetMeetId(meetId);
        MessageEntity pauseMsg = MessageEntity.builder()
            .chatRoomId(chat.getChatRoomId())
            .sender(member)
            .messageType("pause")
            .build();
        messageRepository.save(pauseMsg);
    }
}
