package org.example.api.exceptions;

/**
 * Exceção lançada quando a estrutura de um JSON é considerada inválida.
 * <p>
 * Essa exceção é utilizada para indicar que o JSON fornecido possui uma estrutura 
 * incorreta ou inesperada, como chaves ausentes, formato inadequado, ou outros 
 * problemas de conformidade com o modelo esperado.
 * </p>
 *
 * @see Exception
 */
public class InvalidJsonStructureException extends Exception {

    /**
     * Constrói uma nova {@code InvalidJsonStructureException} com a mensagem de detalhe especificada.
     *
     * @param message a mensagem de detalhe que descreve o problema na estrutura do JSON.
     */
    public InvalidJsonStructureException(String message) {
        super(message);
    }
}
