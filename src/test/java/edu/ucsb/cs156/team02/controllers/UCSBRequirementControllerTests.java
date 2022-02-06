package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.repositories.UserRepository;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
import edu.ucsb.cs156.team02.ControllerTestCase;
import edu.ucsb.cs156.team02.entities.UCSBRequirement;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.repositories.UCSBRequirementRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UCSBRequirementController.class)
@Import(TestConfig.class)
public class UCSBRequirementControllerTests extends ControllerTestCase {

    @MockBean
    UCSBRequirementRepository ucsbRequirementRepository;

    @MockBean
    UserRepository userRepository;


    // Authorization tests for /api/UCSBRequirements/all

    @Test
    public void api_UCSBRequirements_all__logged_out__returns_403() throws Exception {
        mockMvc.perform(get("/api/UCSBRequirements/all"))
                .andExpect(status().is(403));
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_UCSBRequirements_all__user_logged_in__returns_200() throws Exception {
        mockMvc.perform(get("/api/UCSBRequirements/all"))
                .andExpect(status().isOk());
    }

    // Authorization tests for /api/UCSBRequirements/post

    @Test
    public void api_UCSBRequirements_post__logged_out__returns_403() throws Exception {
        mockMvc.perform(post("/api/UCSBRequirements/post"))
                .andExpect(status().is(403));
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_UCSBRequirements_post__user_logged_in() throws Exception {
        // arrange

        //User u = currentUserService.getCurrentUser().getUser();

        UCSBRequirement expectedUCSBRequirement = UCSBRequirement.builder()
                .id(0L)
                .requirementCode("Test RequirementCode")
                .requirementTranslation("Test RequirementTranslation")
                .collegeCode("Test CollegeCode")
                .objCode("Test ObjCode")
                .courseCount(1)
                .units(4)
                .inactive(true)
                .build();

        when(ucsbRequirementRepository.save(eq(expectedUCSBRequirement))).thenReturn(expectedUCSBRequirement);

        // act
        MvcResult response = mockMvc.perform(
                post("/api/UCSBRequirements/post?requirementCode=Test RequirementCode&requirementTranslation=Test RequirementTranslation&collegeCode=Test CollegeCode&objCode=Test ObjCode&courseCount=1&units=4&inactive=true")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbRequirementRepository, times(1)).save(expectedUCSBRequirement);
        String expectedJson = mapper.writeValueAsString(expectedUCSBRequirement);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_UCSBRequirements__user_logged_in__returns_a_UCSBRequirement_that_exists() throws Exception {

        // arrange

        UCSBRequirement ucsbRe1 = UCSBRequirement.builder()
                .requirementCode("rc1")
                .requirementTranslation("rt1")
                .collegeCode("cc1")
                .objCode("oc1")
                .courseCount(1)
                .units(4)
                .inactive(true)
                .id(7L)
                .build();
        when(ucsbRequirementRepository.findById(eq(7L))).thenReturn(Optional.of(ucsbRe1));

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements?id=7"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(ucsbRequirementRepository, times(1)).findById(eq(7L));
        String expectedJson = mapper.writeValueAsString(ucsbRe1);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_UCSBRequirements__user_logged_in__search_for_UCSBRequirement_that_does_not_exist() throws Exception {

        // arrange

        when(ucsbRequirementRepository.findById(eq(7L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements?id=7"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(ucsbRequirementRepository, times(1)).findById(eq(7L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSB requirement with id 7 not found", responseString);
    }

}