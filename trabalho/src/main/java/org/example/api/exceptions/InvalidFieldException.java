package org.example.api.exceptions;

/**
 * Exceção lançada quando um campo específico do JSON é inválido.
 */
public class InvalidFieldException extends Exception {
    public InvalidFieldException(String message) {
        super(message);
    }
}