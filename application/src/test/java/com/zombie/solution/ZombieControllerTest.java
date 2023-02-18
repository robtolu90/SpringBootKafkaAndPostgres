package com.zombie.solution;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zombie.solution.controller.ZombieController;
import com.zombie.solution.features.zombie_location.model.dao.ZombieLocation;
import com.zombie.solution.features.zombie_location.model.dto.ZombieLocationDTO;
import com.zombie.solution.features.zombie_location.service.ZombieLocationService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.zombie.solution.util.ZombieUtil.createPointFromCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ZombieController.class)
public class ZombieControllerTest {

    @MockBean
    ZombieLocationService zombieLocationService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getZombieReturnTheExpectedListOfLocations() throws Exception {
        Double latParam = 50.5;
        Double lonParam = 50.5;
        Integer limit = 10;
        List<UUID> zombieIdsReturned = Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        List<ZombieLocation> zombieLocations =  zombieIdsReturned.stream().map(this::aZombieLocationWithId).collect(Collectors.toList());
        Mockito.when(zombieLocationService.findNearWithinPoint(lonParam, latParam, limit)).thenReturn(zombieLocations);

        MvcResult mvcResult = mockMvc.perform(get("/zombies")
                .param("lat", latParam.toString())
                .param("lon", lonParam.toString())
                .param("limit", limit.toString())
                )
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<ZombieLocationDTO> zombieLocationDTOS = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {});

        assertThat(zombieIdsReturned, Matchers.containsInAnyOrder(zombieLocationDTOS.stream().map(ZombieLocationDTO::getZombieId).toArray()));
    }

    private ZombieLocation aZombieLocationWithId(UUID uuid) {
        return new ZombieLocation(
                uuid,
                LocalDateTime.MAX,
                createPointFromCoordinate(Double.MAX_VALUE, Double.MAX_VALUE)
        );
    }

}

