package com.gpsutil.controller;


import com.gpsutil.service.GpsUtilService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class GpsUtilController {

    private GpsUtilService gpsUtilService;

    public GpsUtilController(GpsUtilService gpsUtilService) {
        this.gpsUtilService = gpsUtilService;
    }

    @PostMapping("/user-location")
    public VisitedLocation getUserLocation(@RequestBody UUID userId){
        return gpsUtilService.getUserLocation(userId);
    }

    @GetMapping("/attractions")
    public List<Attraction> getAttractions(){
        return gpsUtilService.getAttractions();
    }

}
