package vn.job.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class LoginRequest implements Serializable {

    @NotBlank(message = "email must be not null")
    private String email;

    @NotBlank(message = "password must be not null")
    private String password;
}
