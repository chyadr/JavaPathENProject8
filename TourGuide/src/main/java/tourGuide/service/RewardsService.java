package tourGuide.service;

import java.util.List;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;

import rewardCentral.RewardCentral;
import tourGuide.api.ApiClient;
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
	private boolean unitTest =false;



	public RewardsService(GpsUtil gpsUtil, ApiClient apiClient, RewardCentral rewardCentral, boolean unitTest) {
		this.gpsUtil = gpsUtil;
		this.apiClient = apiClient;
		this.rewardsCentral = rewardCentral;
		this.unitTest = unitTest;
	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}
	
	public void calculateRewards(User user) {
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions;
		if(unitTest){
			attractions = gpsUtil.getAttractions();
			List<Attraction> finalAttractions = attractions;
			userLocations.
					forEach(ul -> {
						finalAttractions.stream().
								filter(attraction -> user.getUserRewards().stream().
										noneMatch(r -> r.attraction.attractionName.equals(attraction.attractionName)) && nearAttraction(ul, attraction)).
								forEach(att -> user.addUserReward(new UserReward(ul, att, getRewardPoints(att, user))));});
		}else {
			attractions = apiClient.getAttractions();
			List<Attraction> finalAttractions = attractions;
			userLocations.
					forEach(ul -> {
						finalAttractions.stream().
								filter(attraction -> user.getUserRewards().stream().
										noneMatch(r -> r.attraction.attractionName.equals(attraction.attractionName))).
								forEach(att -> user.addUserReward(new UserReward(ul, att, getRewardPoints(att, user))));});
		}



	}

	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return !(getDistance(attraction, location) > attractionProximityRange);
	}

	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return !(getDistance(attraction, visitedLocation.location) > proximityBuffer);
	}
	
	private int getRewardPoints(Attraction attraction, User user) {
		if (unitTest){
			return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
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
