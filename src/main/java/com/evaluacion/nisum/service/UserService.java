package com.evaluacion.nisum.service;

import com.evaluacion.nisum.dto.UserRequest;
import com.evaluacion.nisum.dto.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRequest userRequest);
}
