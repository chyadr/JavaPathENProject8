package tourGuide.api;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestTemplate;
import tourGuide.dto.*;
import tripPricer.Provider;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ApiClient {

    @Value("${tourguide.gpsutil.url}")
    private String gpsUtilUrl;

    @Value("${tourguide.rewardcentral.url}")
    private String rewardCentralUrl;

    @Value("${tourguide.trippricer.url}")
    private String tripPricerUrl;

    private final RestTemplate restTemplate;

    public ApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public VisitedLocation getUserLocation(UUID param){
         VisitedLocationDTO response=  restTemplate.getForEntity(gpsUtilUrl +"/api/user-location", VisitedLocationDTO.class,param).getBody();
        return new VisitedLocation(response.getUserId(), new Location(response.getLocation().getLatitude(),response.getLocation().getLongitude()), response.getTimeVisited());
    }

    public List<Attraction> getAttractions(){
        AttractionDTO[] response = restTemplate.getForEntity(gpsUtilUrl +"/api/attractions",AttractionDTO[].class).getBody();

        return Arrays.stream(response).map(a -> new Attraction(a.getAttractionName(),a.getCity(),a.getState(),a.getLatitude(),a.getLongitude())).collect(Collectors.toList());

    }


    public int getAttractionRewardPoints (UUID attractionId,UUID userId){
        AttractionRewardPointsDTO attractionRewardPointsDTO= new AttractionRewardPointsDTO();
        attractionRewardPointsDTO.setAttractionId(attractionId);
        attractionRewardPointsDTO.setUserId(userId);
        return restTemplate.getForEntity(rewardCentralUrl +"/api/reward-point",Integer.class, attractionId).getBody();
    }

    public  List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints){
        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setApiKey(apiKey);priceDTO.setAttractionId(attractionId);
        priceDTO.setAdults(adults);priceDTO.setChildren(children);
        priceDTO.setNightsStay(nightsStay);priceDTO.setRewardsPoints(rewardsPoints);

        ProviderDTO[] response = restTemplate.getForEntity(tripPricerUrl +"/api/price", ProviderDTO[].class, priceDTO).getBody();
        return Arrays.stream(response).map(p -> new Provider(p.getTripId(),p.getName(),p.getPrice())).collect(Collectors.toList());

    }


}
