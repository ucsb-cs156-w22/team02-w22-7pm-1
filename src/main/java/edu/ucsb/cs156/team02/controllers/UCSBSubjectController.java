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

@Api(description = "UCSBSubjects") // EX: changed this
@RequestMapping("/api/UCSBSubjects/") // EX:Changed this
@RestController
@Slf4j
public class UCSBSubjectController extends ApiController {
    public class UCSBSubjectError {
        Long id;
        UCSBSubject ucsbSubject;
        ResponseEntity<String> error;

        public UCSBSubjectError(Long id) {
            this.id = id;
        }
    }

    @Autowired
    UCSBSubjectRepository UCSBSubjectRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all subjects")
    @GetMapping("/all")
    public Iterable<UCSBSubject> allUCSBSubject() {
        loggingService.logMethod();
        Iterable<UCSBSubject> UCSBSubjects = UCSBSubjectRepository.findAll();
        return UCSBSubjects;
    }

    // EX: FEELING CONFIDENT THIS METHOD WORKS
    @ApiOperation(value = "Create a new subject JSON object")
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
        UCSBSubject saveducsbSubject = UCSBSubjectRepository.save(ucsbSubject);
        return saveducsbSubject;
    }

    public UCSBSubjectError doesUCSBSubjectExist(UCSBSubjectError ucsbError) {

        Optional<UCSBSubject> optionalUCSBSubject = UCSBSubjectRepository.findBySubjectCode(ucsbError.id);

        if (optionalUCSBSubject.isEmpty()) {
            ucsbError.error = ResponseEntity
                    .badRequest()
                    .body(String.format("id %d not found", ucsbError.id));
        } else {
            ucsbError.todo = optionalUCSBSubject.get();
        }
        return ucsbError;
    }

    // make it work for not just admins
    @ApiOperation(value = "Get a single todo (no matter who it belongs to, admin only)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> getUCSBSubjectID_admin(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        loggingService.logMethod();

        UCSBSubjectError ucsbError = new UCSBSubjectError(id);

        ucsbError = doesUCSBSubjectExist(ucsbError);
        if (ucsbError.error != null) {
            return ucsbError.error;
        }

        String body = mapper.writeValueAsString(ucsbError.todo);
        return ResponseEntity.ok().body(body);
    }

}