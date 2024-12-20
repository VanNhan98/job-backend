package vn.job.controller;


import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.job.dto.response.ResPagination;
import vn.job.dto.response.ResUserDetail;
import vn.job.dto.response.ResponseCreateUser;
import vn.job.dto.response.ResponseUpdateUser;
import vn.job.dto.response.error.ResponseData;
import vn.job.dto.response.error.ResponseError;
import vn.job.model.User;
import vn.job.service.UserService;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User Controller")
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;


    @Operation(summary = "Create new user", description = "API for insert user into databases")
    @PostMapping("/add")
    public ResponseData<ResponseCreateUser> addUser(@Valid @RequestBody User user) {
        log.info("Request create user={}", user.getUsername());
        try {
            String hashPassWord = this.passwordEncoder.encode(user.getPassword());
            user.setPassword(hashPassWord);
            ResponseCreateUser currentUser = userService.handleCreateUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User add successfully", currentUser);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user failed");
        }
    }

    @Operation(summary = "Update user", description = "API for update user into databases")
    @PutMapping("/update")
    public ResponseData<ResponseUpdateUser> updateUser(@Valid @RequestBody User reqUser) {
        log.info("Request update user={}", reqUser.getId());
        try {
            ResponseUpdateUser currentUser = userService.handleUpdateUser(reqUser);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User updated successfully", currentUser);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user failed");
        }
    }

    @Operation(summary = "Get user ", description = "API get user by id from databases")
    @GetMapping("/{userId}")
    public ResponseData<ResUserDetail> getUserById(@PathVariable long userId) {
        log.info("Request detail user={}", userId);
        try {
            ResUserDetail currentUser = this.userService.handleGetUser(userId);
            return new ResponseData<>(HttpStatus.OK.value(), "Get user successfully", currentUser);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get user failed");
        }
    }

    @Operation(summary = "Get all users", description = "API get list of users from databases")
    @GetMapping("/list")
    public ResponseData<ResPagination> getAllUsers(@Filter Specification<User> spec, Pageable pageable
    ) {
        log.info("Request list user");
        try {
            ResPagination currentUser = this.userService.handleGetAllUsers(spec, pageable);
            return new ResponseData<>(HttpStatus.OK.value(), "Get user successfully", currentUser);

        } catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get user failed");
        }
    }


    @Operation(summary = "Confirm email user", description = "API for confirm email user ")
    @GetMapping("/confirm/{userId}")
    public ResponseData<Void> confirmUser(@Min(1)  @PathVariable long userId, String secretCode) {
        log.info("Confirm user userId={}, secretCode={}", userId, secretCode);
        try {
            this.userService.confirmUser(userId, secretCode);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User confirm successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Confirm was failed");
        } finally {

        }
    }


    @Operation(summary = "Delete user", description = "API for delete user from databases")
    @DeleteMapping("/delete/{userId}")
    public ResponseData<Void> deleteUser(@PathVariable("userId") long userId) {
        try {
            this.userService.handleDeleteUserById(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "delete user successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete user fail");
        }
    }

}
