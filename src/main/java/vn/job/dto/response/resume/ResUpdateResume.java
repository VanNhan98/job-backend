package vn.job.dto.response.resume;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ResUpdateResume {
    private long id;
    private String updatedBy;
    private Date updatedAt;
}
