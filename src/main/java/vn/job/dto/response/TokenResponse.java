package vn.job.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class TokenResponse implements Serializable {
    private long id;
    private String accessToken;
    private String refreshToken;

    private long expiresIn;

}