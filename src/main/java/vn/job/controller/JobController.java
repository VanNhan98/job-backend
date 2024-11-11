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
import vn.job.dto.response.job.ResCreateJob;
import vn.job.dto.response.job.ResUpdateJob;
import vn.job.model.Company;
import vn.job.model.Job;
import vn.job.model.Skill;
import vn.job.service.JobService;

@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Job Controller")
public class JobController {
    private final JobService jobService;

    @PostMapping("/add")
    @Operation(summary = "Create new job", description = "API for insert job into databases")
    public ResponseData<ResCreateJob> addJob(@Valid @RequestBody Job job) {
        log.info("Request create job={}", job.getName());
        try {
            ResCreateJob currentJob = this.jobService.handleCreateJob(job);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Add Job successfully", currentJob);
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Add skill failed");
        }
    }

    @PutMapping("/update")
    @Operation(summary = "Update job", description = "API for update job in databases")
    public ResponseData<ResUpdateJob> updateJob(@Valid @RequestBody Job job) {
        log.info("Request update job={}", job.getName());
        try {
            ResUpdateJob currentJob = this.jobService.handleUpdateJob(job);
            return new ResponseData<>(HttpStatus.OK.value(), "Update Job successfully", currentJob);
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Update skill failed");
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete job", description = "API for delete job in databases")
    public ResponseData<Void> deleteJob(@PathVariable("id") long id) {
        log.info("Request delete job={}", id);
        try {
            this.jobService.handleDeleteJob(id);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete job successfully");
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete job failed");
        }
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get job", description = "API for get detail job in databases")
    public ResponseData<Job> getJob(@PathVariable("id") long id) {
        log.info("Request get job={}", id);
        try {
            Job currentJob = this.jobService.handleGetJob(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Detail job successfully", currentJob);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get Detail job failed");
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Get list jobs", description = "API for get list jobs in databases")
    public ResponseData<ResPagination> getAllJob(@Filter Specification<Job> spec, Pageable pageable) {
        log.info("Request list Job");
        try {
            ResPagination currentJob = this.jobService.handleGetAllJobs(spec, pageable);
            return new ResponseData<>(HttpStatus.OK.value(), "Get list Job successfully", currentJob);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list Job failed");
        }
    }

}
