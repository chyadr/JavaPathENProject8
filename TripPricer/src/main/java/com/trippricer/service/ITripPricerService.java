package com.trippricer.service;

import dto.PriceInputDTO;
import org.springframework.web.bind.annotation.RequestBody;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

public interface ITripPricerService {
    List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints);

}
