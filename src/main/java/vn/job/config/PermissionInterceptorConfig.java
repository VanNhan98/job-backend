package vn.job.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.job.service.UserService;

@Configuration
@RequiredArgsConstructor
public class PermissionInterceptorConfig implements WebMvcConfigurer {

    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/", "/auth/**", "/storage/**"
                ,"/companies/**"
                , "/jobs/**", "/skills/**", "/files/upload/**",
                "/subscribers/**", "/send-email/**",
                "/resumes/by-user/**"
        };
        registry.addInterceptor(getPermissionInterceptor()).excludePathPatterns(whiteList);

    }
}
