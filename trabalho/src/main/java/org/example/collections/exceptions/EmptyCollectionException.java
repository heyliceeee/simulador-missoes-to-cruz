package org.example.collections.exceptions;

/**
 * Exceção lançada quando uma operação é realizada em uma coleção vazia.
 * <p>
 * Esta exceção é uma subclasse de {@code RuntimeException} e é usada para
 * indicar que uma coleção está vazia e, portanto, a operação solicitada
 * não pode ser realizada.
 * </p>
 */
public class EmptyCollectionException extends RuntimeException {

    /**
     * Constrói uma nova exceção com uma mensagem específica para o tipo da coleção.
     *
     * @param collectionType O tipo da coleção que está vazia.
     */
    public EmptyCollectionException(String collectionType) {
        super(collectionType + " is empty.");
    }
}
