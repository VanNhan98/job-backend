package vn.job.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;
import vn.job.dto.request.UserCreateRequest;
import vn.job.dto.request.UserUpdateRequest;
import vn.job.dto.response.UserResponse;
import vn.job.util.Gender;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
public class UserController {

    @Operation(summary = "Get all users", description = "API get list of users from databases")
    @GetMapping("/list")
    public List<UserResponse> getAllUsers(@RequestParam(required = false) String keyword,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int pageSize) {
        List<UserResponse> list =  new ArrayList<>();
        list.add(UserResponse.builder().id(1).
                firstName("Nhan").lastName("Mai").phone("12345").dateOfBirth("1998").username("mainhan").country("US")
                .gender(Gender.male).language("vietnam").build());
        list.add(UserResponse.builder().id(2).
                firstName("Nhan").lastName("Mai").phone("12345").dateOfBirth("1998").username("mainhan").country("US")
                .gender(Gender.male).language("vietnam").build());
        list.add(UserResponse.builder().id(3    ).
                firstName("Nhan").lastName("Mai").phone("12345").dateOfBirth("1998").username("mainhan").country("US")
                .gender(Gender.male).language("vietnam").build());
        return list;
    }

    @Operation(summary = "Get user detail", description = "API get user by id from databases")
    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable @Min(1) long userId) {
        return UserResponse.builder().id(1).
                firstName("Nhan").lastName("Mai").phone("12345").dateOfBirth("1998").username("mainhan").country("US")
                .gender(Gender.male).language("vietnam").build();
    }

    @Operation(summary = "Create new user", description = "API for insert user into databases")
    @PostMapping("/add")
    public int addUser(@Valid @RequestBody UserCreateRequest request) {

        return 1;
    }

    @Operation(summary = "Update user", description = "API for update user into databases")
    @PutMapping("/update")
    public void updateUser( @RequestBody UserUpdateRequest request)  {



    }

    @Operation(summary = "Change password", description = "API for change password user into databases")
    @PatchMapping("/change-password/{userId}")
    public String changePassword(@PathVariable @Min(1) long userId, @Min(1) @RequestBody String  password) {
        return "Password changed successfully";
    }

    @Operation(summary = "Delete user", description = "API for delete user from databases")
    @DeleteMapping("/delete{userId}")
    public String deleteUser(@Min(1) @PathVariable  long userId) {
        return "User" + userId + " is deleted successfully";
    }

}
