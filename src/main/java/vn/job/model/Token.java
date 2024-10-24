package vn.job.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="tbl_token")
public class Token extends BaseEntity {
    private String email;
    private String username;

    private String accessToken;

    private String refreshToken;

}
