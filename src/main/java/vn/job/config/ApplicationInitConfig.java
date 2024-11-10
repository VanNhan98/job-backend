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

            // Lấy tất cả các quyền (Permission) từ cơ sở dữ liệu
            List<Permission> allPermissions = this.permissionRepository.findAll();

            // Kiểm tra xem vai trò "ADMIN" đã tồn tại chưa
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

            // Kiểm tra xem người dùng với email "admin@gmail.com" đã tồn tại chưa
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
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
                adminUser.setRole(adminRole);
                this.userRepository.save(adminUser);
                log.info("Created admin user with email admin@gmail.com");
            } else {
                log.warn("Admin user with email admin@gmail.com already exists");
            }
        };
    }
}
