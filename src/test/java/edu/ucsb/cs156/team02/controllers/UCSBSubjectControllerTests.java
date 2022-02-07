package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.repositories.UserRepository;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
import edu.ucsb.cs156.team02.ControllerTestCase;
import edu.ucsb.cs156.team02.entities.UCSBSubject;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.repositories.UCSBSubjectRepository;

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

@WebMvcTest(controllers = UCSBSubjectController.class)
@Import(TestConfig.class)
public class UCSBSubjectControllerTests extends ControllerTestCase {

    @MockBean
    UCSBSubjectRepository ucsbSubjectRepository;

    @MockBean
    UserRepository userRepository;

    // Authorization tests for /api/todos/admin/all

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsbsubject_post__user_logged_in() throws Exception {
        // arrange

        UCSBSubject expectedUCSBSubject = UCSBSubject.builder()
                .subjectCode("Test subjectCode")
                .subjectTranslation("Test subjectTranslation")
                .deptCode("Test deptCode")
                .collegeCode("Test collegeCode")
                .relatedDeptCode("Test relatedDeptCode")
                .inactive(true)
                .build();

        when(ucsbSubjectRepository.save(eq(expectedUCSBSubject))).thenReturn(expectedUCSBSubject);

        // act
        MvcResult response = mockMvc.perform(
                post("/api/UCSBSubjects/post?subjectCode=Test subjectCode&subjectTranslation=Test subjectTranslation&deptCode=Test deptCode&collegeCode=Test collegeCode&relatedDeptCode=Test relatedDeptCode&inactive=true")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).save(expectedUCSBSubject);
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsbsubjects_all__user_logged_in__returns_all_ucsbsubjects() throws Exception {

        // arrange

        User u1 = User.builder().id(1L).build();
        User u2 = User.builder().id(2L).build();
        User u = currentUserService.getCurrentUser().getUser();

        UCSBSubject ucsbSubject1 = UCSBSubject.builder().subjectCode("ucsbSubject1").subjectTranslation("Todo 1")
                .deptCode("false")
                .collegeCode("u1").relatedDeptCode("1L").inactive(true)
                .build();
        UCSBSubject ucsbSubject2 = UCSBSubject.builder().subjectCode("ucsbSubject2").subjectTranslation("Todo 2")
                .deptCode("false")
                .collegeCode("u2").relatedDeptCode("2L").inactive(true)
                .build();
        UCSBSubject ucsbSubject3 = UCSBSubject.builder().subjectCode("ucsbSubject3").subjectTranslation("Todo 3")
                .deptCode("false")
                .collegeCode("u").relatedDeptCode("3L").inactive(true)
                .build();

        ArrayList<UCSBSubject> expectedUCSBSubjects = new ArrayList<>();
        expectedUCSBSubjects.addAll(Arrays.asList(ucsbSubject1, ucsbSubject2, ucsbSubject3));

        when(ucsbSubjectRepository.findAll()).thenReturn(expectedUCSBSubjects);

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(ucsbSubjectRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubjects);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

}