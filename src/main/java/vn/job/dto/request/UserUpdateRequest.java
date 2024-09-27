package vn.job.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.job.util.EnumValue;
import vn.job.util.Gender;
import vn.job.util.PhoneNumber;

import java.io.Serializable;


@Getter
@Setter
public class UserUpdateRequest implements Serializable {
//    private long id;

    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @NotBlank(message = "lastName must be not blank")
    private String lastName;

    @NotNull(message = "username must be not null")
    private String username;

    @NotNull(message = "dateOfBirth must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String dateOfBirth;

    @NotNull(message = "gender must be not null")
    @EnumValue(name = "gender", enumClass = Gender.class)
    private String gender;

    @PhoneNumber
    private String phone;

    @NotEmpty(message = "addresses can not empty")
    private String address;

    private String language;
}
