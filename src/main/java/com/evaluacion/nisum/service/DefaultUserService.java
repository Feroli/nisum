package com.evaluacion.nisum.service;

import com.evaluacion.nisum.dto.UserRequest;
import com.evaluacion.nisum.dto.UserResponse;
import com.evaluacion.nisum.model.User;
import com.evaluacion.nisum.repository.UserRepository;
import com.evaluacion.nisum.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        validationService.validateEmail(userRequest.getEmail());
        validationService.validatePassword(userRequest.getPassword());

        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .token(jwtUtil.generateToken(userRequest.getName()))
                .phones(userRequest.getPhones())
                .build();

        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .token(user.getToken())
                .created(user.getCreated())
                .modified(user.getModified())
                .lastLogin(user.getLastLogin())
                .isActive(user.getIsActive())
                .build();
    }
}