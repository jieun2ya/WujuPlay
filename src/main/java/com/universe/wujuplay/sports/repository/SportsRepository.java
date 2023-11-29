package com.universe.wujuplay.sports.repository;

import com.universe.wujuplay.meet.model.MeetMembersEntity;
import com.universe.wujuplay.sports.model.SportsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportsRepository extends JpaRepository<SportsEntity, Long> {


}
