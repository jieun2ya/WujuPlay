package com.universe.wujuplay.meet.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Table(name="LOCATION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LocationEntity {

    @Id
    private Long locationId;
    private String addressName;
    private String placeName;
    private String x;
    private String y;
    private String phone;
    private String placeUrl;

    @Builder
    public LocationEntity(Long locationId, String addressName, String placeName, String x, String y, String phone, String placeUrl) {
        this.locationId = locationId;
        this.addressName = addressName;
        this.placeName = placeName;
        this.x = x;
        this.y = y;
        this.phone = phone;
        this.placeUrl = placeUrl;
    }

    public static LocationDTO toDTO(LocationEntity locationEntity) {
        return LocationDTO.builder()
                .locationId(locationEntity.getLocationId())
                .addressName(locationEntity.getAddressName())
                .placeName(locationEntity.getPlaceName())
                .x(locationEntity.getX())
                .y(locationEntity.getY())
                .phone(locationEntity.getPhone())
                .placeUrl(locationEntity.getPlaceUrl())
                .build();
    }
}
