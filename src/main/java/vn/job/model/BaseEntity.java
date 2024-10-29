package vn.job.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.job.service.JwtService;

import java.util.Date;


@Getter
@Setter
@MappedSuperclass

public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String createdBy;

    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = new Date();
        this.createdBy = JwtService.getCurrentUserLogin().isPresent() ? JwtService.getCurrentUserLogin().get() : "";
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = JwtService.getCurrentUserLogin().isPresent() ? JwtService.getCurrentUserLogin().get() : "";
        this.updatedAt = new Date();
    }
}
