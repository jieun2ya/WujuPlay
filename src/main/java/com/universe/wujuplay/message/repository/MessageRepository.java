package com.universe.wujuplay.message.repository;

import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.message.model.MessageEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long>, CrudRepository<MessageEntity, Long> {

    @Query(value = "select * from message where message_type = :messageType AND chat_room_id = :chatRoomId and sender = :member order by date desc limit 1;", nativeQuery = true)
    Optional<MessageEntity> findEnterOrPauseMessage(long chatRoomId, MemberEntity member, String messageType);

    Optional<List<MessageEntity>> findAllByChatRoomIdAndDateAfterAndMessageTypeNot(Long chatRoomId, LocalDateTime date, String msgType);

    Optional<List<MessageEntity>> findAllByChatRoomId(long chatRoomId);


    Optional<MessageEntity> findByChatRoomIdAndSenderAndMessageType(long chatRoomId, MemberEntity entity, String msgType);
}
