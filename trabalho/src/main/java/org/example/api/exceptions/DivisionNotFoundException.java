package org.example.api.exceptions;

/**
 * Exceção lançada quando uma divisão não é encontrada no mapa.
 */
public class DivisionNotFoundException extends Exception {
    public DivisionNotFoundException(String message) {
        super(message);
    }
}
