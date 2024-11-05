package vn.job.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResFileDTO {
    private String fileName;
    private Date updatedAt;
}
