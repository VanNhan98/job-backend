package vn.job.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_subscriber")
public class Subscriber extends BaseEntity{
    @NotBlank(message = "Name is must be not empty")
    private String name;

    @NotBlank(message = "Email is must be not empty")
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"subscriber"})
    @JoinTable(name = "tbl_subscriber_skill", joinColumns = @JoinColumn(name = "subscriber_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;
}
