package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.entities.Todo;
import edu.ucsb.cs156.team02.entities.UCSBSubject;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.models.CurrentUser;
import edu.ucsb.cs156.team02.repositories.UCSBSubjectRepository;
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

@Api(description = "UCSBSubjects")        //EX: changed this
@RequestMapping("/api/UCSBSubjects/all") //EX:Changed this 
@RestController
@Slf4j
public class UCSBSubjectController extends ApiController {

    /**
     * This inner class helps us factor out some code for checking
     * whether todos exist, and whether they belong to the current user,
     * along with the error messages pertaining to those situations. It
     * bundles together the state needed for those checks.
     */

    //EX: This feels like the default constructor of the class so I'm going to treat it like one. 
    public class UCSBSubjectOrError {
        Long id;
        String subjectCode; 
        String subjectTranslation; 
        String deptCode;
        String collegeCode;
        String relatedDeptCode;
        boolean inactive;
        UCSBSubject ucsbSubject;
        ResponseEntity<UCSBSubject> error;

        public UCSBSubjectOrError(Long id, String subjectCode, String subjectTranslation, String deptCode, String collegeCode, 
                String relatedDeptCode, boolean inactive) {
            this.id = id;
            this.subjectCode = subjectCode;
            this.subjectTranslation = subjectTranslation;
            this.deptCode = deptCode;
            this.collegeCode = collegeCode;
            this.relatedDeptCode = relatedDeptCode;
            this.inactive = inactive;
        }
    }

