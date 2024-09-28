package vn.job.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.job.dto.response.ResponseCreateUser;
import vn.job.dto.response.ResponseUpdateUser;
import vn.job.exception.IdInvalidException;
import vn.job.model.User;
import vn.job.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public ResponseCreateUser handleCreateUser(User user) {
        //save
        User currentUser =  this.userRepository.save(user);

        // convert response
        ResponseCreateUser resUser = ResponseCreateUser.builder()
                .id(currentUser.getId())
                .firstName(currentUser.getFirstName())
                .lastName(currentUser.getLastName())
                .dateOfBirth(currentUser.getDateOfBirth())
                .gender(currentUser.getGender())
                .email(currentUser.getEmail())
                .phone(currentUser.getPhone())
                .username(currentUser.getUsername())
                .address(currentUser.getAddress())
                .language(currentUser.getLanguage())
                .createdAt(currentUser.getCreatedAt()).build();
        log.info("User created successfully");
        return resUser;

    }

    public ResponseUpdateUser handleUpdateUser(User user) {
        User currentUser = handleGetUserById(user.getId());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setDateOfBirth(user.getDateOfBirth());
        currentUser.setGender(user.getGender());
        currentUser.setPhone(user.getPhone());
        currentUser.setUsername(user.getUsername());
        currentUser.setAddress(user.getAddress());
        currentUser.setLanguage(user.getLanguage());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());

        //save
        User updatedUser = this.userRepository.save(currentUser);

        // convert response
        ResponseUpdateUser resUser = ResponseUpdateUser.builder()
               .id(updatedUser.getId())
               .firstName(updatedUser.getFirstName())
               .lastName(updatedUser.getLastName())
               .dateOfBirth(updatedUser.getDateOfBirth())
               .gender(updatedUser.getGender())
               .phone(updatedUser.getPhone())
                .username(updatedUser.getUsername())
               .address(updatedUser.getAddress())
                .language(updatedUser.getLanguage())
               .updatedAt(updatedUser.getUpdatedAt()).build();
        log.info("User updated successfully");
        return resUser;
    }

    private User handleGetUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IdInvalidException("User not found"));
    }
}
