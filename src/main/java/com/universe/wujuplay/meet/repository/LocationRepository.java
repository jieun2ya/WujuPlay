package com.universe.wujuplay.meet.repository;

import com.universe.wujuplay.meet.model.LocationEntity;
import com.universe.wujuplay.meet.model.MeetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long>{
    Optional<LocationEntity> findByLocationId(Long locationId);

}
