package vn.job.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.job.util.EnumValue;
import vn.job.util.Gender;
import vn.job.util.LevelEnum;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_job")
public class Job extends BaseEntity {

    @NotNull(message = "name must be not null")
    private String name;

    private String location;

    private double salary;

    private int quantity;

    @NotNull(message = "level must be not null")
    @EnumValue(name = "level", enumClass = LevelEnum.class)
    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"jobs"})
    @JoinTable(name = "tbl_job_skill", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;
}
