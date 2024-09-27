package vn.job.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.job.util.Gender;

import java.io.Serializable;


@Getter
@Setter
public class UserCreateRequest implements Serializable {
    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private Gender gender;

    private String phone;

    private String email;

    private String username;

    private String password;

    private String confirmPassword;

    private String country;

    private String language;

}
