package tourGuide.dto;

public class ClosestFiveTouristAttractionsDTO {
     private  String attractionName;
     private   double locationLongitude;
     private   double locationLatitude;
     private   double attractionLongitude;
     private   double attractionLatitude;
     private   double distance;
     private int rewardPoints;

     public String getAttractionName() {
          return attractionName;
     }

     public void setAttractionName(String attractionName) {
          this.attractionName = attractionName;
     }

     public double getLocationLongitude() {
          return locationLongitude;
     }

     public void setLocationLongitude(double locationLongitude) {
          this.locationLongitude = locationLongitude;
     }

     public double getLocationLatitude() {
          return locationLatitude;
     }

     public void setLocationLatitude(double locationLatitude) {
          this.locationLatitude = locationLatitude;
     }

     public double getAttractionLongitude() {
          return attractionLongitude;
     }

     public void setAttractionLongitude(double attractionLongitude) {
          this.attractionLongitude = attractionLongitude;
     }

     public double getAttractionLatitude() {
          return attractionLatitude;
     }

     public void setAttractionLatitude(double attractionLatitude) {
          this.attractionLatitude = attractionLatitude;
     }

     public double getDistance() {
          return distance;
     }

     public void setDistance(double distance) {
          this.distance = distance;
     }

     public int getRewardPoints() {
          return rewardPoints;
     }

     public void setRewardPoints(int rewardPoints) {
          this.rewardPoints = rewardPoints;
     }
}
