package com.evaluacion.nisum.service;

import com.evaluacion.nisum.exception.ValidationException;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@SpringBootTest
public class ValidationServiceTest {

    @Autowired
    private ValidationService validationService;

    @ParameterizedTest(name = "{index} => email=''{0}'', escenario=''{1}''")
    @MethodSource("validEmailProvider")
    @DisplayName("Validación de correos electrónicos válidos")
    void shouldPassWhenEmailIsValid(String email, String scenario) {
        assertThatCode(() -> validationService.validateEmail(email))
                .doesNotThrowAnyException();
    }

    static Stream<Arguments> validEmailProvider() {
        return Stream.of(
                Arguments.of("test@example.com", "Formato estándar"),
                Arguments.of("user.name+tag+sorting@example.com", "Con '+' y subdirección"),
                Arguments.of("user_name@example.co.uk", "Dominio con código de país"),
                Arguments.of("user-name@example.org", "Con guiones en la parte local"),
                Arguments.of("user.name@example.com", "Con punto en la parte local"),
                Arguments.of("user@subdomain.example.com", "Subdominio en el dominio"),
                Arguments.of("firstname.lastname@example.com", "Nombre y apellido separados por punto")
        );
    }

    @ParameterizedTest(name = "{index} => email=''{0}'', escenario=''{1}''")
    @MethodSource("invalidEmailProvider")
    @DisplayName("Validación de correos electrónicos inválidos")
    void shouldThrowExceptionWhenEmailIsInvalid(String email, String scenario) {
        assertThatThrownBy(() -> validationService.validateEmail(email))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Formato de correo inválido");
    }

    static Stream<Arguments> invalidEmailProvider() {
        return Stream.of(
                Arguments.of("plainaddress", "Sin '@' y dominio"),
                Arguments.of("@no-local-part.com", "Sin parte local"),
                Arguments.of("Outlook Contact <outlook-contact@domain.com>", "Nombre con ángulo"),
                Arguments.of("no-at.domain.com", "Sin '@'"),
                Arguments.of("no.tld@domain", "Sin TLD"),
                Arguments.of(";beginning-semicolon@domain.co.uk", "Caracteres especiales al inicio"),
                Arguments.of("middle-semicolon@domain.co;uk", "Caracteres especiales en el dominio"),
                Arguments.of("trailing-semicolon@domain.com;", "Punto y coma al final"),
                Arguments.of("\"email+leading-quotes@domain.com", "Comillas mal formadas"),
                Arguments.of("email+middle\"-quotes@domain.com", "Comillas mal ubicadas"),
                Arguments.of("\"quoted-local-part\"@domain.com", "Comillas en la parte local"),
                Arguments.of("\"quoted@domain.com", "Comillas mal formadas"),
                Arguments.of("multiple@domains@domain.com", "Múltiples '@'"),
                Arguments.of("spaces in local@domain.com", "Espacios en parte local"),
                Arguments.of("spaces-in-domain@dom ain.com", "Espacios en dominio"),
                Arguments.of("underscores-in-domain@dom_ain.com", "Guiones bajos en dominio"),
                Arguments.of("pipe-in-domain@example.com|gov", "Caracteres no permitidos en dominio"),
                Arguments.of("comma,in-local@domain.com", "Coma en parte local"),
                Arguments.of("comma-in-domain@domain,com", "Coma en dominio"),
                Arguments.of("pound-sign-in-local#domain.com", "Almohadilla en parte local")
        );
    }

    @ParameterizedTest(name = "{index} => contraseña=''{0}'', escenario=''{1}''")
    @MethodSource("validPasswordProvider")
    @DisplayName("Validación de contraseñas válidas")
    void shouldPassWhenPasswordIsValid(String password, String scenario) {
        assertThatCode(() -> validationService.validatePassword(password))
                .doesNotThrowAnyException();
    }

    static Stream<Arguments> validPasswordProvider() {
        return Stream.of(
                Arguments.of("Password123", "Contraseña estándar válida"),
                Arguments.of("SecurePass1", "Contraseña con combinación de letras y números"),
                Arguments.of("AnotherPass2", "Otra contraseña válida"),
                Arguments.of("StrongPass3", "Contraseña fuerte"),
                Arguments.of("ValidPass4", "Contraseña válida con número al final"),
                Arguments.of("GoodPass5", "Otra contraseña válida"),
                Arguments.of("ValidPassword6", "Contraseña más larga"),
                Arguments.of("Passw0rdStrong", "Contraseña con cero en lugar de 'o'"),
                Arguments.of("MySecurePassword7", "Contraseña personalizada"),
                Arguments.of("ComplexPass8", "Contraseña compleja")
        );
    }

    @ParameterizedTest(name = "{index} => contraseña=''{0}'', escenario=''{1}''")
    @MethodSource("invalidPasswordProvider")
    @DisplayName("Validación de contraseñas inválidas")
    void shouldThrowExceptionWhenPasswordIsInvalid(String password, String scenario) {
        assertThatThrownBy(() -> validationService.validatePassword(password))
                .isInstanceOf(ValidationException.class)
                .hasMessage("La contraseña no cumple con los requisitos");
    }

    static Stream<Arguments> invalidPasswordProvider() {
        return Stream.of(
                Arguments.of("password123", "Sin mayúsculas"),
                Arguments.of("PASSWORD123", "Sin minúsculas"),
                Arguments.of("Password", "Sin números"),
                Arguments.of("Pass1", "Longitud insuficiente"),
                Arguments.of("12345678", "Solo números"),
                Arguments.of("", "Cadena vacía"),
                Arguments.of("       ", "Solo espacios"),
                Arguments.of("中文密码123", "Caracteres Unicode - Chino"),
                Arguments.of("Пароль123", "Caracteres Unicode - Ruso"),
                Arguments.of("パスワード123", "Caracteres Unicode - Japonés"),
                Arguments.of("كلمهالسر123", "Caracteres Unicode - Árabe")
        );
    }
}