package vn.job.dto.response;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;
import vn.job.service.JwtService;

import java.io.Serializable;
import java.util.Date;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDetail implements Serializable {
    private long id;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String gender;

    private String email;

    private String phone;

    private String username;

    private String address;

    private String language;

    private CompanyUser company;

    private RoleUser role;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyUser {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUser {
        private long id;
        private String name;
    }

}
