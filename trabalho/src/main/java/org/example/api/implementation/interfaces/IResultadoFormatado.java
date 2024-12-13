package org.example.api.implementation.interfaces;

/**
 * Interface para representar e formatar os resultados de simulações.
 *
 * <p>
 * Essa interface define os métodos necessários para acessar os dados formatados
 * de um resultado de simulação, permitindo sua utilização de maneira simples e
 * padronizada.
 * </p>
 */
public interface IResultadoFormatado {

    /**
     * Obtém o identificador único do resultado da simulação.
     *
     * @return O identificador único da simulação.
     */
    String getId();

    /**
     * Obtém o nome da divisão inicial onde a simulação começou.
     *
     * @return O nome da divisão inicial.
     */
    String getDivisaoInicial();

    /**
     * Obtém o nome da divisão final onde a simulação terminou.
     *
     * @return O nome da divisão final.
     */
    String getDivisaoFinal();

    /**
     * Obtém o status da simulação, como "SUCESSO" ou "FALHA".
     *
     * @return O status da simulação.
     */
    String getStatus();

    /**
     * Obtém a quantidade de vida restante ao final da simulação.
     *
     * @return O valor da vida restante.
     */
    int getVidaRestante();

    /**
     * Obtém o trajeto percorrido durante a simulação, representado como um array de
     * divisões.
     *
     * @return Um array de strings com os nomes das divisões do trajeto.
     */
    String[] getTrajeto();

    /**
     * Obtém os nomes das divisões marcadas como entradas ou saídas no mapa da
     * simulação.
     *
     * @return Um array de strings com os nomes das divisões de entrada/saída.
     */
    String[] getEntradasSaidas();

    /**
     * Obtém o código identificador da missão associada à simulação.
     *
     * @return O código da missão.
     */
    String getCodigoMissao();

    /**
     * Obtém a versão da missão associada à simulação.
     *
     * @return A versão da missão.
     */
    int getVersaoMissao();
}
