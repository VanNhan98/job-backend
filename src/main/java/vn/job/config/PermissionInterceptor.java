package vn.job.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import vn.job.exception.InvalidDataException;
import vn.job.model.Permission;
import vn.job.model.Role;
import vn.job.model.User;
import vn.job.service.JwtService;
import vn.job.service.UserService;

import java.util.List;

@Component
@Slf4j
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private  UserService userService;

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info(">>> RUN preHandle");
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String httpMethod = request.getMethod(); // Lấy HTTP Method

        // check permission
        String email = JwtService.getCurrentUserEmail().isPresent() ? JwtService.getCurrentUserEmail().get() : "";
        if (email != null && !email.isEmpty()) {
            User user = this.userService.findUserByEmail(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream().anyMatch(item -> item.getApiPath().equals(path)
                            && item.getMethod().equals(httpMethod));
                    if (isAllow == false) {
                        throw new InvalidDataException("you not access enpoint");
                    }
                } else {
                    throw new InvalidDataException("you not access  enpoint");
                }
            }
        }
        return true;

    }

}

