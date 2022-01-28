package tourGuide.dto;

public class RecentLocationDTO {

    private String userId;
    private double latitude;
    private double longitude;
    private String formattedTimeVisited;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFormattedTimeVisited() {
        return formattedTimeVisited;
    }

    public void setFormattedTimeVisited(String formattedTimeVisited) {
        this.formattedTimeVisited = formattedTimeVisited;
    }
}
