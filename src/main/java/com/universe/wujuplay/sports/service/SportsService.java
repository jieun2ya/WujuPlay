package com.universe.wujuplay.sports.service;

import com.universe.wujuplay.sports.model.SportsEntity;
import com.universe.wujuplay.sports.repository.SportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SportsService {

    private final SportsRepository sportRepository;

    public List<SportsEntity> getAllSports() {
        return (List<SportsEntity>) sportRepository.findAll();
    }
}
