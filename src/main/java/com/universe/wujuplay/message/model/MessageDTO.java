package com.universe.wujuplay.message.model;

import com.universe.wujuplay.chat.model.ChatDTO;
import com.universe.wujuplay.chat.model.ChatEntity;

import com.universe.wujuplay.member.model.MemberResponse;
import java.awt.TrayIcon.MessageType;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MessageDTO {

    private String messageType;
    private long msgId;
    private long chatRoomId;
    private MemberResponse sender;
    private String content;
    private LocalDateTime date;

    public static MessageEntity toEntity(MessageDTO messageDTO) {
        return MessageEntity.builder()
                .messageType(messageDTO.getMessageType())
                .msgId(messageDTO.getMsgId())
                .chatRoomId(messageDTO.getChatRoomId())
                .sender(MemberResponse.toEntity(messageDTO.getSender()))
                .content(messageDTO.getContent())
                .date(messageDTO.getDate()).build();
    }
}
