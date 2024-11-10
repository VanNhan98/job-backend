package vn.job.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vn.job.service.UserService;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final UserService userService;

    private final CustomizeFilter preFilter;

    private String[] WHITE_LIST = {
            "/", "/auth/**","/users/confirm/**", "/storage/**","/send-mail"
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers(HttpMethod.GET, "/companies/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/jobs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/skills/**").permitAll()
//                        .requestMatchers("/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                .authenticationEntryPoint(customAuthenticationEntryPoint))
                .authenticationProvider(provider()).addFilterBefore(preFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider provider() {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setPasswordEncoder(passwordEncoder());
        dao.setUserDetailsService(this.userService.userDetailsService());
        return dao;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurity ->
                webSecurity.ignoring()
                        .requestMatchers("/actuator/**", "/v3/**", "/webjars/**", "/swagger-ui*/*swagger-initializer.js", "/swagger-ui*/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}

