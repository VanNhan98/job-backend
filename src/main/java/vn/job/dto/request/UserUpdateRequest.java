package vn.job.dto.request;

import lombok.Getter;
import lombok.Setter;
import vn.job.util.Gender;

import java.io.Serializable;


@Getter
@Setter
public class UserUpdateRequest implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String dateOfBirth;
    private Gender gender;
    private String phone;
    private String country;
    private String language;
}
