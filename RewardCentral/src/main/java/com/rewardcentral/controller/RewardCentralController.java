package com.rewardcentral.controller;


import com.rewardcentral.dto.AttractionRewardPointsInputDTO;
import com.rewardcentral.service.RewardCentralService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RewardCentralController {
    private final RewardCentralService rewardCentralService;

    public RewardCentralController(RewardCentralService rewardCentralService) {
        this.rewardCentralService = rewardCentralService;
    }

    @GetMapping("/reward-point")
    public int getAttractionRewardPoints(@RequestBody AttractionRewardPointsInputDTO attractionRewardPointsInputDTO) {
        return rewardCentralService.getAttractionRewardPoints(attractionRewardPointsInputDTO.getAttractionId(),attractionRewardPointsInputDTO.getUserId());
    }


}
