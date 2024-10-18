package vn.job.dto.response.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ApiErrorResponse implements Serializable {
    private Date timestamp;
    private int status;
    private String message;
}
