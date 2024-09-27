package vn.job.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.job.dto.request.UserCreateRequest;
import vn.job.dto.request.UserUpdateRequest;
import vn.job.dto.response.ResponseData;
import vn.job.dto.response.UserResponse;
import vn.job.util.Gender;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
@Tag(name = "User Controller")
public class UserController {

    @Operation(summary = "Get all users", description = "API get list of users from databases")
    @GetMapping("/list")
    public ResponseData<List<UserCreateRequest>> getAllUsers(@RequestParam(required = false) String keyword,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return new ResponseData<>(HttpStatus.OK.value(), "user" ,List.of(new UserCreateRequest("mai", "nhan", "han@gmail.com","12312"),
                new UserCreateRequest("mai", "nhan", "han@gmail.com","12312")));
    }

    @Operation(summary = "Get user detail", description = "API get user by id from databases")
    @GetMapping("/{userId}")
    public ResponseData<UserCreateRequest> getUserById(@PathVariable @Min(1) long userId) {
        return new ResponseData<>(HttpStatus.OK.value(),"user", new UserCreateRequest("mai", "nhan", "han@gmail.com","12312"));
    }

    @Operation(summary = "Create new user", description = "API for insert user into databases")
    @PostMapping("/add")
    public ResponseData<?> addUser(@Valid @RequestBody UserCreateRequest request) {

        return new ResponseData<>(HttpStatus.CREATED.value(), "User add successfully", 1);
    }

//    @Operation(summary = "Update user", description = "API for update user into databases")
//    @PutMapping("/update")
//    public ResponseData<?> updateUser(@Valid @RequestBody UserUpdateRequest request)  {
//        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User updated successfully");
//    }
@Operation(summary = "Update user", description = "API for update user into databases")
    @PutMapping("/update/{id}")
    public ResponseData<?> updateUser(@Valid @PathVariable @Min(1) long id, @RequestBody UserUpdateRequest request)  {
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User updated successfully");
    }


@Operation(summary = "Change password", description = "API for change password user into databases")
    @PatchMapping("/change-password/{userId}")
    public ResponseData<Void> changePassword(@PathVariable @Min(1) long userId, @Min(1) @RequestBody String  password) {
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User change successfully" );
    }

    @Operation(summary = "Delete user", description = "API for delete user from databases")
    @DeleteMapping("/delete{userId}")
    public ResponseData<?> deleteUser(@Min(1) @PathVariable  long userId) {
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User delete successfully" );
    }

}
