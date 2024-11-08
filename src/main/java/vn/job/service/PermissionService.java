package vn.job.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.job.dto.response.ResPagination;
import vn.job.exception.EntityAlreadyExistsException;
import vn.job.exception.IdInvalidException;
import vn.job.model.Company;
import vn.job.model.Permission;
import vn.job.model.Role;
import vn.job.repository.PermissionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Permission handleCreatePermission(Permission permission) {
        log.info("-------------------Create Permission ----------------");
        if(isExistPermission(permission)) {
            throw new EntityAlreadyExistsException("Permission already exists");
        }
        return this.permissionRepository.save(permission);
    }

    public boolean isExistPermission(Permission permission) {
        return this.permissionRepository.existsByApiPathAndMethodAndModule(
                permission.getApiPath(),
                permission.getMethod(),
                permission.getModule());
    }

    public Permission handleUpdatePermission(Permission permission) {
        log.info("-------------------Update Permission ----------------");
        Permission currentPermission = this.handleGetPermissionById(permission.getId());
        currentPermission.setName(permission.getName());
        currentPermission.setApiPath(permission.getApiPath());
        currentPermission.setMethod(permission.getMethod());
        currentPermission.setModule(permission.getModule());
        if(isExistPermission(permission)) {
            throw new EntityAlreadyExistsException("Permission already exists");
        }
        return this.permissionRepository.save(permission);
    }

    private Permission handleGetPermissionById(long id) {
        return this.permissionRepository.findById(id).orElseThrow(() -> new IdInvalidException("Id not found"));
    }

    public ResPagination handleGetAllPermissions(Specification<Permission> spec, Pageable pageable) {
        log.info("---------------get list permission---------------");
        Page<Permission> pagePermission = this.permissionRepository.findAll(spec, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pagePermission.getTotalPages());
        mt.setTotal(pagePermission.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pagePermission.getContent());
        return rs;
    }

    public void handleDeletePermission(long id) {
        log.info("---------------delete permission---------------");
        Permission permissionDB = this.handleGetPermissionById(id);
        if(permissionDB.getRoles() != null) {
            List<Role> roles = permissionDB.getRoles();
            for(Role role: roles) {
                role.getPermissions().remove(permissionDB);
            }
        }
        this.permissionRepository.deleteById(id);
    }
}
