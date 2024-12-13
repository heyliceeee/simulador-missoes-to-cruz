package org.example.api.implementation.interfaces;

/**
 * Interface para representar e formatar os resultados de simulacoes.
 *
 * <p>
 * Essa interface define os metodos necessarios para acessar os dados formatados
 * de um resultado de simulacao, permitindo sua utilizacao de maneira simples e
 * padronizada.
 * </p>
 */
public interface IResultadoFormatado {

    /**
     * Obtem o identificador unico do resultado da simulacao.
     *
     * @return O identificador unico da simulacao.
     */
    String getId();

    /**
     * Obtem o nome da divisao inicial onde a simulacao comecou.
     *
     * @return O nome da divisao inicial.
     */
    String getDivisaoInicial();

    /**
     * Obtem o nome da divisao final onde a simulacao terminou.
     *
     * @return O nome da divisao final.
     */
    String getDivisaoFinal();

    /**
     * Obtem o status da simulacao, como "SUCESSO" ou "FALHA".
     *
     * @return O status da simulacao.
     */
    String getStatus();

    /**
     * Obtem a quantidade de vida restante ao final da simulacao.
     *
     * @return O valor da vida restante.
     */
    int getVidaRestante();

    /**
     * Obtem o trajeto percorrido durante a simulacao, representado como um array de
     * divisoes.
     *
     * @return Um array de strings com os nomes das divisoes do trajeto.
     */
    String[] getTrajeto();

    /**
     * Obtem os nomes das divisoes marcadas como entradas ou saidas no mapa da
     * simulacao.
     *
     * @return Um array de strings com os nomes das divisoes de entrada/saida.
     */
    String[] getEntradasSaidas();

    /**
     * Obtem o codigo identificador da missao associada à simulacao.
     *
     * @return O codigo da missao.
     */
    String getCodigoMissao();

    /**
     * Obtem a versao da missao associada à simulacao.
     *
     * @return A versao da missao.
     */
    int getVersaoMissao();
}
