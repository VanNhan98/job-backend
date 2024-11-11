package vn.job.controller;

import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.job.dto.response.ResPagination;
import vn.job.dto.response.error.ResponseData;
import vn.job.dto.response.error.ResponseError;
import vn.job.model.Company;
import vn.job.model.Skill;
import vn.job.service.SkillService;

@RestController
@RequestMapping("skills")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Skill Controller")
public class SkillController {
    private final SkillService skillService;

    @PostMapping("/add")
    @Operation(summary = "Create new skill", description = "API for insert skill into databases")
    public ResponseData<Skill> addSkill(@Valid @RequestBody Skill skill) {
        log.info("Request create skill={}", skill.getName());
        try {
            Skill currentSkill =  this.skillService.handleCreateSkill(skill);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Skill add successfully", currentSkill);
        }
        catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Add skill failed");
        }
    }

    @PutMapping("/update")
    @Operation(summary = "Update skill", description = "API for update skill into databases")
    public ResponseData<Skill> updateSkill(@Valid @RequestBody Skill skill) {
        log.info("Request update skill={}", skill.getName());
        try {
            Skill currentSkill =  this.skillService.handleUpdateSkill(skill);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(),  " Update skill successfully", currentSkill);
        }
        catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Update skill failed");
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Get list skills", description = "API for get list skills into databases")
    public ResponseData<ResPagination> getAllCompany(@Filter Specification<Skill> spec, Pageable pageable) {
        log.info("Request list skill");
        try {
            ResPagination currentSkill = this.skillService.handleGetAllSkills(spec, pageable);
            return new ResponseData<>(HttpStatus.OK.value(), "Get list skill successfully", currentSkill);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list skill failed");
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete skill", description = "API for delete skill into databases")
    public ResponseData<Void> deleteSkill(@PathVariable("id") long id) {
        log.info("Request delete skill={}", id);
        try {
            this.skillService.handleDeleteSkill(id);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete skill successfully");
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete skill failed");
        }
    }

}
