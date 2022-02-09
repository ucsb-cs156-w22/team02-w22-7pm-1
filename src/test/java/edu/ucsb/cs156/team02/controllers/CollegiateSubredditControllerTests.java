package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.repositories.UserRepository;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
import edu.ucsb.cs156.team02.ControllerTestCase;
import edu.ucsb.cs156.team02.entities.CollegiateSubreddit;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.repositories.CollegiateSubredditRepository;

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

@WebMvcTest(controllers = CollegiateSubredditController.class)
@Import(TestConfig.class)
public class CollegiateSubredditControllerTests extends ControllerTestCase {

    @MockBean
    CollegiateSubredditRepository colSubRepository;

    @MockBean
    UserRepository userRepository;

    // Authorization tests for /api/todos/admin/all
/*
    @Test
    public void api_todos_admin_all__logged_out__returns_403() throws Exception {
        mockMvc.perform(get("/api/todos/admin/all"))
                .andExpect(status().is(403));
    }
*/
/*
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_CollegiateSubreddit_admin_all__user_logged_in__returns_403() throws Exception {
        mockMvc.perform(get("/api/collegiateSubreddit/admin/all"))
                .andExpect(status().is(403));
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_CollegiateSubreddit_admin__user_logged_in__returns_403() throws Exception {
        mockMvc.perform(get("/api/collegiateSubreddit/admin/?id=7"))
                .andExpect(status().is(403));
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_CollegiateSubreddit_admin_all__admin_logged_in__returns_200() throws Exception {
        mockMvc.perform(get("/api/collegiateSubreddit/admin/all"))
                .andExpect(status().isOk());
    }

    // Authorization tests for /api/collegiateSubreddit/all

    @Test
    public void api_CollegiateSubreddit_all__logged_out__returns_403() throws Exception {
        mockMvc.perform(get("/api/collegiateSubreddit/all"))
                .andExpect(status().is(403));
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_CollegiateSubreddit_all__user_logged_in__returns_200() throws Exception {
        mockMvc.perform(get("/api/collegiateSubreddit/all"))
                .andExpect(status().isOk());
    }

    // Authorization tests for /api/todos/post

    @Test
    public void api_CollegiateSubreddit_post__logged_out__returns_403() throws Exception {
        mockMvc.perform(post("/api/collegiateSubreddit/post"))
                .andExpect(status().is(403));
    }
*/
    // Tests with mocks for database actions

/*
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_CollegiateSubreddit__user_logged_in__returns_a_CollegiateSubreddit_that_exists() throws Exception {

        // arrange

        CollegiateSubreddit colSub1 = CollegiateSubreddit.builder().name("CollegiateSubreddit name1").location("CollegiateSubreddit location1")
                                                                        .subreddit("CollegiateSubreddit subreddit1").id(7L).build();
        when(colSubRepository.findById(eq(7L))).thenReturn(Optional.of(colSub1));

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddit/?id=7"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(colSubRepository, times(1)).findById(eq(7L));
        String expectedJson = mapper.writeValueAsString(colSub1);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }
*/

/*
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_CollegiateSubreddit__user_logged_in__search_for_todo_that_does_not_exist() throws Exception {

        // arrange

        // User u = currentUserService.getCurrentUser().getUser();

        when(colSubRepository.findById(eq(7L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddit/?id=7"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(colSubRepository, times(1)).findById(eq(7L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 7 not found", responseString);
    }
*/

