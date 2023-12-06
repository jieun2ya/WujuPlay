package com.universe.wujuplay.meet.model;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {

    private Long locationId;
    private String addressName;
    private String placeName;
    private String x;
    private String y;
    private String phone;
    private String placeUrl;



    public static LocationEntity toEntity(LocationDTO locationDTO) {
        return LocationEntity.builder()
                .locationId(locationDTO.getLocationId())
                .addressName(locationDTO.getAddressName())
                .placeName(locationDTO.getPlaceName())
                .x(locationDTO.getX())
                .y(locationDTO.getY())
                .phone(locationDTO.getPhone())
                .placeUrl(locationDTO.getPlaceUrl())
                .build();
    }



}
