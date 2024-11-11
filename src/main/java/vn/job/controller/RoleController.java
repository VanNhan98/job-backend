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
import vn.job.dto.response.resume.ResDetailResume;
import vn.job.model.Permission;
import vn.job.model.Role;
import vn.job.service.RoleService;

@RestController
@RequestMapping("/roles")
@Slf4j
@RequiredArgsConstructor
@Tag(name ="Role Controller")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/add")
    @Operation(summary = "Create new role", description = "API for insert role into databases")
    public ResponseData<Role> addRole(@Valid @RequestBody Role role) {
        log.info("Request create role={}", role.getName());
        try {
            Role currentRole = this.roleService.handleCreateRole(role);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Role created successfully", currentRole);
        } catch (Exception e) {
            log.error("Error creating role: {}", e.getMessage());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Role created failed");
        }
    }

    @PutMapping("/update")
    @Operation(summary = "Update role", description = "API for update role into databases")
    public ResponseData<Role> updateRole(@Valid @RequestBody Role role) {
        log.info("Request update role={}", role.getName());
        try {

            Role currentRole = this.roleService.handleUpdateRole(role);
            return new ResponseData<>(HttpStatus.OK.value(), "Update role successfully", currentRole);
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "update role failed");
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Get list roles", description = "API for insert company into databases")
    public ResponseData<ResPagination> getAllRoles(@Filter Specification<Role> spec, Pageable pageable) {
        log.info("Request list role");
        try {
            ResPagination currentRole = this.roleService.handleGetAllRoles(spec, pageable);
            return new ResponseData<>(HttpStatus.OK.value(), "Get list role successfully", currentRole);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list role failed");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role", description = "API for get role into databases")
    public ResponseData<Role> getRole(@PathVariable("id") long id) {
        log.info("Request get role={}", id);
        try {
            Role currentRole = this.roleService.handleGetRole(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Get Detail role successfully", currentRole);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get Detail role failed");
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Get list role", description = "API for get list role into databases")
    public ResponseData<Void> deleteRole(@PathVariable("id") long id) {
        log.info("Request delete role={}", id);
        try {
            this.roleService.handleDeleteRole(id);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete role successfully");
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete role failed");
        }
    }

}
