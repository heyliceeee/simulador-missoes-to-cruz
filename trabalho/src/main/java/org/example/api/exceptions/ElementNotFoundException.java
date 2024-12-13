package org.example.api.exceptions;

/**
 * Exceção lançada quando um elemento específico não é encontrado.
 * <p>
 * Essa exceção é utilizada para indicar que um elemento esperado não 
 * pôde ser localizado no contexto da aplicação. A mensagem de erro 
 * especificada será prefixada por "ERROR:" para padronização.
 * </p>
 *
 * @see Exception
 */
public class ElementNotFoundException extends Exception {

    /**
     * Constrói uma nova {@code ElementNotFoundException} com uma mensagem de detalhe especificada.
     * <p>
     * A mensagem de erro fornecida será automaticamente prefixada com "ERROR:".
     * </p>
     *
     * @param m a mensagem de detalhe que descreve o motivo da exceção.
     */
    public ElementNotFoundException(String m) {
        super("ERROR: " + m);
    }
}
