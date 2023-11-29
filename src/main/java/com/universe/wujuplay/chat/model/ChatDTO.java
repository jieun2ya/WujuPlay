package com.universe.wujuplay.chat.model;

import com.universe.wujuplay.chat.service.ChatService;
import com.universe.wujuplay.meet.model.MeetDTO;
import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.message.model.MessageDTO;
import com.universe.wujuplay.message.model.MessageEntity;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.socket.WebSocketSession;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatDTO {
    private long chatRoomId;
    private String name;
//    private MeetDTO meet;
    private MeetEntity meet;
    private LocalDateTime createdDate;
    private int active;


    public static ChatEntity toEntity(ChatDTO chatDTO){
        return ChatEntity.builder()
                .name(chatDTO.getName())
                .chatRoomId(chatDTO.getChatRoomId())
                .meet(chatDTO.getMeet())
                .createdDate(chatDTO.getCreatedDate())
                .active(chatDTO.getActive()).build();
    }
}
