package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.entities.CollegiateSubreddit;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.models.CurrentUser;
import edu.ucsb.cs156.team02.repositories.CollegiateSubredditRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Api(description = "CollegiateSubreddits")
@RequestMapping("/api/collegiateSubreddit")
@RestController
@Slf4j
public class CollegiateSubredditController extends ApiController {

    /**
     * This inner class helps us factor out some code for checking
     * whether todos exist, and whether they belong to the current user,
     * along with the error messages pertaining to those situations. It
     * bundles together the state needed for those checks.
     */
    public class CollegiateSubredditOrError {
        Long id;
        CollegiateSubreddit colSub;
        ResponseEntity<String> error;

        public CollegiateSubredditOrError(Long id) {
            this.id = id;
        }
    }

    @Autowired
    CollegiateSubredditRepository colSubRepository;

    @Autowired
    ObjectMapper mapper;


//task 2 - GET list all
    @ApiOperation(value = "List this user's collegiate subreddits")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<CollegiateSubreddit> thisUsersCollegiateSubreddits() {
        loggingService.logMethod();
        Iterable<CollegiateSubreddit> colSubs = colSubRepository.findAll();
        return colSubs;
    }

/*wrap unknown; looks like other GETs

    @ApiOperation(value = "List all CollegiateSubreddits")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/all")
    public Iterable<CollegiateSubreddit> allUsersCollegiateSubreddits() {
        loggingService.logMethod();
        Iterable<CollegiateSubreddit> colSubs = colSubRepository.findAll();
        return colSubs;
    }

    @ApiOperation(value = "Get a single collegiate subreddit (if it belongs to current user)")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<String> getCollegiateSubredditById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        CollegiateSubredditOrError toe = new CollegiateSubredditOrError(id);

        toe = doesCollegiateSubredditExist(toe);
        if (toe.error != null) {
            return toe.error;
        }
        toe = doesCollegiateSubredditBelongToCurrentUser(toe);
        if (toe.error != null) {
            return toe.error;
        }
        String body = mapper.writeValueAsString(toe.colSub);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Get a single collegiate subreddit (no matter who it belongs to, admin only)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> getCollegiateSubredditById_admin(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        CollegiateSubredditOrError toe = new CollegiateSubredditOrError(id);

        toe = doesCollegiateSubredditExist(toe);
        if (toe.error != null) {
            return toe.error;
        }

        String body = mapper.writeValueAsString(toe.colSub);
        return ResponseEntity.ok().body(body);
    }
*/ 
//wrap end

//task 2 - POST a new
    @ApiOperation(value = "Create a new CollegiateSubreddit")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/post")
    public CollegiateSubreddit postCollegiateSubreddit(
            @ApiParam("name") @RequestParam String name,
            @ApiParam("location") @RequestParam String location,
            @ApiParam("subreddit") @RequestParam String subreddit) {

        loggingService.logMethod();

        CollegiateSubreddit colSub = new CollegiateSubreddit();
        colSub.setName(name);
        colSub.setLocation(location);
        colSub.setSubreddit(subreddit);

        CollegiateSubreddit savedCollegiateSubreddit = colSubRepository.save(colSub);
        return savedCollegiateSubreddit;
    }

/*
    @ApiOperation(value = "Delete a Todo owned by this user")
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteTodo(
            @ApiParam("id") @RequestParam Long id) {
        CollegiateSubredditOrError toe = new CollegiateSubredditOrError(id);

        toe = doesCollegiateSubredditExist(toe);
        if (toe.error != null) {
            return toe.error;
        }

        toe = doesCollegiateSubredditBelongToCurrentUser(toe);
        if (toe.error != null) {
            return toe.error;
        }
        todoRepository.deleteById(id);
        return ResponseEntity.ok().body(String.format("todo with id %d deleted", id));

    }
*/

/*
    @ApiOperation(value = "Delete another user's todo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin")
    public ResponseEntity<String> deleteTodo_Admin(
            @ApiParam("id") @RequestParam Long id) {
        CollegiateSubredditOrError toe = new CollegiateSubredditOrError(id);

        toe = doesTodoExist(toe);
        if (toe.error != null) {
            return toe.error;
        }

        todoRepository.deleteById(id);

        return ResponseEntity.ok().body(String.format("todo with id %d deleted", id));

    }
*/

/*
    @ApiOperation(value = "Update a single todo (if it belongs to current user)")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("")
    public ResponseEntity<String> putTodoById(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid Todo incomingTodo) throws JsonProcessingException {
        CurrentUser currentUser = getCurrentUser();
        User user = currentUser.getUser();

        CollegiateSubredditOrError toe = new CollegiateSubredditOrError(id);

        toe = doesTodoExist(toe);
        if (toe.error != null) {
            return toe.error;
        }
        toe = doesTodoBelongToCurrentUser(toe);
        if (toe.error != null) {
            return toe.error;
        }

        incomingTodo.setUser(user);
        todoRepository.save(incomingTodo);

        String body = mapper.writeValueAsString(incomingTodo);
        return ResponseEntity.ok().body(body);
    }
*/
/*
    @ApiOperation(value = "Update a single collegiate subreddit (regardless of ownership, admin only, can't change ownership)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin")
    public ResponseEntity<String> putCollegiateSubredditById_admin(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid CollegiateSubreddit incomingCollegiateSubreddit) throws JsonProcessingException {
        CollegiateSubredditOrError toe = new CollegiateSubredditOrError(id);

        toe = doesCollegiateSubredditExist(toe);
        if (toe.error != null) {
            return toe.error;
        }

        // Even the admin can't change the user; they can change other details
        // but not that.

        User previousUser = toe.colSub.getUser();
        incomingCollegiateSubreddit.setUser(previousUser);
        colSubRepository.save(incomingCollegiateSubreddit);

        String body = mapper.writeValueAsString(incomingCollegiateSubreddit);
        return ResponseEntity.ok().body(body);
    }
*/

    /**
     * Pre-conditions: toe.id is value to look up, toe.todo and toe.error are null
     *
     * Post-condition: if todo with id toe.id exists, toe.todo now refers to it, and
     * error is null.
     * Otherwise, todo with id toe.id does not exist, and error is a suitable return
     * value to
     * report this error condition.
     */
    public CollegiateSubredditOrError doesCollegiateSubredditExist(CollegiateSubredditOrError toe) {

        Optional<CollegiateSubreddit> optionalCollegiateSubreddit = colSubRepository.findById(toe.id);

        if (optionalCollegiateSubreddit.isEmpty()) {
            toe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("collegiate subreddit with id %d not found", toe.id));
        } else {
            toe.colSub = optionalCollegiateSubreddit.get();
        }
        return toe;
    }

/*
    
     * Pre-conditions: toe.todo is non-null and refers to the todo with id toe.id,
     * and toe.error is null
     *
     * Post-condition: if todo belongs to current user, then error is still null.
     * Otherwise error is a suitable
     * return value.
    
    public CollegiateSubredditOrError doesCollegiateSubredditBelongToCurrentUser(CollegiateSubredditOrError toe) {
        CurrentUser currentUser = getCurrentUser();
        log.info("currentUser={}", currentUser);

        Long currentUserId = currentUser.getUser().getId();
        Long colSubUserId = toe.colSub.getUser().getId();
        log.info("currentUserId={} colSubUserId={}", currentUserId, colSubUserId);

        if (colSubUserId != currentUserId) {
            toe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("Collegiate subreddit with id %d not found", toe.id));
        }
        return toe;
    }
*/
}