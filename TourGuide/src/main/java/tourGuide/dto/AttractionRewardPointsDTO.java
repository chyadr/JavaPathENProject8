package tourGuide.dto;

import java.util.UUID;

public class AttractionRewardPointsDTO {

    private UUID attractionId;
    private UUID userId;

    public UUID getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(UUID attractionId) {
        this.attractionId = attractionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
