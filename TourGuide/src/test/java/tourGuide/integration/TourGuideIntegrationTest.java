package tourGuide.integration;

import com.jsoniter.output.JsonStream;
import gpsUtil.GpsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import rewardCentral.RewardCentral;
import tourGuide.ConstantsTest;
import tourGuide.api.ApiClient;
import tourGuide.controller.TourGuideController;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TourGuideController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TourGuideIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private  ApiClient apiClient;


    @TestConfiguration
     static class TourGuideIntegrationTestContextConfiguration {




        @Bean
        public TourGuideService tourGuideService() {
            return new TourGuideService(new GpsUtil(), Mockito.mock(ApiClient.class),
                    new RewardsService(new GpsUtil(),Mockito.mock(ApiClient.class),new RewardCentral(),false),false);
        }

        @Bean
        public RewardsService rewardsService() {
            return new RewardsService(new GpsUtil(),Mockito.mock(ApiClient.class),new RewardCentral(),false);
        }
    }

    @Autowired
    private TourGuideService tourGuideService;
    @Autowired
    private RewardsService rewardsService;



    @Test
    public void givenNothing_whenIndex_thenReturnGreetingsFromTourGuide() throws Exception {

        mvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk()).
                andExpect(content().string("Greetings from TourGuide!"));
    }

    @Test
    public void givenUserName_whenGetLocation_thenReturnJsonLocation() throws Exception {

        when(tourGuideService.getApiClient().getUserLocation(any())).thenReturn(ConstantsTest.visitedLocation);

        mvc.perform(get("/getLocation").param("userName",tourGuideService.getAllUsers().stream().findFirst()
                                .map(user -> {
                                    user.clearVisitedLocations();
                                    return user;
                                }).get().getUserName())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk()).
                andExpect(content().string(JsonStream.serialize(ConstantsTest.visitedLocation.location)));
    }

    @Test
    public void givenUserName_whenGetNearbyAttractions_thenReturnJsonClosestFiveTouristAttractionsDTOS() throws Exception {

        when(tourGuideService.getApiClient().getUserLocation(any())).thenReturn(ConstantsTest.visitedLocation);
        when(tourGuideService.getApiClient().getAttractions()).thenReturn(ConstantsTest.attractions);
        when(tourGuideService.getApiClient().getAttractionRewardPoints(any(),any())).thenReturn(1);

        mvc.perform(get("/getNearbyAttractions").param("userName",tourGuideService.getAllUsers().stream().findFirst()
                                .map(user -> {
                                    user.clearVisitedLocations();
                                    return user;
                                }).get().getUserName())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUserName_whenGetRewards_thenReturnJsonUserRewards() throws Exception {

        mvc.perform(get("/getRewards").param("userName",tourGuideService.getAllUsers().stream().findAny().get().getUserName())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }


    @Test
    public void givenUserName_whenGetTripDeals_thenReturnJsonProviders() throws Exception {

        when(tourGuideService.getApiClient().getPrice(anyString(),any(),anyInt(),anyInt(),anyInt(),anyInt())).thenReturn(ConstantsTest.providers);

        mvc.perform(get("/getTripDeals").param("userName",tourGuideService.getAllUsers().stream().findFirst()
                                .map(user -> {
                                    user.clearVisitedLocations();
                                    return user;
                                }).get().getUserName())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk()).
                andExpect(content().string(JsonStream.serialize(ConstantsTest.providers)));
    }

    @Test
    public void givenUserName_whenGetAllVisitedLocation_thenReturnJsonVisitedLocation() throws Exception {

        when(apiClient.getUserLocation(any())).thenReturn(ConstantsTest.visitedLocation);

        mvc.perform(get("/getAllUsersLocation")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }


}

