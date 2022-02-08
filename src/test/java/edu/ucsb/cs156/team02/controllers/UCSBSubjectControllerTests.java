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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
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

    // Authorization tests for /api/UCSBSubjects/all

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

        // User u1 = User.builder().id(1L).build();
        // User u2 = User.builder().id(2L).build();
        // User u = currentUserService.getCurrentUser().getUser();

        UCSBSubject ucsbSubject1 = UCSBSubject.builder().id(0L).subjectCode("ucsbSubject1").subjectTranslation("Todo 1")
                .deptCode("false")
                .collegeCode("u1").relatedDeptCode("1L").inactive(true)
                .build();
        UCSBSubject ucsbSubject2 = UCSBSubject.builder().id(1L).subjectCode("ucsbSubject2").subjectTranslation("Todo 2")
                .deptCode("false")
                .collegeCode("u2").relatedDeptCode("2L").inactive(true)
                .build();
        UCSBSubject ucsbSubject3 = UCSBSubject.builder().id(2L).subjectCode("ucsbSubject3").subjectTranslation("Todo 3")
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
    // EX:Testing for fourth part to get max test coverage (incomplete)
    // @WithMockUser(roles = { "USER" })
    // @Test
    // public void api_todos__user_logged_in__put_todo() throws Exception {
    // // arrange

    // User u = currentUserService.getCurrentUser().get
    // User otherUser = User.builder().id(999).build();
    // UCSBSubject ucsbSubject = UCSBSubject.builder().title("Todo 1").details("Todo
    // 1").done(false).user(u).id(67L).build();
    // // We deliberately set the user information to another user
    //

    // UCSBSubject correctSubject = UCSBSubject.builder().title("New
    // Title").details("New Details").done(true).user(u)
    // .id(67L).build();

    // // String requestBody = mapper.writeValueAsString(updatedSubject);
    // // String expectedReturn = mapper.writeValueAsString(correctSubje

    // when(ucsbSubjectRepository.findById(eq(67L))).thenReturn(Optional.of(ucsbSubject));

    // // // act
    // // MvcResult response = mockMvc.perform(
    // // put("/api/UCSBSubjects?id=67")
    // // .contentType(MediaType.APPLICATION_JSON)
    // // .characterEncoding("utf-8")
    // // .content(requestBody)
    // // .with(csrf()))
    // // .andExpect(status().isOk()).andReturn();

    // // // assert
    // //

    // verify(ucsbSubjectRepository, times(1)).save(correctSubject); // should be
    // saved with correct user
    // // String responseString = response.getResponse().getContentAsString();
    // // assertEquals(expectedReturn, responseString);
    // // }

    @Test
    public void api_subjects__delete_subjects() throws Exception {

    // arrange

    UCSBSubject req1 = UCSBSubject.builder()
    .subjectCode("subjectCode")
    .subjectTranslation("subjectTranslation")
    .collegeCode("collegeCode")
    .deptCode("deptCode")
    .collegeCode("collegeCode")
    .relatedDeptCode("relatedDeptCode")
    .inactive(false)
    .id(123L).build();

    when(ucsbSubjectRepository.findById(eq(123L))).thenReturn(Optional.of(req1));

    // act
    MvcResult response = mockMvc.perform(
        delete("/api/UCSBSubjects?id=123")
                .with(csrf()))
        .andExpect(status().isOk()).andReturn();

    // assert

    verify(ucsbSubjectRepository, times(1)).findById(123L);
    verify(ucsbSubjectRepository, times(1)).deleteById(123L);
    String responseString = response.getResponse().getContentAsString();
    assertEquals("record 123 deleted", responseString);
    }

    @Test
    public void api_subjects__delete_subjects_that_does_not_exist() throws Exception {

    // arrange

    when(ucsbSubjectRepository.findById(eq(123L))).thenReturn(Optional.empty());

    MvcResult response = mockMvc.perform(
    delete("/api/UCSBSubjects?id=123")
    .with(csrf()))
    .andExpect(status().isBadRequest()).andReturn();

    // assert

    verify(ucsbSubjectRepository, times(1)).findById(123L);
    String responseString = response.getResponse().getContentAsString();
    assertEquals("record 123 not found", responseString);
    }

    @Test
    public void api_subjects__put_subjects() throws Exception {

    // arrange

    UCSBSubject initialSubject = UCSBSubject.builder()
    .subjectCode("subjectCode")
    .subjectTranslation("subjectTranslation")
    .collegeCode("collegeCode")
    .deptCode("deptCode")
    .collegeCode("collegeCode")
    .relatedDeptCode("relatedDeptCode")
    .inactive(false)
    .id(123L).build();

    UCSBSubject updatedSubject = UCSBSubject.builder()
    .subjectCode("new subjectCode")
    .subjectTranslation("new subjectTranslation")
    .collegeCode("new collegeCode")
    .deptCode("new deptCode")
    .collegeCode("new collegeCode")
    .relatedDeptCode("new relatedDeptCode")
    .inactive(true)
    .id(123L).build();

    String requestBody = mapper.writeValueAsString(updatedSubject);
    String expectedReturn = mapper.writeValueAsString(updatedSubject);

    when(ucsbSubjectRepository.findById(eq(123L))).thenReturn(Optional.of(initialSubject));

    // act

    MvcResult response = mockMvc.perform(
    put("/api/UCSBSubjects?id=123")
    .contentType(MediaType.APPLICATION_JSON)
    .characterEncoding("utf-8")
    .content(requestBody)
    .with(csrf()))
    .andExpect(status().isOk()).andReturn();

    // assert

    verify(ucsbSubjectRepository, times(1)).findById(123L);
    verify(ucsbSubjectRepository, times(1)).save(updatedSubject);
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedReturn, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_subjects__user_logged_in__returns_a_subject_that_exists() throws Exception {

        // arrange

        UCSBSubject subject1 = UCSBSubject.builder()
                .subjectCode("subjectCode")
                .subjectTranslation("subjectTranslation")
                .deptCode("deptCode")
                .collegeCode("collegeCode")
                .relatedDeptCode("relatedDeptCode")
                .inactive(false)
                .id(123L).build();

        when(ucsbSubjectRepository.findById(eq(123L))).thenReturn(Optional.of(subject1));

        // act

        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects?id=123"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(ucsbSubjectRepository, times(1)).findById(eq(123L));
        String expectedJson = mapper.writeValueAsString(subject1);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }


    @WithMockUser(roles = { "USER" })
    @Test
    public void api_UCSBSubjects__user_logged_in__search_for_UCSBSubject_that_does_not_exist() throws Exception {

        // arrange

        when(ucsbSubjectRepository.findById(eq(123L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(

                get("/api/UCSBSubjects?id=123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        verify(ucsbSubjectRepository, times(1)).findById(eq(123L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("id 123 not found", responseString);
    }
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_subjects__cannot_put_subjects_that_does_not_exist() throws Exception {

        // arrange

        UCSBSubject updatedSubject = UCSBSubject.builder()
                .subjectCode("subjectCode")
                .subjectTranslation("subjectTranslation")
                .deptCode("deptCode")
                .collegeCode("collegeCode")
                .relatedDeptCode("relatedDeptCode")
                .inactive(false)
                .id(123L).build();

        String requestBody = mapper.writeValueAsString(updatedSubject);

        when(ucsbSubjectRepository.findById(eq(123L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                put("/api/UCSBSubjects?id=123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findById(123L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("id 123 not found", responseString);
    }

}
//
