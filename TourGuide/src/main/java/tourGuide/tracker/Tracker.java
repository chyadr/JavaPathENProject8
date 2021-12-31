package tourGuide.tracker;

import java.util.List;
import java.util.concurrent.*;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourGuide.service.TourGuideService;
import tourGuide.user.User;

public class Tracker {
	private Logger logger = LoggerFactory.getLogger(Tracker.class);
	private final TourGuideService tourGuideService;
	private static final int trackerThreads = 50;
	private final ExecutorService executorService = new ThreadPoolExecutor(trackerThreads, 2000,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>());


	public Tracker(TourGuideService tourGuideService) {
		this.tourGuideService = tourGuideService;
	}
	
	/**
	 * Assures to shut down the Tracker thread
	 */
	public void runTracking() {
		StopWatch stopWatch = new StopWatch();
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "50");
		List<User> users = tourGuideService.getAllUsers();
		logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
		stopWatch.start();
		if(users.size()<trackerThreads){
			Runnable worker = new TrackerRunnable(users,tourGuideService);
			executorService.execute(worker);
		}else{
			for (List<User> subUsers : Lists.partition(users,users.size()/trackerThreads)) {
				logger.debug("partition size : " + subUsers.size());
				Runnable worker = new TrackerRunnable(subUsers,tourGuideService);
				executorService.execute(worker);
			}
		}

		stopWatch.stop();

		executorService.shutdown();

	}


}

class TrackerRunnable implements Runnable {
	private final List<User> users;
	private final TourGuideService tourGuideService;

	public TrackerRunnable(List<User> users, TourGuideService tourGuideService) {
		this.users = users;
		this.tourGuideService = tourGuideService;
	}

	@Override
	public void run() {
		tourGuideService.trackUsersLocation(users);

	}
}