package tourGuide.api;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import tourGuide.dto.*;
import tripPricer.Provider;


import java.util.*;
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


         VisitedLocationDTO response=  restTemplate.postForEntity(gpsUtilUrl +"/api/user-location",param, VisitedLocationDTO.class).getBody();
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
        return restTemplate.postForEntity(rewardCentralUrl +"/api/reward-point",attractionRewardPointsDTO,Integer.class).getBody();
    }

    public  List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints){
        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setApiKey(apiKey);priceDTO.setAttractionId(attractionId);
        priceDTO.setAdults(adults);priceDTO.setChildren(children);
        priceDTO.setNightsStay(nightsStay);priceDTO.setRewardsPoints(rewardsPoints);

        ProviderDTO[] response = restTemplate.postForEntity(tripPricerUrl +"/api/price", priceDTO,ProviderDTO[].class).getBody();
        return Arrays.stream(response).map(p -> new Provider(p.getTripId(),p.getName(),p.getPrice())).collect(Collectors.toList());

    }


}
