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

@Api(description = "UCSB Subject Controller")
@RequestMapping("/api/UCSBSubjects")
@RestController
@Slf4j
public class UCSBSubjectController extends ApiController {

    public class UCSBSubjectOrError {
        Long id;
        UCSBSubject ucsbSubject;
        ResponseEntity<String> error;

        public UCSBSubjectOrError(Long id) {
            this.id = id;
        }
    }

    @Autowired
    UCSBSubjectRepository ucsbSubjectRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all UCSB Subjects")
    // @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBSubject> allUCSBSubjects() {
        loggingService.logMethod();
        Iterable<UCSBSubject> UCSBSubjects = ucsbSubjectRepository.findAll();
        return UCSBSubjects;
    }

    @ApiOperation(value = "Create a new UCSB Subject JSON Object")
    // @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/post")
    public UCSBSubject postUCSBSubject(
            @ApiParam("subjectCode") @RequestParam String subjectCode,
            @ApiParam("subjectTranslation") @RequestParam String subjectTranslation,
            @ApiParam("deptCode") @RequestParam String deptCode,
            @ApiParam("collegeCode") @RequestParam String collegeCode,
            @ApiParam("relatedDeptCode") @RequestParam String relatedDeptCode,
            @ApiParam("inactive") @RequestParam Boolean inactive) {
        loggingService.logMethod();

        log.info(
                "UCSB subject /post called: subjectCode={}, subjectTranslation={}, "
                        + "deptCode={}, collegeCode={}, relatedDeptCode={}, inactive={}",
                subjectCode, subjectTranslation, deptCode, collegeCode, relatedDeptCode, inactive); // EX: borrowed/took
                                                                                                    // influence this
                                                                                                    // from another
                                                                                                    // group to see

        UCSBSubject ucsbSubject = new UCSBSubject();
        ucsbSubject.setSubjectCode(subjectCode);
        ucsbSubject.setSubjectTranslation(subjectTranslation);
        ucsbSubject.setDeptCode(deptCode);
        ucsbSubject.setCollegeCode(collegeCode);
        ucsbSubject.setRelatedDeptCode(relatedDeptCode);
        ucsbSubject.setInactive(inactive);
        UCSBSubject saveducsbSubject = ucsbSubjectRepository.save(ucsbSubject);
        return saveducsbSubject;
    }

    @ApiOperation(value = "Get a single UCSBSubject")
    // @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<String> getUCSBSubjectById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        loggingService.logMethod();
        UCSBSubjectOrError roe = new UCSBSubjectOrError(id);

        roe = doesUCSBSubjectExist(roe);
        if (roe.error != null) {

            return roe.error;
        }
        String body = mapper.writeValueAsString(roe.ucsbSubject);
        return ResponseEntity.ok().body(body);
    }

    public UCSBSubjectOrError doesUCSBSubjectExist(UCSBSubjectOrError roe) {

        Optional<UCSBSubject> optionalUCSBSubject = ucsbSubjectRepository.findById(roe.id);

        if (optionalUCSBSubject.isEmpty()) {
            roe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("subject with id %d not found", roe.id));
        } else {
            roe.ucsbSubject = optionalUCSBSubject.get();
        }
        return roe;
    }

    //FOR TASK FOUR THIS FUNCTION (EX)
    @ApiOperation(value = "Update a single subject by ID)")
    //@PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("")
    public ResponseEntity<String> putUCSBSubjectID(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid UCSBSubject incomingUCSBSubject) throws JsonProcessingException {
        loggingService.logMethod();

        //CurrentUser currentUser = getCurrentUser();
        //User user = currentUser.getUser();    (EX: I DONT THINK WE HAVE A USER ???)

        UCSBSubjectOrError ucsbSubError = new UCSBSubjectOrError(id);

        ucsbSubError = doesUCSBSubjectExist(ucsbSubError);
        if (ucsbSubError.error != null) {
            return ucsbSubError.error;
        }

        //incomingTodo.setUser(user);
        // incomingUCSBSubject.setSubjectCode(subjectCode);
        // incomingUCSBSubject.setSubjectTranslation(subjectTranslation);
        // incomingUCSBSubject.setDeptCode(deptCode);
        // incomingUCSBSubject.setCollegeCode(collegeCode);
        // incomingUCSBSubject.setRelatedDeptCode(relatedDeptCode);
        // incomingUCSBSubject.setInactive(inactive);
        ucsbSubjectRepository.save(incomingUCSBSubject);

        String body = mapper.writeValueAsString(incomingUCSBSubject);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Delete a subject by ID")
    @DeleteMapping("")
    public ResponseEntity<String> deleteUCSBSubject(
            @ApiParam("id") @RequestParam Long id) {
        loggingService.logMethod();

        UCSBSubjectOrError roe = new UCSBSubjectOrError(id);

        roe = doesUCSBSubjectExist(roe);
        if (roe.error != null) {
            return roe.error;
        }

        ucsbSubjectRepository.deleteById(id);
        return ResponseEntity.ok().body(String.format("subject with id %d deleted", id));

    }

}