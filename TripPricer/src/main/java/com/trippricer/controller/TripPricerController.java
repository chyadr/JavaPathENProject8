package com.trippricer.controller;

import com.trippricer.service.ITripPricerService;
import com.trippricer.service.TripPricerService;
import dto.PriceInputDTO;
import org.springframework.web.bind.annotation.*;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TripPricerController {

    private final TripPricerService tripPricerService;

    public TripPricerController(TripPricerService tripPricerService) {
        this.tripPricerService = tripPricerService;
    }


    @PostMapping("/price")
    public List<Provider> getPrice(@RequestBody PriceInputDTO priceInputDTO){
        return tripPricerService.getPrice(priceInputDTO.getApiKey(),priceInputDTO.getAttractionId(),priceInputDTO.getAdults() ,priceInputDTO.getChildren(),priceInputDTO.getNightsStay(),priceInputDTO.getRewardsPoints());
    }

}
