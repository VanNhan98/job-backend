package vn.job.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.job.util.Gender;
import vn.job.util.PhoneNumber;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name ="tbl_user")
public class User extends BaseEntity implements UserDetails, Serializable
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


    /**
     * @return Returns the authorities granted to the user. Cannot return null.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<String> roleList = this.roles.stream().map(role -> role.getRole().getName()).toList();
//        return roleList.stream().map(SimpleGrantedAuthority::new).toList();
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be authenticated.
     *
     * @return true if the user's account is valid (ie non-expired), false if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be authenticated.
     *
     * @return true if the user is not locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired credentials prevent authentication.
     *
     * @return true if the user's credentials are valid (ie non-expired), false if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
     *
     * @return true if the user is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
