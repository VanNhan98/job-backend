package vn.job.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_permission")
public class Permission extends BaseEntity{

    @NotBlank(message = "Name is must be not empty")
    private String name;

    @NotBlank(message = "ApiPath is must be not empty")
    private String apiPath;

    @NotBlank(message = "Method is must be not empty")
    private String method;

    @NotBlank(message = "Module is must be not empty")
    private String module;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    @JsonIgnore
    private List<Role> roles;
}