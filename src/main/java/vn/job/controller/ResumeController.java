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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.job.dto.request.RequestUpdateResume;
import vn.job.dto.response.ResPagination;
import vn.job.dto.response.error.ResponseData;
import vn.job.dto.response.error.ResponseError;
import vn.job.dto.response.job.ResCreateJob;
import vn.job.dto.response.resume.ResCreateResume;
import vn.job.dto.response.resume.ResDetailResume;
import vn.job.dto.response.resume.ResUpdateResume;
import vn.job.exception.EntityAlreadyExistsException;
import vn.job.exception.IdInvalidException;
import vn.job.model.Job;
import vn.job.model.Resume;
import vn.job.service.ResumeService;

@RestController
@RequestMapping("/resumes")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Resume Controller")
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping("/add")
    @Operation(summary = "Create new resume", description = "API for insert resume into databases")
    public ResponseData<ResCreateResume> addResume(@Valid @RequestBody Resume resume) {
        log.info("Request create resume={}", resume.getEmail());
        try {

            ResCreateResume currentResume = this.resumeService.handleCreateResume(resume);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Add resume successfully", currentResume);
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Add resume failed");
        }
    }

    @PutMapping("/update")
    @Operation(summary = "Update resume", description = "API for update resume into databases")
    public ResponseData<ResUpdateResume> updateResume(@RequestBody RequestUpdateResume resume) {
        log.info("Request update resume={}", resume.getId());
        try {

            ResUpdateResume currentResume = this.resumeService.handleUpdateResume(resume);
            return new ResponseData<>(HttpStatus.OK.value(), "Update resume successfully", currentResume);
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Update resume failed");
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete resume", description = "API for delete resume into databases")

    public ResponseData<Void> deleteResume(@PathVariable("id") long id) {
        log.info("Request delete deleteResume={}", id);
        try {
            this.resumeService.handleDeleteResume(id);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete Resume successfully");
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete Resume failed");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get resume", description = "API for get resume into databases")

    public ResponseData<ResDetailResume> getResume(@PathVariable("id") long id) {
        log.info("Request get resume={}", id);
        try {
            ResDetailResume currentResume = this.resumeService.handleGetResume(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Detail Resume successfully", currentResume);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get Detail Resume failed");
        }
    }


    @GetMapping("/list")
    @Operation(summary = "Get list resumes", description = "API for get list resumes into databases")

    public ResponseData<ResPagination> getAllResume(@Filter Specification<Resume> spec, Pageable pageable) {
        log.info("Request list Resume");
        try {
            ResPagination currentResume = this.resumeService.handleGetAllResume(spec, pageable);
            return new ResponseData<>(HttpStatus.OK.value(), "Get list Resume successfully", currentResume);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list Resume failed");
        }
    }

    @GetMapping("/by-user")
    @Operation(summary = "Get resume by user", description = "API for get resume by user into databases")
    public ResponseData<ResPagination> getResumeByUser(Pageable pageable) {
        log.info("Request get resume by user");
        try {
            ResPagination currentResume = this.resumeService.handleGetResumeByUser(pageable);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Detail Resume successfully", currentResume);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get Detail Resume failed");
        }
    }


}
