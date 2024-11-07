package vn.job.dto.request;

import lombok.Getter;
import vn.job.util.StatusEnum;

@Getter
public class RequestUpdateResume {
    private long id;
    private StatusEnum status;
}
