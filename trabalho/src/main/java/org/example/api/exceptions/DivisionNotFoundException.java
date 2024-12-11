package org.example.api.exceptions;

/**
 * Excecao lancada quando uma divisao nao e encontrada no mapa.
 */
public class DivisionNotFoundException extends Exception {
    public DivisionNotFoundException(String message) {
        super(message);
    }
}
