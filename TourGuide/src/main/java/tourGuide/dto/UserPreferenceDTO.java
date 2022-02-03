package tourGuide.dto;

import javax.validation.constraints.Min;

public class UserPreferenceDTO {

    /**
     *  maximum distance where the attrations must be
     */
    @Min(value =0,  message ="must be greater than or equal to 0")
    private int attractionProximity ;

    /**
     *  lowest acceptable price
     */
    @Min(value =0,  message ="must be greater than or equal to 0")
    private int lowerPricePoint;

    /**
     *  maximum acceptable price
     */
    @Min(value =1,  message ="must be greater than or equal to 0")
    private int highPricePoint;

    /**
     *  length of stay
     */
    @Min(value =1,  message ="must be greater than or equal to 1")
    private int tripDuration;

    @Min(value =1,  message ="must be greater than or equal to 1")
    private int ticketQuantity;

    @Min(value =1,  message ="must be greater than or equal to 1")
    private int numberOfAdults;

    @Min(value =0,  message ="must be greater than or equal to 0")
    private int numberOfChildren;

    public int getAttractionProximity() {
        return attractionProximity;
    }

    public void setAttractionProximity(int attractionProximity) {
        this.attractionProximity = attractionProximity;
    }

    public int getLowerPricePoint() {
        return lowerPricePoint;
    }

    public void setLowerPricePoint(int lowerPricePoint) {
        this.lowerPricePoint = lowerPricePoint;
    }

    public int getHighPricePoint() {
        return highPricePoint;
    }

    public void setHighPricePoint(int highPricePoint) {
        this.highPricePoint = highPricePoint;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
