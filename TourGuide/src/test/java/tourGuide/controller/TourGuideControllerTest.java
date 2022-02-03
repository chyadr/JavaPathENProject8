package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.ConstantsTest;
import tourGuide.api.ApiClient;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TourGuideController.class)
public class TourGuideControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    TourGuideService tourGuideService;
    @MockBean
    RewardsService rewardsService;
    @MockBean
    ApiClient apiClient;

    @Test
    public void givenNothing_whenIndex_thenReturnGreetingsFromTourGuide() throws Exception {

        mvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk()).
                andExpect(content().string("Greetings from TourGuide!"));
    }

    @Test
    public void givenUserName_whenGetLocation_thenReturnJsonLocation() throws Exception {

        when(tourGuideService.getUserLocation(any())).thenReturn(ConstantsTest.visitedLocation);

        mvc.perform(get("/getLocation").param("userName","test")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk()).
                andExpect(content().string(JsonStream.serialize(ConstantsTest.visitedLocation.location)));
    }

    @Test
    public void givenUserName_whenGetNearbyAttractions_thenReturnJsonClosestFiveTouristAttractionsDTOS() throws Exception {

        when(tourGuideService.getUserLocation(any())).thenReturn(ConstantsTest.visitedLocation);
        when(tourGuideService.getNearByAttractions(any())).thenReturn(ConstantsTest.attractions);
        when(rewardsService.getDistance(any(),any())).thenReturn(1d);
        when(apiClient.getAttractionRewardPoints(any(),any())).thenReturn(1);

        mvc.perform(get("/getNearbyAttractions").param("userName","test")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUserName_whenGetRewards_thenReturnJsonUserRewards() throws Exception {

        when(tourGuideService.getUserRewards(any())).thenReturn(ConstantsTest.userRewards);

        mvc.perform(get("/getRewards").param("userName","test")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk()).
                andExpect(content().string(JsonStream.serialize(ConstantsTest.userRewards)));
    }

    @Test
    public void givenUserName_whenGetAllCurrentLocations_thenReturnJsonUsers() throws Exception {

        when(tourGuideService.getAllUsers()).thenReturn(ConstantsTest.users);

        mvc.perform(get("/getAllCurrentLocations")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUserName_whenGetTripDeals_thenReturnJsonProviders() throws Exception {

        when(tourGuideService.getTripDeals(any())).thenReturn(ConstantsTest.providers);

        mvc.perform(get("/getTripDeals").param("userName","test")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk()).
                andExpect(content().string(JsonStream.serialize(ConstantsTest.providers)));
    }

    @Test
    public void givenUserName_whenGetAllVisitedLocation_thenReturnJsonVisitedLocation() throws Exception {

        when(tourGuideService.getAllUsers()).thenReturn(ConstantsTest.users);
        when(apiClient.getUserLocation(any())).thenReturn(ConstantsTest.visitedLocation);

        mvc.perform(get("/getAllUsersLocation")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }


}

