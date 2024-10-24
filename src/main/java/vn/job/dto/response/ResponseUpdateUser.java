package vn.job.dto.response;

import jakarta.persistence.PreUpdate;
import lombok.Builder;
import lombok.Getter;
import vn.job.service.JwtService;

import java.io.Serializable;
import java.util.Date;
@Getter
@Builder
public class ResponseUpdateUser implements Serializable {
    private long id;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String gender;

    private String phone;

    private String username;

    private String address;

    private String language;

    private Date updatedAt;

    private String updatedBy;


}
