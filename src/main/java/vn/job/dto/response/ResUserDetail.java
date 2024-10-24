package vn.job.dto.response;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;


}
