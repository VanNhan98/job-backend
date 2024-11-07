package vn.job.dto.response.resume;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ResCreateResume {
    private long id;
    private Date createdAt;
    private String createdBy;
}
