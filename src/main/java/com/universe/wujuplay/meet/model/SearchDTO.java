package com.universe.wujuplay.meet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SearchDTO {

    private String searchPeriod;
    private String searchType;
    private String sportsId;
    private String keyword;
    private String startDate;
    private String endDate;




}
