package org.example.api.exceptions;

/**
 * Exceção lançada quando um campo específico de um JSON é considerado inválido.
 * <p>
 * Essa exceção é utilizada para indicar que um campo fornecido no JSON 
 * não atende aos critérios esperados, como formato incorreto, valor ausente 
 * ou outros problemas de validação.
 * </p>
 *
 * @see Exception
 */
public class InvalidFieldException extends Exception {

    /**
     * Constrói uma nova {@code InvalidFieldException} com a mensagem de detalhe especificada.
     *
     * @param message a mensagem de detalhe que descreve o campo inválido e o motivo do erro.
     */
    public InvalidFieldException(String message) {
        super(message);
    }
}
