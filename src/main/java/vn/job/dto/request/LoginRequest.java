package vn.job.dto.request;


import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String email;
    private String password;
}
