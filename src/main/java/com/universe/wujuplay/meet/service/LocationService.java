package com.universe.wujuplay.meet.service;

import com.universe.wujuplay.meet.model.LocationDTO;
import com.universe.wujuplay.meet.model.LocationEntity;
import com.universe.wujuplay.meet.repository.LocationRepository;
import com.universe.wujuplay.meet.repository.MeetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final MeetRepository meetRepository;

    public List<Integer> saveLocations(List<LocationDTO> locationDTOList) {
        List<Integer> countList = new ArrayList<>();
        for (LocationDTO locationDTO : locationDTOList) {
            Optional<LocationEntity> existingLocation = locationRepository.findByLocationId(locationDTO.getLocationId());

            if (existingLocation.isPresent()) {
                // 이미 존재하는 경우
                LocationEntity locationEntity = existingLocation.get();
                int cnt = meetRepository.findMeetCountByLocationId(locationEntity.getLocationId());
                countList.add(cnt);
            } else {
                // 존재하지 않는 경우에만 저장
                LocationEntity locationEntity = LocationDTO.toEntity(locationDTO);
                LocationEntity savedLocation = locationRepository.save(locationEntity);
                int cnt = meetRepository.findMeetCountByLocationId(savedLocation.getLocationId());
                countList.add(cnt);
            }
        }
        return countList;
    }

}
