package com.universe.wujuplay.message.model;

import com.universe.wujuplay.chat.model.ChatDTO;
import com.universe.wujuplay.chat.model.ChatEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.model.MemberResponse;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@NoArgsConstructor
@Table(name = "MESSAGE")
@Builder
@Getter
@AllArgsConstructor
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long msgId;

    private long chatRoomId;

    @ManyToOne
    @JoinColumn(name = "sender")
    private MemberEntity sender;

    private String content;
    @CreationTimestamp
    private LocalDateTime date;

    private String messageType;

    public static MessageDTO toDTO(MessageEntity messageEntity){
        return MessageDTO.builder()
            .messageType(messageEntity.getMessageType())
            .msgId(messageEntity.getMsgId())
            .chatRoomId(messageEntity.getChatRoomId())
            .sender(MemberEntity.toResponseDTO(messageEntity.getSender()))
            .content(messageEntity.getContent())
            .date(messageEntity.getDate()).build();
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
