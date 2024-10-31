package vn.job.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name ="tbl_company")
public class Company extends BaseEntity {

    @NotBlank(message = "name must be not blank")
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @NotBlank(message = "address must be not blank")
    private String address;

    @NotEmpty(message = "logo must be upload")
    private String logo;

}