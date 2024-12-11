package org.example.api.exceptions;

/**
 * Excecao lancada quando um campo especifico do JSON e inválido.
 */
public class InvalidFieldException extends Exception {
    public InvalidFieldException(String message) {
        super(message);
    }
}