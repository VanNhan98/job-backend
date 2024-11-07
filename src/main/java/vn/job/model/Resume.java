package vn.job.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.job.util.EnumValue;
import vn.job.util.GenderEnum;
import vn.job.util.StatusEnum;

@Getter
@Setter
@Entity
@Table(name = "tbl_resume")
public class Resume extends BaseEntity{
    private String email;

    private String url;

    @NotNull(message = "status must be not null")
    @EnumValue(name = "status", enumClass = StatusEnum.class)
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;
}
