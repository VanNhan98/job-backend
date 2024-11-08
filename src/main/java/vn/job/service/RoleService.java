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
import vn.job.model.BaseEntity;
import vn.job.model.Permission;
import vn.job.model.Role;
import vn.job.repository.PermissionRepository;
import vn.job.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;



@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public Role handleCreateRole(Role role) {
        log.info("--------------------Create Role------------------");
        if(isExistName(role.getName())) {
            throw new EntityAlreadyExistsException("Role name already exists");
        }

        if(role.getPermissions() != null) {
            List<Long> listIdPermissions = role.getPermissions().stream().map(BaseEntity::getId).collect(Collectors.toList());
            List<Permission> dbPermission = this.permissionRepository.findByIdIn(listIdPermissions);
            role.setPermissions(dbPermission);
        }

        return this.roleRepository.save(role);
    }


    public Role handleUpdateRole(Role role) {
        log.info("--------------------Update Role------------------");
        Role currentRole = handleGetRoleById(role.getId());


        if(role.getPermissions() != null) {
            List<Long> listIdPermissions = role.getPermissions().stream().map(BaseEntity::getId).collect(Collectors.toList());
            List<Permission> dbPermission = this.permissionRepository.findByIdIn(listIdPermissions);
            role.setPermissions(dbPermission);
        }

        currentRole.setId(role.getId());
        currentRole.setName(role.getName());
        currentRole.setPermissions(role.getPermissions());


        return this.roleRepository.save(role);
    }

    public Role handleGetRoleById(long id) {
        return this.roleRepository.findById(id).orElseThrow(() -> new IdInvalidException("Id not found"));
    }


    public boolean isExistName(String name) {
        return roleRepository.existsByName(name);
    }

    public ResPagination handleGetAllRoles(Specification<Role> spec, Pageable pageable) {
        log.info("---------------get list permission---------------");
        Page<Role> pageRole = this.roleRepository.findAll(spec, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageRole.getTotalPages());
        mt.setTotal(pageRole.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageRole.getContent());
        return rs;
    }

    public void handleDeleteRole(long id) {
        log.info("---------------delete role---------------");
        Role role = handleGetRoleById(id);
        this.roleRepository.delete(role);
    }

    public Role handleGetRole(long id) {
        log.info("---------------get role---------------");
        return handleGetRoleById(id);

    }
}
