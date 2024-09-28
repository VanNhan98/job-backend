package vn.job.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.job.dto.response.ResponseCreateUser;
import vn.job.dto.response.ResponseUpdateUser;
import vn.job.dto.response.error.ResponseData;
import vn.job.dto.response.error.ResponseError;
import vn.job.model.User;
import vn.job.service.UserService;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User Controller")
public class UserController {
    private final UserService userService;

//    @Operation(summary = "Get all users", description = "API get list of users from databases")
//    @GetMapping("/list")
//    public ResponseData<List<UserCreateRequest>> getAllUsers(@RequestParam(required = false) String keyword,
//                                                             @RequestParam(defaultValue = "0") int page,
//                                                             @RequestParam(defaultValue = "10") int size) {
//        return new ResponseData<>(HttpStatus.OK.value(), "user" ,List.of(new UserCreateRequest("mai", "nhan", "han@gmail.com","12312"),
//                new UserCreateRequest("mai", "nhan", "han@gmail.com","12312")));
//    }
//
//    @Operation(summary = "Get user detail", description = "API get user by id from databases")
//    @GetMapping("/{userId}")
//    public ResponseData<UserCreateRequest> getUserById(@PathVariable @Min(1) long userId) {
//        return new ResponseData<>(HttpStatus.OK.value(),"user", new UserCreateRequest("mai", "nhan", "han@gmail.com","12312"));
//    }

    @Operation(summary = "Create new user", description = "API for insert user into databases")
    @PostMapping("/add")
    public ResponseData<ResponseCreateUser> addUser(@Valid @RequestBody User user) {
        try {
            ResponseCreateUser currentUser = userService.handleCreateUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User add successfully", currentUser);

        }catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user failed");
        }
    }

        @Operation(summary = "Update user", description = "API for update user into databases")
    @PutMapping("/update")
    public ResponseData<ResponseUpdateUser> updateUser(@Valid @RequestBody User reqUser)  {
            try {
                ResponseUpdateUser currentUser = userService.handleUpdateUser(reqUser);
                return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User updated successfully",currentUser);

            }catch (Exception e) {
                log.error("errorMessage= {} ", e.getMessage(), e.getCause());
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user failed");
            }
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
