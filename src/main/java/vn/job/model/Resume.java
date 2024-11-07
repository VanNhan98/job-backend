package vn.job.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import vn.job.util.EnumValue;

import vn.job.util.StatusEnum;

@Getter
@Setter
@Entity
@Table(name = "tbl_resume")
public class Resume extends BaseEntity{
    @NotNull(message = "email must be not null")
    private String email;

    @NotNull(message = "upload CV failed")
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
