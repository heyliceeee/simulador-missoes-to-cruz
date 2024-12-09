package org.example.api.exceptions;

/**
 * Exceção lançada quando a estrutura do JSON é inválida.
 */
public class InvalidJsonStructureException extends Exception {
    public InvalidJsonStructureException(String message) {
        super(message);
    }
}