    /*
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_todos__user_logged_in__search_for_todo_that_belongs_to_another_user() throws Exception {

        // arrange

        User u = currentUserService.getCurrentUser().getUser();
        User otherUser = User.builder().id(999L).build();
        CollegiateSubreddit otherUsersCollegiateSubreddit = CollegiateSubreddit.builder().title("CollegiateSubreddit 1").details("CollegiateSubreddit 1").done(false).user(otherUser).id(13L)
                .build();

        when(colSubRepository.findById(eq(13L))).thenReturn(Optional.of(otherUsersCollegiateSubreddit));

        // act
        MvcResult response = mockMvc.perform(get("/api/todos?id=13"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(colSubRepository, times(1)).findById(eq(13L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 13 not found", responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_todos__admin_logged_in__search_for_todo_that_belongs_to_another_user() throws Exception {

        // arrange

        User u = currentUserService.getCurrentUser().getUser();
        User otherUser = User.builder().id(999L).build();
        CollegiateSubreddit otherUsersCollegiateSubreddit = CollegiateSubreddit.builder().title("CollegiateSubreddit 1").details("CollegiateSubreddit 1").done(false).user(otherUser).id(27L)
                .build();

        when(colSubRepository.findById(eq(27L))).thenReturn(Optional.of(otherUsersCollegiateSubreddit));

        // act
        MvcResult response = mockMvc.perform(get("/api/todos/admin?id=27"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(colSubRepository, times(1)).findById(eq(27L));
        String expectedJson = mapper.writeValueAsString(otherUsersCollegiateSubreddit);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }
    */


/*
    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_todos__admin_logged_in__search_for_todo_that_does_not_exist() throws Exception {

        // arrange

        when(colSubRepository.findById(eq(29L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/todos/admin?id=29"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(colSubRepository, times(1)).findById(eq(29L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 29 not found", responseString);
    }
*/
//task 2 - POST
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_CollegiateSubreddit_post__user_logged_in() throws Exception {
        // arrange

        // User u = currentUserService.getCurrentUser().getUser();

        CollegiateSubreddit expectedCollegiateSubreddit = CollegiateSubreddit.builder()
                .name("Test name")
                .location("Test location")
                .subreddit("Test subreddit")
                .id(0L)
                .build();

        when(colSubRepository.save(eq(expectedCollegiateSubreddit))).thenReturn(expectedCollegiateSubreddit);

        // act
        MvcResult response = mockMvc.perform(
                post("/api/collegiateSubreddit/post?name=Test name&location=Test location&subreddit=Test subreddit")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(colSubRepository, times(1)).save(expectedCollegiateSubreddit);
        String expectedJson = mapper.writeValueAsString(expectedCollegiateSubreddit);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

//task 2 - GET
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_CollegiateSubreddit_all__user_logged_in__returns_all_CollegiateSubreddit() throws Exception {
        // arrange

        CollegiateSubreddit CollegiateSubreddit1 = CollegiateSubreddit.builder()
                .name("Test name1")
                .location("Test location1")
                .subreddit("Test subreddit1")
                .id(0L)
                .build();

        CollegiateSubreddit CollegiateSubreddit2 = CollegiateSubreddit.builder()
                .name("Test name2")
                .location("Test location2")
                .subreddit("Test subreddit2")
                .id(1L)
                .build();
        
        CollegiateSubreddit CollegiateSubreddit3 = CollegiateSubreddit.builder()
                .name("Test name3")
                .location("Test location3")
                .subreddit("Test subreddit3")
                .id(2L)
                .build();

        
        ArrayList<CollegiateSubreddit> expectedCollegiateSubreddit = new ArrayList<>();
        expectedCollegiateSubreddit.addAll(Arrays.asList(CollegiateSubreddit1, CollegiateSubreddit2, CollegiateSubreddit3));
        
        when(colSubRepository.findAll()).thenReturn(expectedCollegiateSubreddit);

        // act
        MvcResult response = mockMvc.perform(
                get("/api/collegiateSubreddit/all"))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(colSubRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedCollegiateSubreddit);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

//task 2 end

/*
    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_todos_admin_all__admin_logged_in__returns_all_CollegiateSubreddit() throws Exception {

        // arrange

        User u1 = User.builder().id(1L).build();
        User u2 = User.builder().id(2L).build();
        User u = currentUserService.getCurrentUser().getUser();

        CollegiateSubreddit colSub1 = CollegiateSubreddit.builder().name("CollegiateSubreddit name1").location("CollegiateSubreddit loc1").subreddit("CollegiateSubreddit subr1").user(u1).id(1L).build();
        CollegiateSubreddit colSub2 = CollegiateSubreddit.builder().name("CollegiateSubreddit name1").location("CollegiateSubreddit loc1").subreddit("CollegiateSubreddit subr1").user(u2).id(2L).build();
        CollegiateSubreddit colSub3 = CollegiateSubreddit.builder().name("CollegiateSubreddit name1").location("CollegiateSubreddit loc1").subreddit("CollegiateSubreddit subr1").user(u).id(3L).build();

        ArrayList<CollegiateSubreddit> expectedCollegiateSubreddits = new ArrayList<>();
        expectedCollegiateSubreddits.addAll(Arrays.asList(colSub1, colSub2, colSub3));

        when(colSubRepository.findAll()).thenReturn(expectedCollegiateSubreddits);

        // act
        MvcResult response = mockMvc.perform(get("/api/todos/admin/all"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(colSubRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedCollegiateSubreddits);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }
*/

/*
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_todos_all__user_logged_in__returns_only_CollegiateSubreddit_for_user() throws Exception {

        // arrange

        User thisUser = currentUserService.getCurrentUser().getUser();

        CollegiateSubreddit todo1 = CollegiateSubreddit.builder().title("CollegiateSubreddit 1").details("CollegiateSubreddit 1").done(false).user(thisUser).id(1L).build();
        CollegiateSubreddit todo2 = CollegiateSubreddit.builder().title("CollegiateSubreddit 2").details("CollegiateSubreddit 2").done(false).user(thisUser).id(2L).build();

        ArrayList<CollegiateSubreddit> expectedCollegiateSubreddits = new ArrayList<>();
        expectedCollegiateSubreddits.addAll(Arrays.asList(todo1, todo2));
        when(colSubRepository.findAllByUserId(thisUser.getId())).thenReturn(expectedCollegiateSubreddits);

        // act
        MvcResult response = mockMvc.perform(get("/api/todos/all"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(colSubRepository, times(1)).findAllByUserId(eq(thisUser.getId()));
        String expectedJson = mapper.writeValueAsString(expectedCollegiateSubreddits);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }
*/

/*
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_todos__user_logged_in__delete_todo() throws Exception {
        // arrange

        User u = currentUserService.getCurrentUser().getUser();
        CollegiateSubreddit todo1 = CollegiateSubreddit.builder().title("CollegiateSubreddit 1").details("CollegiateSubreddit 1").done(false).user(u).id(15L).build();
        when(colSubRepository.findById(eq(15L))).thenReturn(Optional.of(todo1));

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/todos?id=15")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(colSubRepository, times(1)).findById(15L);
        verify(colSubRepository, times(1)).deleteById(15L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 15 deleted", responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_todos__user_logged_in__delete_CollegiateSubreddit_that_does_not_exist() throws Exception {
        // arrange

        User otherUser = User.builder().id(98L).build();
        CollegiateSubreddit todo1 = CollegiateSubreddit.builder().title("CollegiateSubreddit 1").details("CollegiateSubreddit 1").done(false).user(otherUser).id(15L).build();
        when(colSubRepository.findById(eq(15L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/todos?id=15")
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(colSubRepository, times(1)).findById(15L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 15 not found", responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_todos__user_logged_in__cannot_delete_todo_belonging_to_another_user() throws Exception {
        // arrange

        User otherUser = User.builder().id(98L).build();
        CollegiateSubreddit todo1 = CollegiateSubreddit.builder().title("CollegiateSubreddit 1").details("CollegiateSubreddit 1").done(false).user(otherUser).id(31L).build();
        when(colSubRepository.findById(eq(31L))).thenReturn(Optional.of(todo1));

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/todos?id=31")
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(colSubRepository, times(1)).findById(31L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 31 not found", responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_todos__admin_logged_in__delete_todo() throws Exception {
        // arrange

        User otherUser = User.builder().id(98L).build();
        CollegiateSubreddit todo1 = CollegiateSubreddit.builder().title("CollegiateSubreddit 1").details("CollegiateSubreddit 1").done(false).user(otherUser).id(16L).build();
        when(colSubRepository.findById(eq(16L))).thenReturn(Optional.of(todo1));

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/todos/admin?id=16")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(colSubRepository, times(1)).findById(16L);
        verify(colSubRepository, times(1)).deleteById(16L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 16 deleted", responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_todos__admin_logged_in__cannot_delete_todo_that_does_not_exist() throws Exception {
        // arrange

        when(colSubRepository.findById(eq(17L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/todos/admin?id=17")
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(colSubRepository, times(1)).findById(17L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 17 not found", responseString);
    }
*/

/*
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_CollegiateSubreddit__user_logged_in__put_CollegiateSubreddit() throws Exception {
        // arrange

        User u = currentUserService.getCurrentUser().getUser();
        User otherUser = User.builder().id(999).build();
        CollegiateSubreddit todo1 = CollegiateSubreddit.builder().title("CollegiateSubreddit 1").details("CollegiateSubreddit 1").done(false).user(u).id(67L).build();
        // We deliberately set the user information to another user
        // This shoudl get ignored and overwritten with currrent user when todo is saved

        CollegiateSubreddit updatedCollegiateSubreddit = CollegiateSubreddit.builder().title("New Title").details("New Details").done(true).user(otherUser).id(67L).build();
        CollegiateSubreddit correctCollegiateSubreddit = CollegiateSubreddit.builder().title("New Title").details("New Details").done(true).user(u).id(67L).build();

        String requestBody = mapper.writeValueAsString(updatedCollegiateSubreddit);
        String expectedReturn = mapper.writeValueAsString(correctCollegiateSubreddit);

        when(colSubRepository.findById(eq(67L))).thenReturn(Optional.of(todo1));

        // act
        MvcResult response = mockMvc.perform(
                put("/api/todos?id=67")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(colSubRepository, times(1)).findById(67L);
        verify(colSubRepository, times(1)).save(correctCollegiateSubreddit); // should be saved with correct user
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedReturn, responseString);
    }
*/

/*
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_CollegiateSubreddit__user_logged_in__cannot_put_CollegiateSubreddit_that_does_not_exist() throws Exception {
        // arrange

        CollegiateSubreddit updatedCollegiateSubreddit = CollegiateSubreddit.builder().name("New name").location("New location").subreddit("New subreddit").id(67L).build();

        String requestBody = mapper.writeValueAsString(updatedCollegiateSubreddit);

        when(colSubRepository.findById(eq(67L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                put("/api/todos?id=67")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(colSubRepository, times(1)).findById(67L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 67 not found", responseString);
    }
*/

/*
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_todos__user_logged_in__cannot_put_todo_for_another_user() throws Exception {
        // arrange

        User otherUser = User.builder().id(98L).build();
        CollegiateSubreddit todo1 = CollegiateSubreddit.builder().title("CollegiateSubreddit 1").details("CollegiateSubreddit 1").done(false).user(otherUser).id(31L).build();
        CollegiateSubreddit updatedCollegiateSubreddit = CollegiateSubreddit.builder().title("New Title").details("New Details").done(true).id(31L).build();

        when(colSubRepository.findById(eq(31L))).thenReturn(Optional.of(todo1));

        String requestBody = mapper.writeValueAsString(updatedCollegiateSubreddit);

        when(colSubRepository.findById(eq(67L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                put("/api/todos?id=31")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(colSubRepository, times(1)).findById(31L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 31 not found", responseString);
    }
*/

/*
    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_todos__admin_logged_in__put_todo() throws Exception {
        // arrange

        User otherUser = User.builder().id(255L).build();
        CollegiateSubreddit colSub1 = CollegiateSubreddit.builder().name("CollegiateSubreddit name1").location("CollegiateSubreddit location1")
                                                                        .subreddit("CollegiateSubreddit subreddit1").user(otherUser).id(77L).build();
        User yetAnotherUser = User.builder().id(512L).build();
        // We deliberately put the wrong user on the updated todo
        // We expect the controller to ignore this and keep the user the same
        CollegiateSubreddit updatedCollegiateSubreddit = CollegiateSubreddit.builder().title("New Title").details("New Details").done(true).user(yetAnotherUser).id(77L)
                .build();
        CollegiateSubreddit correctCollegiateSubreddit = CollegiateSubreddit.builder().title("New Title").details("New Details").done(true).user(otherUser).id(77L)
                .build();

        String requestBody = mapper.writeValueAsString(updatedCollegiateSubreddit);
        String expectedJson = mapper.writeValueAsString(correctCollegiateSubreddit);

        when(colSubRepository.findById(eq(77L))).thenReturn(Optional.of(todo1));

        // act
        MvcResult response = mockMvc.perform(
                put("/api/collegiateSubreddit/admin?id=77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(colSubRepository, times(1)).findById(77L);
        verify(colSubRepository, times(1)).save(correctCollegiateSubreddit);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }
*/

/*
    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_CollegiateSubreddit__admin_logged_in__cannot_put_CollegiateSubreddit_that_does_not_exist() throws Exception {
        // arrange

        // User otherUser = User.builder().id(345L).build();
        CollegiateSubreddit updatedCollegiateSubreddit = CollegiateSubreddit.builder().name("New Name").location("New Location").subreddit("New Subreddit").id(77L)
                .build();

        String requestBody = mapper.writeValueAsString(updatedCollegiateSubreddit);

        when(colSubRepository.findById(eq(77L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                put("/api/collegiateSubreddit/admin?id=77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(colSubRepository, times(1)).findById(77L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("todo with id 77 not found", responseString);
    }
    */

}
