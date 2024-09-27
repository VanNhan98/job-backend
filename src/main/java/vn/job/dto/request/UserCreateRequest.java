package vn.job.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.job.util.EnumValue;
import vn.job.util.Gender;
import vn.job.util.PhoneNumber;

import java.io.Serializable;


@Getter
@Setter
public class UserCreateRequest implements Serializable {

    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @NotBlank(message = "firstName must be not blank")
    private String lastName;

    @NotNull(message = "dateOfBirth must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String dateOfBirth;

    @NotNull(message = "gender must be not null")
    @EnumValue(name = "gender", enumClass = Gender.class)
//    @Enumerated(EnumType.STRING)
    private Gender gender;

    @PhoneNumber
    private String phone;

    @Email(message = "email invalid format")
    private String email;

    @NotNull(message = "username must be not null")
    private String username;

    @NotNull(message = "password must be not null")
    private String password;

    private String confirmPassword;

    @NotEmpty(message = "addresses can not empty")
    private String address;

    private String language;

    public UserCreateRequest(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }
}
