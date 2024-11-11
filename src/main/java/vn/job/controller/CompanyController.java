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
import vn.job.dto.response.ResPagination;
import vn.job.dto.response.error.ResponseData;
import vn.job.dto.response.error.ResponseError;
import vn.job.model.Company;
import vn.job.service.CompanyService;


@RestController
@RequestMapping("/companies")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Company Controller")
public class CompanyController {

    private final CompanyService companyService;
    @Operation(summary = "Create new company", description = "API for insert company into databases")
    @PostMapping("/add")
    public ResponseData<Company> addCompany(@Valid @RequestBody Company reqcompany) {
            log.info("Request add company");
        try {
            Company currentCompany = this.companyService.handleCreateCompany(reqcompany);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Company add successfully", currentCompany);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add company failed");
        }
    }

    @PutMapping("/update")
    @Operation(summary = "Update company", description = "API for update company into databases")
    public ResponseData<Company> updateCompany(@Valid @RequestBody Company reqcompany) {
        log.info("Request update company");
        try {
            Company currentCompany = this.companyService.handleUpdateCompany(reqcompany);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Company updated successfully", currentCompany);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update company failed");
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete company", description = "API for delete company into databases")
    public ResponseData<Void> deleteCompany(@PathVariable("id") long id) {
        log.info("Request delete company={}", id);
        try {
            this.companyService.handleDeleteCompany(id);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete company successfully");
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete company failed");
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Get list companies", description = "API for get list companies into databases")
    public ResponseData<ResPagination> getAllCompany(@Filter Specification<Company> spec, Pageable pageable) {
        log.info("Request list company");
        try {
            ResPagination currentCompany = this.companyService.handleGetAllCompanies(spec, pageable);
            return new ResponseData<>(HttpStatus.OK.value(), "Get list company successfully", currentCompany);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list company failed");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get company", description = "API for get company by id into databases")
    public ResponseData<Company> getCompany(@PathVariable("id") long id) {
        log.info("Request get company={}", id);
        try {
            Company currentCompany = this.companyService.handleGetCompany(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Detail company successfully", currentCompany);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get Detail company failed");
        }
    }

}