    @Autowired
    UCSBSubjectRepository UCSBSubjectRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all subjects")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/all")
    public Iterable<UCSBSubject> allUsersUCSBSubject() {
        loggingService.logMethod();
        Iterable<UCSBSubject> UCSBSubjects = UCSBSubjectRepository.findAll();
        return UCSBSubjects;
    }

    @ApiOperation(value = "List this user's subjects")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBSubject> thisUsersUCSBSubjects() {
        loggingService.logMethod();
        CurrentUser currentUser = getCurrentUser();
        Iterable<UCSBSubject> UCSBSubjects = UCSBSubjectRepository.findAllByUserId(currentUser.getUser().getId()); //EX: ERROR HERE
        return UCSBSubjects;
    }
    //EX: This entire method is so messy, I don't even think any of this is right lol
    @ApiOperation(value = "Get a single UCSBSubject (if it belongs to current user)")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<UCSBSubject> getUCSBSubjectById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        loggingService.logMethod();
        UCSBSubjectOrError ucsbSubject = new UCSBSubjectOrError(id, null, null, null, null, null, false);

        ucsbSubject = doesUCSBSubjectExist(ucsbSubject);
        if (ucsbSubject.error != null) {
            return ucsbSubject.error;
        }
        ucsbSubject = doesUCSBSubjectBelongToCurrentUser(ucsbSubject);
        if (ucsbSubject.error != null) {
            return ucsbSubject.error;
        }
        String body = mapper.writeValueAsString(ucsbSubject.todo);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Get a single todo (no matter who it belongs to, admin only)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> getTodoById_admin(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        loggingService.logMethod();

        TodoOrError toe = new TodoOrError(id);

        toe = doesTodoExist(toe);
        if (toe.error != null) {
            return toe.error;
        }

        String body = mapper.writeValueAsString(toe.todo);
        return ResponseEntity.ok().body(body);
    }
    //EX: FEELING CONFIDENT THIS METHOD WORKS
    @ApiOperation(value = "Create a new subject")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/post")
    public UCSBSubject postUCSBSubject(
            @ApiParam("subjectCode") @RequestParam String subjectCode,
            @ApiParam("subjectTranslation") @RequestParam String subjectTranslation,
            @ApiParam("deptCode") @RequestParam String deptCode,
            @ApiParam("collegeCode") @RequestParam String collegeCode,
            @ApiParam("relatedDeptCode") @RequestParam String relatedDeptCode,
            @ApiParam("inactive") @RequestParam Boolean inactive){
        loggingService.logMethod();
        CurrentUser currentUser = getCurrentUser();
        log.info("currentUser={}", currentUser);

        Todo todo = new Todo();
        todo.setUser(currentUser.getUser());
        todo.setTitle(title);
        todo.setDetails(details);
        todo.setDone(done);
        Todo savedTodo = todoRepository.save(todo);
        return savedTodo;
    }

    @ApiOperation(value = "Delete a Todo owned by this user")
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteTodo(
            @ApiParam("id") @RequestParam Long id) {
        loggingService.logMethod();

        TodoOrError toe = new TodoOrError(id);

        toe = doesTodoExist(toe);
        if (toe.error != null) {
            return toe.error;
        }

        toe = doesTodoBelongToCurrentUser(toe);
        if (toe.error != null) {
            return toe.error;
        }
        todoRepository.deleteById(id);
        return ResponseEntity.ok().body(String.format("todo with id %d deleted", id));

    }

    @ApiOperation(value = "Delete another user's todo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin")
    public ResponseEntity<String> deleteTodo_Admin(
            @ApiParam("id") @RequestParam Long id) {
        loggingService.logMethod();

        TodoOrError toe = new TodoOrError(id);

        toe = doesTodoExist(toe);
        if (toe.error != null) {
            return toe.error;
        }

        todoRepository.deleteById(id);

        return ResponseEntity.ok().body(String.format("todo with id %d deleted", id));

    }

    @ApiOperation(value = "Update a single todo (if it belongs to current user)")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("")
    public ResponseEntity<String> putTodoById(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid Todo incomingTodo) throws JsonProcessingException {
        loggingService.logMethod();

        CurrentUser currentUser = getCurrentUser();
        User user = currentUser.getUser();

        TodoOrError toe = new TodoOrError(id);

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

    @ApiOperation(value = "Update a single todo (regardless of ownership, admin only, can't change ownership)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin")
    public ResponseEntity<String> putTodoById_admin(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid Todo incomingTodo) throws JsonProcessingException {
        loggingService.logMethod();

        TodoOrError toe = new TodoOrError(id);

        toe = doesTodoExist(toe);
        if (toe.error != null) {
            return toe.error;
        }

        // Even the admin can't change the user; they can change other details
        // but not that.

        User previousUser = toe.todo.getUser();
        incomingTodo.setUser(previousUser);
        todoRepository.save(incomingTodo);

        String body = mapper.writeValueAsString(incomingTodo);
        return ResponseEntity.ok().body(body);
    }

    /**
     * Pre-conditions: toe.id is value to look up, toe.todo and toe.error are null
     * 
     * Post-condition: if todo with id toe.id exists, toe.todo now refers to it, and
     * error is null.
     * Otherwise, todo with id toe.id does not exist, and error is a suitable return
     * value to
     * report this error condition.
     */
    public TodoOrError doesTodoExist(TodoOrError toe) {

        Optional<Todo> optionalTodo = todoRepository.findById(toe.id);

        if (optionalTodo.isEmpty()) {
            toe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("todo with id %d not found", toe.id));
        } else {
            toe.todo = optionalTodo.get();
        }
        return toe;
    }

    /**
     * Pre-conditions: toe.todo is non-null and refers to the todo with id toe.id,
     * and toe.error is null
     * 
     * Post-condition: if todo belongs to current user, then error is still null.
     * Otherwise error is a suitable
     * return value.
     */
    public TodoOrError doesTodoBelongToCurrentUser(TodoOrError toe) {
        CurrentUser currentUser = getCurrentUser();
        log.info("currentUser={}", currentUser);

        Long currentUserId = currentUser.getUser().getId();
        Long todoUserId = toe.todo.getUser().getId();
        log.info("currentUserId={} todoUserId={}", currentUserId, todoUserId);

        if (todoUserId != currentUserId) {
            toe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("todo with id %d not found", toe.id));
        }
        return toe;
    }

}