package org.example.api.exceptions;

/**
 * Excecao lancada quando a estrutura do JSON e inválida.
 */
public class InvalidJsonStructureException extends Exception {
    public InvalidJsonStructureException(String message) {
        super(message);
    }
}