package org.example.collections.exceptions;

/**
 * Exceção lançada quando uma operação é realizada em uma lista vazia.
 * <p>
 * Esta exceção é uma subclasse de {@code RuntimeException} e é usada para
 * indicar que uma lista está vazia e, portanto, a operação solicitada
 * não pode ser realizada.
 * </p>
 */
public class EmptyListException extends RuntimeException {

    /**
     * Constrói uma nova exceção com a mensagem fornecida.
     *
     * @param message A mensagem que descreve o motivo da exceção.
     */
    public EmptyListException(String message) {
        super(message);
    }
}
