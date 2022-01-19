package com.gpsutil.controller;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class GpsUtilController {

    private final GpsUtil gpsUtil;

    public GpsUtilController() {
        this.gpsUtil = new GpsUtil();
    }

    @GetMapping("/user-location")
    public VisitedLocation getUserLocation(@RequestBody UUID userId){
        return gpsUtil.getUserLocation(userId);
    }

    @GetMapping("/attractions")
    public List<Attraction> getAttractions(){
        return gpsUtil.getAttractions();
    }

}
