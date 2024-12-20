package vn.job.service;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.job.dto.response.*;
import vn.job.exception.EntityAlreadyExistsException;
import vn.job.exception.IdInvalidException;
import vn.job.model.Company;
import vn.job.model.Role;
import vn.job.model.User;
import vn.job.repository.RoleRepository;
import vn.job.repository.UserRepository;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final EmailService emailService;

    private  final CompanyService companyService;

    private final RoleService roleService;

    private final RoleRepository roleRepository;


    public UserDetailsService userDetailsService() {
        return email -> this.userRepository.findByEmail(email).orElseThrow(() ->new UsernameNotFoundException("User not found"));
    }


    public ResponseCreateUser handleCreateUser(User user) throws MessagingException, UnsupportedEncodingException {
        log.info("---------------create user---------------");
        if (isEmailExist(user.getEmail())) {
            throw new EntityAlreadyExistsException("Email already exists: " + user.getEmail());
        }

        // check company
        if(user.getCompany() != null) {
            Optional<Company> companyOptional = Optional.ofNullable(this.companyService.handleGetCompany(user.getCompany().getId()));
            user.setCompany(companyOptional.orElse(null));
        }
        // check role
        if(user.getRole() != null) {
            Optional<Role> roleOptional = Optional.ofNullable(this.roleService.handleGetRoleById(user.getRole().getId()));
            user.setRole(roleOptional.orElse(null));
        }


        //save
        User currentUser =  this.userRepository.save(user);

        // send email
        if(user.getId() != null) {
            // send email confirm here
            emailService.sendConfirmLink(user.getEmail(), user.getId(), "secretCode");
        }

        // convert response
        ResponseCreateUser.CompanyUser companyUser = null;
        if (currentUser.getCompany() != null) {
            companyUser = new ResponseCreateUser.CompanyUser();
            companyUser.setId(currentUser.getCompany().getId());
            companyUser.setName(currentUser.getCompany().getName());
        }

        ResponseCreateUser.RoleUser roleUser = null;
        if (currentUser.getCompany() != null) {
            roleUser = new ResponseCreateUser.RoleUser();
            roleUser.setId(currentUser.getRole().getId());
            roleUser.setName(currentUser.getRole().getName());
        }

        ResponseCreateUser resUser = ResponseCreateUser.builder()
                .id(currentUser.getId())
                .firstName(currentUser.getFirstName())
                .lastName(currentUser.getLastName())
                .dateOfBirth(currentUser.getDateOfBirth())
                .gender(String.valueOf(currentUser.getGender()))
                .email(currentUser.getEmail())
                .phone(currentUser.getPhone())
                .username(currentUser.getUsername())
                .address(currentUser.getAddress())
                .language(currentUser.getLanguage())
                .company(companyUser)
                .role(roleUser)
                .createdAt(currentUser.getCreatedAt())
                .createdBy(currentUser.getCreatedBy())
                .build();

        log.info("User created successfully");
        return resUser;

    }

    public ResponseUpdateUser handleUpdateUser(User user) {
        log.info("---------------update user---------------");
        User currentUser = handleGetUserById(user.getId());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setDateOfBirth(user.getDateOfBirth());
        currentUser.setGender(user.getGender());
        currentUser.setPhone(user.getPhone());
        currentUser.setUsername(user.getUsername());
        currentUser.setAddress(user.getAddress());
        currentUser.setLanguage(user.getLanguage());
        currentUser.setCompany(user.getCompany());

        // check company
        if(user.getCompany() != null) {
            Optional<Company> companyOptional = Optional.ofNullable(this.companyService.handleGetCompany(user.getCompany().getId()));
            currentUser.setCompany(companyOptional.orElse(null));
        }

        // check role
        if(user.getRole() != null) {
            Optional<Role> roleOptional = Optional.ofNullable(this.roleService.handleGetRoleById(user.getRole().getId()));
            currentUser.setRole(roleOptional.orElse(null));
        }

        // save
        User updatedUser = this.userRepository.save(currentUser);

        ResponseUpdateUser.CompanyUser companyUser = null;
        if (updatedUser.getCompany() != null) {
            companyUser = new ResponseUpdateUser.CompanyUser();
            companyUser.setId(updatedUser.getCompany().getId());
            companyUser.setName(updatedUser.getCompany().getName());
        }

        ResponseUpdateUser.RoleUser roleUser = null;
        if (currentUser.getRole() != null) { // Sửa lại ở đây
            roleUser = new ResponseUpdateUser.RoleUser();
            roleUser.setId(currentUser.getRole().getId());
            roleUser.setName(currentUser.getRole().getName());
        }


        // convert response
        ResponseUpdateUser resUser = ResponseUpdateUser.builder()
               .id(updatedUser.getId())
               .firstName(updatedUser.getFirstName())
               .lastName(updatedUser.getLastName())
               .dateOfBirth(updatedUser.getDateOfBirth())
               .gender(String.valueOf(updatedUser.getGender()))
               .phone(updatedUser.getPhone())
                .username(updatedUser.getUsername())
               .address(updatedUser.getAddress())
                .language(updatedUser.getLanguage())
                .company(companyUser)
                .role(roleUser)
               .updatedAt(updatedUser.getUpdatedAt())
                .updatedBy(currentUser.getUpdatedBy()).build();
        log.info("User updated successfully");
        return resUser;
    }

    public ResPagination handleGetAllUsers(Specification<User> spec, Pageable pageable) {
        log.info("---------------get all user---------------");
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);

        List<ResUserDetail> listUser = pageUser.getContent()
                .stream()
                .map(user -> this.handleGetUser(user.getId())
                 )
                .collect(Collectors.toList());
        rs.setResult(listUser);
        log.info("Get All Users successfully");
        return rs;
    }


    public ResUserDetail handleGetUser(long id) {
        log.info("---------------get detail user---------------");

        User currentUser = handleGetUserById(id);

        ResUserDetail.CompanyUser companyUser = null;
        if (currentUser.getCompany() != null) {
            companyUser = new ResUserDetail.CompanyUser();
            companyUser.setId(currentUser.getCompany().getId());
            companyUser.setName(currentUser.getCompany().getName());
        }

        ResUserDetail.RoleUser roleUser = null;
        if (currentUser.getRole() != null) {
            roleUser = new ResUserDetail.RoleUser();
            roleUser.setId(currentUser.getRole().getId());
            roleUser.setName(currentUser.getRole().getName());
        }

        ResUserDetail resUser = ResUserDetail.builder()
                .id(currentUser.getId())
                .firstName(currentUser.getFirstName())
                .lastName(currentUser.getLastName())
                .dateOfBirth(currentUser.getDateOfBirth())
                .gender(String.valueOf(currentUser.getGender()))
                .phone(currentUser.getPhone())
                .email(currentUser.getEmail())
                .username(currentUser.getUsername())
                .address(currentUser.getAddress())
                .language(currentUser.getLanguage())
                .company(companyUser)
                .role(roleUser)
                .updatedAt(currentUser.getUpdatedAt())
                .updatedBy(currentUser.getUpdatedBy())
                .createdAt(currentUser.getCreatedAt())
                .createdBy(currentUser.getCreatedBy())
                .build();
        log.info("Get User successfully");
        return resUser;
    }
    public void handleDeleteUserById(long id) {
        log.info("---------------delete user---------------");
        User currentUser = handleGetUserById(id);
        this.userRepository.deleteById(currentUser.getId());
        log.info("Delete successfully");
    }

    private User handleGetUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IdInvalidException("Id  not found"));
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }


    public void confirmUser( long userId, String secretCode) {
        log.info("Confirm");

    }

    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new IdInvalidException("User not found"));
    }

    public ResRegisterDTO handleRegisterUser(User user) {
        log.info("---------------register user---------------");
        if (isEmailExist(user.getEmail())) {
            throw new EntityAlreadyExistsException("Email already exists: " + user.getEmail());
        }


//        Role userRole = this.roleRepository.findByName("USER");
//        if(userRole == null) {
//            Role newRole = new Role();
//            newRole.setName("USER");
//            userRole = this.roleRepository.save(newRole);
//        }
//
//        user.setRole(userRole);
        
        User newUser = this.userRepository.save(user);

        ResRegisterDTO resRegisterDTO = ResRegisterDTO.builder()
                .id(newUser.getId())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .dateOfBirth(newUser.getDateOfBirth())
                .gender(String.valueOf(newUser.getGender()))
                .phone(newUser.getPhone())
                .email(newUser.getEmail())
                .password(newUser.getPassword())
                .username(newUser.getUsername())
                .address(newUser.getAddress())
                .language(newUser.getLanguage())
                .createdAt(newUser.getCreatedAt())
                .build();
        return resRegisterDTO;
    }
}
