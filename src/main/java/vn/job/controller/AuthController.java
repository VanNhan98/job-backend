package vn.job.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.job.dto.request.LoginRequest;
import vn.job.dto.request.ResetPasswordDTO;
import vn.job.dto.response.ResRegisterDTO;
import vn.job.dto.response.ResponseCreateUser;
import vn.job.dto.response.TokenResponse;
import vn.job.dto.response.error.ResponseData;
import vn.job.dto.response.error.ResponseError;
import vn.job.model.User;
import vn.job.service.AuthService;
import vn.job.service.UserService;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    @Operation(summary = "Login user", description = "API for authenticate user")
    @PostMapping("/access")
    public ResponseData<TokenResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {

        log.info("Login user request={}", request);
        try {
            return new ResponseData<>(HttpStatus.OK.value(),"Login successfully",authService.authenticate(request,response));
        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Login user failed");
        }
    }

    @Operation(summary = "Refresh token", description = "API for refresh token")
    @PostMapping("/refresh")
    public ResponseData<TokenResponse> refresh(HttpServletRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(),"Refresh successfully",authService.refresh(request) );

        }catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Refresh failed");
        }
    }



    @Operation(summary = "Register user", description = "API for register new user")
    @PostMapping("/register")
    public ResponseData<ResRegisterDTO> register(@Valid @RequestBody User user) {
        log.info("Request create user={}", user.getUsername());
        try {
            String hashPassWord = this.passwordEncoder.encode(user.getPassword());
            user.setPassword(hashPassWord);
            ResRegisterDTO currentUser = this.userService.handleRegisterUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User register successfully", currentUser);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "User register failed");
        }
    }


    @Operation(summary = "Logout user", description = "API for logout user")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(authService.logout(request,response), HttpStatus.OK);
    }


    @Operation(summary = "Forgot password", description = "API for forgot password")
    @PostMapping("/forgot-password")
    public ResponseData<String> forgotPassword(@RequestBody String email) throws MessagingException, UnsupportedEncodingException {
        return new ResponseData<>(HttpStatus.OK.value(), authService.forgotPassword(email));
    }

    @Operation(summary = "Reset password", description = "API for reset password")
    @GetMapping("/reset-password")
    public ResponseData<String> resetPassword(@RequestParam String secretKey) {
        return new ResponseData<>(HttpStatus.OK.value(),authService.resetPassword(secretKey));
    }


    @Operation(summary = "Change password", description = "API for change password")
    @PostMapping("/change-password")
    public ResponseData<String> changePassword(@RequestBody ResetPasswordDTO request) {
        return new ResponseData<>(HttpStatus.OK.value(), authService.changePassword(request));
    }


}
