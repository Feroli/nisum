package com.evaluacion.nisum.service;

import com.evaluacion.nisum.exception.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class DefaultValidationService implements ValidationService {

    @Value("${validation.email.regex}")
    private String emailRegex;

    @Value("${validation.password.regex}")
    private String passwordRegex;

    @Override
    public void validateEmail(String email) {
        if (!Pattern.matches(emailRegex, email)) {
            throw new ValidationException("Formato de correo inválido");
        }
    }

    @Override
    public void validatePassword(String password) {
        if (!Pattern.matches(passwordRegex, password)) {
            throw new ValidationException("La contraseña no cumple con los requisitos");
        }
    }
}
