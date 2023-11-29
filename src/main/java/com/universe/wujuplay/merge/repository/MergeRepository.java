package com.universe.wujuplay.merge.repository;

import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.merge.model.MergeEntity;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MergeRepository extends JpaRepository<MergeEntity, Long>, CrudRepository<MergeEntity, Long> {

    MeetEntity findByHosting(Long hostingId);

    Optional<MergeEntity> findByHostingAndEnrolling(MeetEntity hosting, MeetEntity enrolling);




}
