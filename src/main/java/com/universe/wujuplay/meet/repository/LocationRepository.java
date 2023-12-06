package com.universe.wujuplay.meet.repository;

import com.universe.wujuplay.meet.model.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long>{

    // 전달된 locationId에 해당하는 location 정보 가져오기
    Optional<LocationEntity> findByLocationId(Long locationId);

}
