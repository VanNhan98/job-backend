package vn.job.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.job.model.Permission;
import vn.job.model.Role;
import vn.job.model.User;
import vn.job.repository.PermissionRepository;
import vn.job.repository.RoleRepository;
import vn.job.repository.UserRepository;
import vn.job.util.GenderEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner() {

        return args -> {
            log.info("Application Init Config");
            log.info("Starting Application Runner...");
            long countPermissions = this.permissionRepository.count();
            long countRoles = this.roleRepository.count();
            long countUsers = this.userRepository.count();

            if (countPermissions == 0) {
                ArrayList<Permission> arr = new ArrayList<>();
                arr.add(new Permission("Create a company", "/companies/add", "POST", "COMPANIES"));
                arr.add(new Permission("Update a company", "/companies/update", "PUT", "COMPANIES"));
                arr.add(new Permission("Delete a company", "/companies/delete/{id}", "DELETE", "COMPANIES"));
                arr.add(new Permission("Get a company by id", "/companies/{id}", "GET", "COMPANIES"));
                arr.add(new Permission("Get companies with pagination", "/companies/list", "GET", "COMPANIES"));

                arr.add(new Permission("Create a job", "/jobs/add", "POST", "JOBS"));
                arr.add(new Permission("Update a job", "/jobs/update", "PUT", "JOBS"));
                arr.add(new Permission("Delete a job", "/jobs/delete/{id}", "DELETE", "JOBS"));
                arr.add(new Permission("Get a job by id", "/jobs/{id}", "GET", "JOBS"));
                arr.add(new Permission("Get jobs with pagination", "/jobs/list", "GET", "JOBS"));

                arr.add(new Permission("Create a permission", "/permissions/add", "POST", "PERMISSIONS"));
                arr.add(new Permission("Update a permission", "/permissions/update", "PUT", "PERMISSIONS"));
                arr.add(new Permission("Delete a permission", "/permissions/delete/{id}", "DELETE", "PERMISSIONS"));
                arr.add(new Permission("Get a permission by id", "/permissions/{id}", "GET", "PERMISSIONS"));
                arr.add(new Permission("Get permissions with pagination", "/permissions/list", "GET", "PERMISSIONS"));

                arr.add(new Permission("Create a resume", "/resumes/add", "POST", "RESUMES"));
                arr.add(new Permission("Update a resume", "/resumes/update", "PUT", "RESUMES"));
                arr.add(new Permission("Delete a resume", "/resumes/delete/{id}", "DELETE", "RESUMES"));
                arr.add(new Permission("Get a resume by id", "/resumes/{id}", "GET", "RESUMES"));
                arr.add(new Permission("Get resumes with pagination", "/resumes/list", "GET", "RESUMES"));

                arr.add(new Permission("Create a role", "/roles/add", "POST", "ROLES"));
                arr.add(new Permission("Update a role", "/roles/update", "PUT", "ROLES"));
                arr.add(new Permission("Delete a role", "/roles/delete/{id}", "DELETE", "ROLES"));
                arr.add(new Permission("Get a role by id", "/roles/{id}", "GET", "ROLES"));
                arr.add(new Permission("Get roles with pagination", "/roles/list", "GET", "ROLES"));

                arr.add(new Permission("Create a user", "/users/add", "POST", "USERS"));
                arr.add(new Permission("Update a user", "/users/update", "PUT", "USERS"));
                arr.add(new Permission("Delete a user", "/users/delete/{id}", "DELETE", "USERS"));
                arr.add(new Permission("Get a user by id", "/users/{id}", "GET", "USERS"));
                arr.add(new Permission("Get users with pagination", "/users/list", "GET", "USERS"));

                arr.add(new Permission("Create a subscriber", "/subscribers/add", "POST", "SUBSCRIBERS"));
                arr.add(new Permission("Update a subscriber", "/subscribers/update", "PUT", "SUBSCRIBERS"));
                arr.add(new Permission("Delete a subscriber", "/subscribers/delete/{id}", "DELETE", "SUBSCRIBERS"));
                arr.add(new Permission("Get a subscriber by id", "/subscribers/{id}", "GET", "SUBSCRIBERS"));
                arr.add(new Permission("Get subscribers with pagination", "/subscribers/list", "GET", "SUBSCRIBERS"));

                arr.add(new Permission("Upload a file", "/files/upload", "GET", "FILES"));
                arr.add(new Permission("Send mail", "/send-mail", "POST", "MAILS"));

                this.permissionRepository.saveAll(arr);
            }

            if (countRoles == 0) {
                // Get all permissions from database
                List<Permission> allPermissions = this.permissionRepository.findAll();
                // check role "ADMIN" ?
                Role adminRole = roleRepository.findByName("ADMIN");
                if (adminRole == null) {
                    adminRole = new Role();
                    adminRole.setName("ADMIN");
                    adminRole.setDescription("Administrator");
                    adminRole.setActive(true);
                    adminRole.setPermissions(allPermissions);
                    this.roleRepository.save(adminRole);
                    log.info("Created ADMIN role");
                } else {
                    log.warn("ADMIN role already exists");
                }
            }


            if (countUsers == 0) {
                    User adminUser = new User();
                    adminUser.setEmail("admin@gmail.com");
                    adminUser.setPassword(passwordEncoder.encode("123456"));
                    adminUser.setFirstName("Van ");
                    adminUser.setLastName("Nhan");
                    adminUser.setDateOfBirth("25/1/1998");
                    adminUser.setGender(GenderEnum.MALE);
                    adminUser.setPhone("0912345678");
                    adminUser.setUsername("admin");
                    adminUser.setAddress("QN");
                    adminUser.setLanguage("vi");
                     Role adminRole = this.roleRepository.findByName("ADMIN");
                     if (adminRole != null) {
                    adminUser.setRole(adminRole);
                    }
                    this.userRepository.save(adminUser);
                    log.info("Created admin user with email admin@gmail.com");
            }

        };
    }
}
