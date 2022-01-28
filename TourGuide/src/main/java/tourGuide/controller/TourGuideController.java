package tourGuide.controller;

import java.util.List;
import java.util.stream.Collectors;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.api.ApiClient;
import tourGuide.dto.ClosestFiveTouristAttractionsDTO;
import tourGuide.dto.RecentLocationDTO;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;
    @Autowired
    RewardsService rewardsService;
    @Autowired
    ApiClient apiClient;
	
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    @RequestMapping("/getLocation") 
    public String getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
    }
    
    //  TODO: Change this method to no longer return a List of Attractions.
 	//  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
 	//  Return a new JSON object that contains:
    	// Name of Tourist attraction, 
        // Tourist attractions lat/long, 
        // The user's location lat/long, 
        // The distance in miles between the user's location and each of the attractions.
        // The reward points for visiting each Attraction.
        //    Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        List<Attraction> attractions =tourGuideService.getNearByAttractions(visitedLocation);

        List<ClosestFiveTouristAttractionsDTO> closestFiveTouristAttractionsDTOS=attractions.stream().map(a -> {
            ClosestFiveTouristAttractionsDTO closestFiveTouristAttractionsDTO= new ClosestFiveTouristAttractionsDTO();
            closestFiveTouristAttractionsDTO.setAttractionName(a.attractionName);
            closestFiveTouristAttractionsDTO.setAttractionLatitude(a.latitude);
            closestFiveTouristAttractionsDTO.setAttractionLongitude(a.longitude);
            closestFiveTouristAttractionsDTO.setLocationLatitude(visitedLocation.location.latitude);
            closestFiveTouristAttractionsDTO.setLocationLongitude(visitedLocation.location.longitude);
            Location attractionLocation= new Location(a.latitude,a.longitude);
            closestFiveTouristAttractionsDTO.setDistance(rewardsService.getDistance(visitedLocation.location,attractionLocation));
            closestFiveTouristAttractionsDTO.setRewardPoints(apiClient.getAttractionRewardPoints(a.attractionId,visitedLocation.userId));
            return closestFiveTouristAttractionsDTO;
        }).collect(Collectors.toList());
    	return JsonStream.serialize(closestFiveTouristAttractionsDTOS);


    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }
    
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
    	// TODO: Get a list of every user's most recent location as JSON
    	//- Note: does not use gpsUtil to query for their current location,
    	//        but rather gathers the user's current location from their stored location history.
    	//
    	// Return object should be the just a JSON mapping of userId to Locations similar to:
    	//     {
    	//        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
    	//        ...
    	//     }
        List<User> users = tourGuideService.getAllUsers();
        List<RecentLocationDTO> recentLocationDTOS = users.stream().map(
                user -> user.getVisitedLocations().stream()
                        .sorted((a,b) -> b.timeVisited.compareTo(a.timeVisited)).limit(1).collect(Collectors.toList()))
                .collect(Collectors.toList()).stream().flatMap(List::stream).map(l -> {
                    RecentLocationDTO recentLocationDTO=new RecentLocationDTO();
                    recentLocationDTO.setUserId(l.userId.toString());
                    recentLocationDTO.setLatitude(l.location.latitude);
                    recentLocationDTO.setLongitude(l.location.longitude);
                    recentLocationDTO.setFormattedTimeVisited(l.timeVisited.toString());
                    return recentLocationDTO;
                }).collect(Collectors.toList());
    	
    	return JsonStream.serialize(recentLocationDTOS);
    }
    
    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
    	return JsonStream.serialize(providers);
    }
    
    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   
    @RequestMapping("/getAllVisitedLocation")
    public String getUserAllVisitedLocation(){
        List<VisitedLocation> allVisitedLocations = tourGuideService.getAllUsers().stream().map(User::getVisitedLocations).flatMap(List::stream).collect(Collectors.toList());

        return JsonStream.serialize(allVisitedLocations);
    }

    @RequestMapping("/getAllUsersNames")
    public String getUserAllUsersNames(){

        return JsonStream.serialize(tourGuideService.getAllUsers().stream().map(User::getUserName).collect(Collectors.toList()));
    }

}