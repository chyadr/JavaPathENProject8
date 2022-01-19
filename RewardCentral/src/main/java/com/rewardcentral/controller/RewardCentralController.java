package com.rewardcentral.controller;


import com.rewardcentral.dto.AttractionRewardPointsInputDTO;
import org.springframework.web.bind.annotation.*;
import rewardCentral.RewardCentral;

@RestController
@RequestMapping("/api")
public class RewardCentralController {

    private final RewardCentral rewardCentral;

    public RewardCentralController() {
        this.rewardCentral = new RewardCentral();
    }

    @GetMapping("/reward-point")
    public int getAttractionRewardPoints(@RequestBody AttractionRewardPointsInputDTO attractionRewardPointsInputDTO) {
        return rewardCentral.getAttractionRewardPoints(attractionRewardPointsInputDTO.getAttractionId(),attractionRewardPointsInputDTO.getUserId());
    }


}
