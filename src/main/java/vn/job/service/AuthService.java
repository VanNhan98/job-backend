package vn.job.service;


import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.job.dto.request.LoginRequest;
import vn.job.dto.request.ResetPasswordDTO;
import vn.job.dto.response.TokenResponse;
import vn.job.exception.InvalidDataException;
import vn.job.model.Token;
import vn.job.model.User;
import vn.job.repository.UserRepository;
import vn.job.util.TokenType;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final EmailService emailService;

    public TokenResponse authenticate(LoginRequest request, HttpServletResponse response) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email or Password failed"));

        String accessToken = this.jwtService.generateAccessToken(user, request.getEmail());
        String refreshToken = this.jwtService.generateRefreshToken(user, request.getEmail());

        // save into db
        tokenService.saveToken(Token.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build()
        );

        // Tạo cookie chứa refreshToken
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600 * 24 * 30)
                .build();

        // Set cookie vào response
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // response fe
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .build();
    }

    public TokenResponse refresh(HttpServletRequest request) {
        log.info("---------------refresh password---------------");

        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // validate
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }


        // extract email or user the refresh token
        final String email = jwtService.extractEmail(refreshToken, TokenType.REFRESH_TOKEN);


        // check into db
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        if (!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN, user)) {
            throw new InvalidDataException("Token is invalid");
        }

        // generate new token
        String accessToken = this.jwtService.generateAccessToken(user, email);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .build();

    }

    public String logout(HttpServletRequest request) {
        log.info("---------------logout user---------------");
        String refreshToken = request.getHeader("x-token");
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }

        // extract user from token
         String email = jwtService.extractEmail(refreshToken, TokenType.REFRESH_TOKEN);
       Token currentToken = this.tokenService.findTokenByEmail(email);


        return  this.tokenService.deleteToken(currentToken);

    }

    public String forgotPassword(String email) throws MessagingException, UnsupportedEncodingException {
        // check email exist or not
        User user = this.userService.findUserByEmail(email);

        // User is  active or inactive
        if(!user.isEnabled()) {
            throw new InvalidDataException("User is not active");
        }

        // generate reset token
         String resetToken = jwtService.generateResetToken(user,email );

        // Gửi email với link reset password
        try {
            this.emailService.sendResetPasswordLink(email, resetToken);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send reset password email to {}", email, e);
            throw new RuntimeException("Failed to send reset password email");
        }

        return "Password reset link has been sent successfully";
    }

    public String resetPassword(String secretKey) {
        log.info("---------------reset password---------------");
        final String email = jwtService.extractEmail(secretKey, TokenType.RESET_TOKEN);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        if (!jwtService.isValid(secretKey, TokenType.RESET_TOKEN, user)) {
            throw new InvalidDataException("Token is invalid");
        }

        return "Reset successfully";
    }

    public String changePassword(ResetPasswordDTO request) {
        log.info("---------------change password---------------");
        User user = isValidUserByToken(request.getSecretKey());
        user.setPassword(request.getPassword());
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new InvalidDataException("Passwords do not match");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        this.userRepository.save(user);
        return "Changed password successfully";
    }

    private User isValidUserByToken(String secretKey) {
        final String email = jwtService.extractEmail(secretKey, TokenType.RESET_TOKEN);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isValid(secretKey, TokenType.RESET_TOKEN, user)) {
            throw new InvalidDataException("Not allow access with token");
        }
        return user;
    }
}
