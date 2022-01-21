package com.rewardcentral.service;

import com.rewardcentral.dto.AttractionRewardPointsInputDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface IRewardCentralService {
     int getAttractionRewardPoints(UUID attractionId, UUID userId);

    }
