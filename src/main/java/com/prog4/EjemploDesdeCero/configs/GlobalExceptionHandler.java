package com.prog4.EjemploDesdeCero.configs;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.prog4.EjemploDesdeCero.configs.exceptions.CustomException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones personalizadas.
     */
    @ExceptionHandler(CustomException.class)
    public ProblemDetail handleCustomException(CustomException ex) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());

        problem.setTitle(ex.getStatus().getReasonPhrase());

        if (ex.getErrors() != null && !ex.getErrors().isEmpty()) {
            problem.setProperty("errors", ex.getErrors());
        }

        return problem;

    }

    /**
     * Maneja las excepciones de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Error de validación");

        problem.setDetail("Revise los campos indicados.");

        problem.setProperty("errors", errors);

        return problem;
    }

    /**
     * Maneja las excepciones genéricas.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado");
        problem.setTitle("Error interno del servidor");
        problem.setProperty("errors", List.of("Contacte al administrador"));
        return problem;
    }
}

