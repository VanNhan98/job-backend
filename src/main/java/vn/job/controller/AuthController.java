package vn.job.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor

public class AuthController {
    @PostMapping("/login")
    public String login(String email, String password) {
        return "success";
    }

    @PostMapping("/logout")
    public String logout(){
        return "success";
    }
}