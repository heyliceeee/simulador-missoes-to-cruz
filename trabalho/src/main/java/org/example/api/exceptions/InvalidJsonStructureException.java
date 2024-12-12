package org.example.api.exceptions;

/**
 * Excecao lancada quando a estrutura do JSON e invalida.
 */
public class InvalidJsonStructureException extends Exception {
    public InvalidJsonStructureException(String message) {
        super(message);
    }
}