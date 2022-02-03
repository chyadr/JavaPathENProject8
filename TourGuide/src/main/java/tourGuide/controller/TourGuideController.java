package tourGuide.controller;

import java.util.List;
import java.util.stream.Collectors;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.api.ApiClient;
import tourGuide.dto.ClosestFiveTouristAttractionsDTO;
import tourGuide.dto.RecentLocationDTO;
import tourGuide.dto.UserPreferenceDTO;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

import javax.validation.Valid;

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
		return JsonStream.serialize(visitedLocation != null ? visitedLocation.location : "");
    }

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

    /**
     * updates the preferences of the user given by name
     *
     * @param userName
     * @param userPreferencesDTO
     * @return UserPreferences
     */
    @PutMapping("/addUserPreferences")
    private UserPreferenceDTO addUserPreferences(@Valid @RequestBody UserPreferenceDTO userPreferencesDTO, @RequestParam(required = true) String userName ) {

        return tourGuideService.addUserPreferences( userName, userPreferencesDTO);
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    /**
     * Serve to gather all user's current location based on their stored location history
     * @return
     */
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
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
   
    @RequestMapping("/getAllUsersLocation")
    public String getAllUsersLocation(){
        List<Location> allUsersLocation = tourGuideService.getAllUsers().stream()
                    .map(user -> apiClient.getUserLocation(user.getUserId()))
                    .map(response -> response.location)
                    .collect(Collectors.toList());

        return JsonStream.serialize(allUsersLocation);
    }

    /**
     * Serve to get all the user's names for once
     * @return
     */
    @RequestMapping("/getAllUsersNames")
    public String getUserAllUsersNames(){

        return JsonStream.serialize(tourGuideService.getAllUsers().stream().map(User::getUserName).collect(Collectors.toList()));
    }

}