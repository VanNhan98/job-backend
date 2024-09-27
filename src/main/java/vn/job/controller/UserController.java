package vn.job.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping("/list")
    public List<UserResponse> getAllUsers() {
        List<UserResponse> list =  new ArrayList<UserResponse>();
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

    @GetMapping("/{userId}")
    public UserResponse getUserById() {
        return UserResponse.builder().id(1).
                firstName("Nhan").lastName("Mai").phone("12345").dateOfBirth("1998").username("mainhan").country("US")
                .gender(Gender.male).language("vietnam").build();
    }


    @PostMapping("/add")
    public UserResponse addUser(@RequestBody UserCreateRequest request) {
        UserResponse currentUser = UserResponse.builder().id(1).firstName(request.getFirstName()).lastName(request.getLastName())
                .phone(request.getPhone()).dateOfBirth(request.getDateOfBirth()).username(request.getUsername())
                .country(request.getCountry()).gender(request.getGender()).language(request.getLanguage()).build();
        return currentUser;
    }

    @PutMapping("/update/{userId}")
    public UserResponse updateUser(@PathVariable long userId, @RequestBody UserUpdateRequest request)  {
        UserResponse updateUser = UserResponse.builder().id(userId).firstName(request.getFirstName()).lastName(request.getLastName())
                .phone(request.getPhone()).dateOfBirth(request.getDateOfBirth()).username(request.getUsername())
                .country(request.getCountry()).gender(request.getGender()).language(request.getLanguage()).build();
        return updateUser;

    }

    @PatchMapping("/change-password")
    public String changePassword() {
        return "Password changed successfully";
    }

    @DeleteMapping("/delete{userId}")
    public String deleteUser(long id) {
        return "User" + id + " is deleted successfully";
    }

}
