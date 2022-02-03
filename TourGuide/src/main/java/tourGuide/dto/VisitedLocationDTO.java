package tourGuide.dto;

import java.util.Date;
import java.util.UUID;

public class VisitedLocationDTO  {

    private   UUID userId;
    private   LocationDTO location;
    private   Date timeVisited;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public Date getTimeVisited() {
        return timeVisited;
    }

    public void setTimeVisited(Date timeVisited) {
        this.timeVisited = timeVisited;
    }
}
