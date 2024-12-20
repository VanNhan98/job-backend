package vn.job.dto.response;

import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import vn.job.service.JwtService;

import java.io.Serializable;
import java.util.Date;

@Getter
@Builder
public class ResponseCreateUser implements Serializable {
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

    private String createdBy;


    @Getter
    @Setter
    public static class  CompanyUser {
        private long id;
        private String name;
    }


    @Getter
    @Setter
    public static class RoleUser {
        private long id;
        private String name;
    }

}
