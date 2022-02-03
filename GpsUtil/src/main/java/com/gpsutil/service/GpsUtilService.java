package com.gpsutil.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilService implements IGpsUtilService{

    private final GpsUtil gpsUtil;

    public GpsUtilService() {
        this.gpsUtil = new GpsUtil();
    }

    @Override
    public VisitedLocation getUserLocation(UUID userId) {
        return gpsUtil.getUserLocation(userId);
    }

    @Override
    public List<Attraction> getAttractions() {
        return gpsUtil.getAttractions();
    }
}
