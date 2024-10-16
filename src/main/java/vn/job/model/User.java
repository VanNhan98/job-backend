package vn.job.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
import vn.job.util.EnumValue;
import vn.job.util.Gender;
import vn.job.util.PhoneNumber;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name ="tbl_user")
public class User extends BaseEntity
//        implements UserDetails
{

    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @NotBlank(message = "lastName must be not blank")
    private String lastName;

    @NotNull(message = "dateOfBirth must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String dateOfBirth;

    @NotNull(message = "gender must be not null")
//    @EnumValue(name = "gender", enumClass = Gender.class)
    @Enumerated(EnumType.STRING)
    private Gender  gender;

    @PhoneNumber
    private String phone;

    @Email(message = "email invalid format")
    private String email;

    @NotNull(message = "username must be not null")
    private String username;

    @NotBlank(message = "password must be not null")
    private String password;


    @NotEmpty(message = "addresses can not empty")
    private String address;

    private String language;



}
