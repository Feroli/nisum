package com.evaluacion.nisum.controller;

import com.evaluacion.nisum.dto.UserRequest;
import com.evaluacion.nisum.dto.UserResponse;
import com.evaluacion.nisum.exception.ValidationException;
import com.evaluacion.nisum.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import helper.ModelGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String REGISTRO_PATH = "/registro";

    @Test
    @DisplayName("Debería registrar un usuario correctamente y retornar 201 CREATED")
    void shouldRegisterUserSuccessfully() throws Exception {
        UserRequest userRequest = UserRequest.builder()
                .name("Juan Pérez")
                .email("juan.perez@example.com")
                .password("Password123")
                .phones(ModelGenerator.generatePhones(1))
                .build();

        UserResponse userResponse = UserResponse.builder()
                .id(UUID.randomUUID())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token("jwt-token-123")
                .isActive(true)
                .build();

        Mockito.when(userService.registerUser(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post(REGISTRO_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userResponse.getId().toString()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.modified").exists())
                .andExpect(jsonPath("$.lastLogin").exists())
                .andExpect(jsonPath("$.token").value("jwt-token-123"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    @DisplayName("Debería retornar 409 CONFLICT si el email ya está registrado")
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        UserRequest userRequest = UserRequest.builder()
                .name("Juan Pérez")
                .email("juan.perez@example.com")
                .password("Password123")
                .phones(null)
                .build();

        Mockito.when(userService.registerUser(any(UserRequest.class)))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.CONFLICT, "El correo ya está registrado"));

        mockMvc.perform(post(REGISTRO_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El correo ya está registrado"));
    }

    @Test
    @DisplayName("Debería retornar 400 BAD REQUEST si el payload es inválido")
    void shouldReturnBadRequestForInvalidPayload() throws Exception {
        UserRequest userRequest = UserRequest.builder()
                .name("Juan Pérez")
                .email("email-invalido")
                .password("pass")
                .phones(null)
                .build();

        Mockito.when(userService.registerUser(any(UserRequest.class)))
                .thenThrow(new ValidationException("Formato de correo inválido"));

        mockMvc.perform(post(REGISTRO_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Formato de correo inválido"));
    }
}