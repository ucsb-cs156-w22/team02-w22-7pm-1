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

@Api(description = "UCSB Requirement Controller")
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
            @ApiParam("requirementCode") @RequestParam String requirementCode,
            @ApiParam("requirementTranslation") @RequestParam String requirementTranslation,
            @ApiParam("collegeCode") @RequestParam String collegeCode,
            @ApiParam("objCode") @RequestParam String objCode,
            @ApiParam("courseCount") @RequestParam int courseCount,
            @ApiParam("units") @RequestParam int units,
            @ApiParam("inactive") @RequestParam boolean inactive) {
        loggingService.logMethod();

        UCSBRequirement ucsbReq = new UCSBRequirement();
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

    @ApiOperation(value = "Get a single UCSBRequirement")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<String> getUCSBRequirementById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        loggingService.logMethod();
        UCSBRequirementOrError roe = new UCSBRequirementOrError(id);

        roe = doesUCSBRequirementExist(roe);
        if (roe.error != null) {
            //System.out.printf("id 123 not found");
            return roe.error;
        }
        String body = mapper.writeValueAsString(roe.ucsbRe);
        return ResponseEntity.ok().body(body);
    }


    public UCSBRequirementOrError doesUCSBRequirementExist(UCSBRequirementOrError roe) {

        Optional<UCSBRequirement> optionalUCSBRequirement = ucsbRequirementRepository.findById(roe.id);

        if (optionalUCSBRequirement.isEmpty()) {
            roe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("UCSB Requirement with id %d not found", roe.id));
        } else {
            roe.ucsbRe = optionalUCSBRequirement.get();
        }
        return roe;
    }
    //new adding 

    @ApiOperation(value = "Delete a UCSBRequirment owned by this user")
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteUCSBRequirement(
            @ApiParam("id") @RequestParam Long id) {
        loggingService.logMethod();

        UCSBRequirementOrError roe = new UCSBRequirementOrError(id);

        roe = doesUCSBRequirementExist(roe);
        if (roe.error != null) {
           return roe.error;
        }

    //this part may need to change 

   
        ucsbRequirementRepository.deleteById(id);
        return ResponseEntity.ok().body(String.format("UCSB Requirment with id %d deleted", id));

}

//***************************************   PUT  *********************************** */

@ApiOperation(value = "Update a single UCSB Requirment ")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("")
    public ResponseEntity<String> putUCSBRequirmentById(
            @ApiParam("id") @RequestParam Long id ,
            @RequestBody @Valid UCSBRequirement incomingUCSBReuirement) throws JsonProcessingException{
        loggingService.logMethod();

                

        UCSBRequirementOrError roe = new UCSBRequirementOrError(id);

        roe = doesUCSBRequirementExist(roe);
        if (roe.error != null) {
            return roe.error;
        }
 

        //we need to changes this if we save the date more then one time code is in ADMIN
        incomingUCSBReuirement.setId(id);
        ucsbRequirementRepository.save(incomingUCSBReuirement);
        
        

        String body = mapper.writeValueAsString(incomingUCSBReuirement);
        return ResponseEntity.ok().body(body);
    }




}


