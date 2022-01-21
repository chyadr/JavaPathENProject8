package com.trippricer.service;

import dto.PriceInputDTO;
import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;


@Service
public class TripPricerService implements ITripPricerService {
    private final TripPricer tripPricer;

    public TripPricerService(TripPricer tripPricer) {
        this.tripPricer = tripPricer;
    }

    @Override
    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        return tripPricer.getPrice(apiKey,attractionId,adults,children,nightsStay,rewardsPoints);
    }
}
