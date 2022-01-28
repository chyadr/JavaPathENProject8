package com.rewardcentral.service;

import com.rewardcentral.dto.AttractionRewardPointsInputDTO;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.UUID;

@Service
public class RewardCentralService  implements IRewardCentralService{

    private final RewardCentral rewardCentral;

    public RewardCentralService() {
        this.rewardCentral = new RewardCentral();
    }

    @Override
    public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
        return rewardCentral.getAttractionRewardPoints(attractionId,userId);
    }
}
