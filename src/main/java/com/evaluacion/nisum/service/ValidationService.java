package com.evaluacion.nisum.service;

import com.evaluacion.nisum.exception.ValidationException;

public interface ValidationService {
    void validateEmail(String email) throws ValidationException;
    void validatePassword(String password) throws ValidationException;
}
