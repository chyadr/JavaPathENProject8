package tourGuide;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tripPricer.Provider;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ConstantsTest {
    public static final VisitedLocation visitedLocation = new VisitedLocation(new UUID(1l, 2l), new Location(2d, 5d), new Date());

    public static final Attraction attraction1 = new Attraction("attraction1", "city1", "state1", 1d, 1d);
    public static final Attraction attraction2 = new Attraction("attraction2", "city2", "state2", 2d, 2d);
    public static final Attraction attraction3 = new Attraction("attraction3", "city3", "state3", 3d, 3d);
    public static final Attraction attraction4 = new Attraction("attraction4", "city4", "state4", 4d, 4d);
    public static final Attraction attraction5 = new Attraction("attraction5", "city5", "state5", 5d, 5d);

    public static final List<Attraction> attractions = List.of(attraction1, attraction2, attraction3, attraction4, attraction5);

    public static final List<UserReward> userRewards = Collections.singletonList(new UserReward(visitedLocation, attraction1));
    public static final List<User> users = Collections.singletonList(new User(new UUID(1l, 2l), "userName", "0983838838", "mail"));
    public static final List<Provider> providers = Collections.singletonList(new Provider(new UUID(1l, 1l), "name", 1D));

}