package org.example.api.exceptions;

/**
 * Exceção lançada quando uma divisão solicitada não é encontrada no mapa.
 * <p>
 * Essa exceção é utilizada para indicar que uma divisão específica não 
 * pôde ser localizada ou não existe no contexto atual da aplicação.
 * </p>
 *
 * @see Exception
 */
public class DivisionNotFoundException extends Exception {

    /**
     * Constrói uma nova {@code DivisionNotFoundException} com a mensagem de detalhe especificada.
     *
     * @param message a mensagem de detalhe que fornece mais contexto sobre a exceção.
     */
    public DivisionNotFoundException(String message) {
        super(message);
    }
}
