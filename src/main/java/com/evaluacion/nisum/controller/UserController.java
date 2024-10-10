package com.evaluacion.nisum.controller;
import com.evaluacion.nisum.dto.UserRequest;
import com.evaluacion.nisum.dto.UserResponse;
import com.evaluacion.nisum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Registers a new user.
     *
     * @param userRequest the user registration request
     * @return the registered user details
     */
    @PostMapping("/registro")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.registerUser(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

}
