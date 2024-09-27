package vn.job.dto.response;

import lombok.Builder;
import lombok.Getter;
import vn.job.util.Gender;

import java.io.Serializable;

@Getter
@Builder
public class UserResponse implements Serializable {
    private long id;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String gender;

    private String phone;

    private String username;

    private String country;

    private String language;
}
