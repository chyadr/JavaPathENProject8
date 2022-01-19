package tourGuide.service;

import java.util.List;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import rewardCentral.RewardCentral;
import tourGuide.api.ApiClient;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsUtil gpsUtil;
	private final ApiClient apiClient;
	private final RewardCentral rewardsCentral;
	@Value("${tourguide.testMode:true}")
	private boolean testMode=true;



	public RewardsService(GpsUtil gpsUtil, ApiClient apiClient, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.apiClient = apiClient;
		this.rewardsCentral = rewardCentral;
	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}
	
	public void calculateRewards(User user) {
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = null;
		if(testMode){
			gpsUtil.getAttractions();
		}else {
			attractions = apiClient.getAttractions();
		}


	List<Attraction> finalAttractions = attractions;
		userLocations.
				forEach(ul -> {
					finalAttractions.stream().
							filter(attraction -> user.getUserRewards().stream().
									noneMatch(r -> r.attraction.attractionName.equals(attraction.attractionName)) && nearAttraction(ul, attraction)).
							forEach(att -> user.addUserReward(new UserReward(ul, att, getRewardPoints(att, user))));});
	}

	public void calculateRewards(List<User> users) {
		List<Attraction>  attractions;
		if (testMode) {
			attractions = gpsUtil.getAttractions();
		}
		else {attractions = apiClient.getAttractions();}

		users.stream().parallel().forEach(user -> user.getVisitedLocations().forEach(ul -> attractions.stream().
				filter(attraction -> user.getUserRewards().stream().
						noneMatch(r -> r.attraction.attractionName.equals(attraction.attractionName)) && nearAttraction(ul, attraction)).
				forEach(att -> user.addUserReward(new UserReward(ul, att, getRewardPoints(att, user))))));

	}
	
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return !(getDistance(attraction, location) > attractionProximityRange);
	}
	
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return !(getDistance(attraction, visitedLocation.location) > proximityBuffer);
	}
	
	private int getRewardPoints(Attraction attraction, User user) {
		if (testMode){
			rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
		}
		return  apiClient.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
	
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
		return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
	}

}
