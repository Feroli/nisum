package com.evaluacion.nisum.service;

import com.evaluacion.nisum.dto.UserRequest;
import com.evaluacion.nisum.dto.UserResponse;
import com.evaluacion.nisum.exception.ValidationException;
import com.evaluacion.nisum.model.User;
import com.evaluacion.nisum.repository.UserRepository;
import helper.ModelGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Debería registrar un usuario correctamente")
    void shouldRegisterUserSuccessfully() {
        User testUser = ModelGenerator.generateUser(1);

        UserRequest userRequest = new UserRequest();
        userRequest.setName(testUser.getName());
        userRequest.setEmail(testUser.getEmail());
        userRequest.setPassword("Password123");
        userRequest.setPhones(testUser.getPhones());

        UserResponse userResponse = userService.registerUser(userRequest);

        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getCreated()).isNotNull();
        assertThat(userResponse.getModified()).isNotNull();
        assertThat(userResponse.getLastLogin()).isNotNull();
        assertThat(userResponse.getToken()).isNotEmpty();
        assertThat(userResponse.getIsActive()).isTrue();

        User userInDb = userRepository.findByEmail(userRequest.getEmail()).orElse(null);
        assertThat(userInDb).isNotNull();
        assertThat(userInDb.getEmail()).isEqualTo(userRequest.getEmail());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el email es inválido")
    void shouldThrowValidationExceptionWhenEmailIsInvalid() {
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword("Password123");
        userRequest.setEmail("invalid-email");
        userRequest.setPhones(ModelGenerator.generatePhones(1));

        assertThatThrownBy(() -> userService.registerUser(userRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Formato de correo inválido");
    }

    @Test
    @DisplayName("Debería lanzar EmailAlreadyExistsException si el email ya está registrado")
    void shouldThrowEmailAlreadyExistsExceptionWhenEmailAlreadyExists() {
        User existingUser = ModelGenerator.generateUser(1);
        userRepository.save(existingUser);

        UserRequest userRequest = new UserRequest();
        userRequest.setName(existingUser.getName());
        userRequest.setEmail(existingUser.getEmail());
        userRequest.setPassword("Password123");
        userRequest.setPhones(existingUser.getPhones());

        assertThatThrownBy(() -> userService.registerUser(userRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT)
                .extracting("reason")
                .isEqualTo("El correo ya está registrado");
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si la contraseña es inválida")
    void shouldThrowValidationExceptionWhenPasswordIsInvalid() {
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword("");
        userRequest.setName("Fer");
        userRequest.setEmail("fer@sample.com");
        userRequest.setPhones(ModelGenerator.generatePhones(1));

        assertThatThrownBy(() -> userService.registerUser(userRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessage("La contraseña no cumple con los requisitos");
    }
}