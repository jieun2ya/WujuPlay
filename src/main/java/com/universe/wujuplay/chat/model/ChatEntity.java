package com.universe.wujuplay.chat.model;


import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.message.model.MessageEntity;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "CHAT")
@Builder
@AllArgsConstructor
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private long chatRoomId;

    @OneToOne
    @JoinColumn(name = "meet_id")
    private MeetEntity meet;

    private String name;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private int active;

    public static ChatDTO toDTO(ChatEntity chatEntity){
        return ChatDTO.builder().
            name(chatEntity.getName())
            .chatRoomId(chatEntity.getChatRoomId())
            .meet(chatEntity.getMeet())
            .createdDate(chatEntity.getCreatedDate())
            .active(chatEntity.getActive()).build();
    }

    public void setActive(int active) {
        this.active = active;
    }
}
