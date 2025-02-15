package tourGuide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import org.springframework.util.CollectionUtils;
import tourGuide.api.ApiClient;
import tourGuide.dto.UserPreferenceDTO;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode=true;
	private boolean unitTest =false;
	private final GpsUtil gpsUtil;
	private final ApiClient apiClient;


	public TourGuideService(GpsUtil gpsUtil, ApiClient apiClient, RewardsService rewardsService, boolean unitTest) {
		this.gpsUtil = gpsUtil;
		this.apiClient = apiClient;
		this.rewardsService = rewardsService;
		this.unitTest =unitTest;
		
		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		tracker.runTracking();
	}





	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}
	
	public VisitedLocation getUserLocation(User user) {
		return (user != null && !CollectionUtils.isEmpty(user.getVisitedLocations())) ?
			user.getLastVisitedLocation() :
			trackUserLocation(user);
	}
	
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}
	
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}
	
	public void addUser(User user) {
			internalUserMap.putIfAbsent(user.getUserName(), user);

	}
	
	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(UserReward::getRewardPoints).sum();

		List<Provider> providers;

				if(unitTest){
					providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
							user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
				}else {
					providers = apiClient.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
							user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
				}

		user.setTripDeals(providers);
		return providers;
	}

	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = null ;
		if (unitTest){
			visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		}  else {
			visitedLocation = apiClient.getUserLocation(user.getUserId());

		}
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}

	public List<VisitedLocation> trackUsersLocation(List<User> users) {


		return users.stream().parallel().
				map(user -> {
					VisitedLocation visitedLocation;
					if (unitTest){
						visitedLocation = gpsUtil.getUserLocation(user.getUserId());
					}  else {
						visitedLocation = apiClient.getUserLocation(user.getUserId());

					}
					user.addToVisitedLocations(visitedLocation);
					rewardsService.calculateRewards(user);
					return visitedLocation;
				} ).collect(Collectors.toList());
	}

	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> attractions;
		if (unitTest){
			attractions = gpsUtil.getAttractions();
		}  else {
			attractions = apiClient.getAttractions();;

		}

		return  attractions.stream().
				sorted(Comparator.comparing(attraction -> rewardsService.getDistance(attraction, visitedLocation.location)))
				.limit(5)
				.collect(Collectors.toList());


	}

	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}
	
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}
	
	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

	public ApiClient getApiClient() {
		return apiClient;
	}

	public UserPreferenceDTO addUserPreferences(String userName, UserPreferenceDTO userPreferenceDTO) {

		User user = getUser(userName);
		UserPreferences userPreferences = new UserPreferences();
				BeanUtils.copyProperties(userPreferenceDTO,userPreferences);
		user.setUserPreferences(userPreferences);

		return userPreferenceDTO;
	}
}
