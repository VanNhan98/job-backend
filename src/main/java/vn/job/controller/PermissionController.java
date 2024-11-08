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
import vn.job.dto.response.ResPagination;
import vn.job.dto.response.error.ResponseData;
import vn.job.dto.response.error.ResponseError;
import vn.job.dto.response.resume.ResCreateResume;
import vn.job.model.Company;
import vn.job.model.Permission;
import vn.job.model.Resume;
import vn.job.service.PermissionService;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PermissionController {

    private final PermissionService permissionService;
    @PostMapping("/add")
    public ResponseData<Permission> addPermission(@Valid @RequestBody Permission permission) {
        log.info("Request create permission={}", permission.getName());
        try {

            Permission currentPermission = this.permissionService.handleCreatePermission(permission);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Add permission successfully", currentPermission);
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Add permission failed");
        }
    }

    @PutMapping("/update")
    public ResponseData<Permission> updatePermission(@Valid @RequestBody Permission permission) {
        log.info("Request update permission={}", permission.getName());
        try {

            Permission currentPermission = this.permissionService.handleUpdatePermission(permission);
            return new ResponseData<>(HttpStatus.OK.value(), "update permission successfully", currentPermission);
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "update permission failed");
        }
    }


    @GetMapping("/list")
    public ResponseData<ResPagination> getAllPermission(@Filter Specification<Permission> spec, Pageable pageable) {
        log.info("Request list permission");
        try {
            ResPagination currentPermission = this.permissionService.handleGetAllPermissions(spec, pageable);
            return new ResponseData<>(HttpStatus.OK.value(), "Get list permission successfully", currentPermission);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list permission failed");
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseData<Void> deletePermission(@PathVariable("id") long id) {
        log.info("Request delete permission={}", id);
        try {
            this.permissionService.handleDeletePermission(id);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete permission successfully");
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete permission failed");
        }
    }

}
