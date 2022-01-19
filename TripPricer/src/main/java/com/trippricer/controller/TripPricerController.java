package com.trippricer.controller;

import dto.PriceInputDTO;
import org.springframework.web.bind.annotation.*;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TripPricerController {

    private final TripPricer tripPricer;

    public TripPricerController() {
        this.tripPricer = new TripPricer();
    }
    @GetMapping("/price")
    public List<Provider> getPrice(@RequestBody PriceInputDTO priceInputDTO){
        return tripPricer.getPrice(priceInputDTO.getApiKey(), priceInputDTO.getAttractionId(), priceInputDTO.getAdults(), priceInputDTO.getChildren(), priceInputDTO.getNightsStay(), priceInputDTO.getRewardsPoints());
    }

}
