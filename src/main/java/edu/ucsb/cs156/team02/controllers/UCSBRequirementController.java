package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.entities.UCSBRequirement;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.models.CurrentUser;
import edu.ucsb.cs156.team02.repositories.UCSBRequirementRepository;
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

@Api(description = "UCSB Requirements")
@RequestMapping("/api/UCSBRequirements")
@RestController
@Slf4j
public class UCSBRequirementController extends ApiController {

    /**
     * This inner class helps us factor out some code for checking
     * whether todos exist, and whether they belong to the current user,
     * along with the error messages pertaining to those situations. It
     * bundles together the state needed for those checks.
     */
    public class UCSBRequirementOrError {
        Long id;
        UCSBRequirement ucsbRe;
        ResponseEntity<String> error;

        public UCSBRequirementOrError(Long id) {
            this.id = id;
        }
    }

    @Autowired
    UCSBRequirementRepository ucsbRequirementRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all UCSB requirements")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBRequirement> allUsersUCSBRequirements() {
        loggingService.logMethod();
        Iterable<UCSBRequirement> requirements = ucsbRequirementRepository.findAll();
        return requirements;
    }

    @ApiOperation(value = "Create a new UCSBRequirement")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/post")
    public UCSBRequirement postUCSBRequirement(
            @ApiParam("id") @RequestParam long id,
            @ApiParam("requirementCode") @RequestParam String requirementCode,
            @ApiParam("requirementTranslation") @RequestParam String requirementTranslation,
            @ApiParam("collegeCode") @RequestParam String collegeCode,
            @ApiParam("objCode") @RequestParam String objCode,
            @ApiParam("courseCount") @RequestParam int courseCount,
            @ApiParam("units") @RequestParam int units,
            @ApiParam("inactive") @RequestParam boolean inactive) {
        loggingService.logMethod();

        UCSBRequirement ucsbReq = new UCSBRequirement();
        ucsbReq.setId(id);
        ucsbReq.setRequirementCode(requirementCode);
        ucsbReq.setRequirementTranslation(requirementTranslation);
        ucsbReq.setCollegeCode(collegeCode);
        ucsbReq.setObjCode(objCode);
        ucsbReq.setCourseCount(courseCount);
        ucsbReq.setUnits(units);
        ucsbReq.setInactive(inactive);
        UCSBRequirement savedUCSBRequirement = ucsbRequirementRepository.save(ucsbReq);
        return savedUCSBRequirement;
    }
}
