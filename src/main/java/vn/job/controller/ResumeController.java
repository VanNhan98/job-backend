package vn.job.controller;

import com.turkraft.springfilter.boot.Filter;
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
@RequestMapping("resumes")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping("/add")
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

}
