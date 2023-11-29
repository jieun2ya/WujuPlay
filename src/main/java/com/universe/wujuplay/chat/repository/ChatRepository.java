package com.universe.wujuplay.chat.repository;

import com.universe.wujuplay.chat.model.ChatDTO;
import com.universe.wujuplay.chat.model.ChatEntity;
import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.meet.model.MeetMembersEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long>, CrudRepository<ChatEntity, Long> {

    @Query(value = "select c.* from chat c join meet m on c.meet_id = m.meet_id join meet_members mm on m.meet_id = mm.meet_id where mm.member_id = :memberId AND m.active = 1", nativeQuery = true)
    List<ChatEntity> findByMemberId(@Param("memberId") long memberId);

    ChatEntity findByMeetMeetId(long meetId);

}

