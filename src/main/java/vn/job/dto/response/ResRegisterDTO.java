package vn.job.dto.response;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@Builder
public class ResRegisterDTO {
    private long id;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String gender;

    private String email;

    private String password;

    private String phone;

    private String username;

    private String address;

    private String language;


    private Date createdAt;


}
