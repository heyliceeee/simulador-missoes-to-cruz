package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface que define o contrato para a execução de uma simulação envolvendo o personagem To Cruz,
 * seus movimentos pelo mapa, batalhas contra inimigos e coleta de itens.
 * 
 * Uma implementação típica desta interface gerenciará a lógica de:
 * <ul>
 *   <li>Movimentar To Cruz até um objetivo em uma divisão específica do mapa.</li>
 *   <li>Enfrentar e derrotar inimigos pelo caminho, se houver.</li>
 *   <li>Coletar itens encontrados, incluindo kits de vida ou outros recursos.</li>
 *   <li>Calcular o resultado final da simulação (sucesso ou falha), dependendo do objetivo e da vida restante.</li>
 * </ul>
 */
public interface ISimulacao {
    
    /**
     * Executa a simulação visando alcançar a divisão objetivo e, se necessário, retornar de forma
     * segura. Esta simulação pode envolver a escolha de um caminho, combates com inimigos,
     * coleta de itens e avaliação da vida de To Cruz.
     *
     * @param divisaoObjetivo A divisão do mapa que representa o objetivo da simulação.
     *                        Pode ser, por exemplo, onde se encontra um alvo a resgatar.
     * @throws IllegalArgumentException se a divisão objetivo for nula ou inválida.
     * @throws org.example.api.exceptions.ElementNotFoundException se ocorrer algum erro durante a
     *         navegação pelo mapa (por exemplo, divisões ou itens não encontrados).
     */
    void executar(IDivisao divisaoObjetivo);

    /**
     * Obtém a quantidade de vida restante de To Cruz após as interações e combates ocorridos
     * na simulação.
     *
     * @return Um valor inteiro representando a vida restante de To Cruz.
     */
    int getVidaRestante();

    /**
     * Retorna o status final da simulação, que pode ser:
     * <ul>
     *   <li><b>"SUCESSO"</b>: Se To Cruz conseguiu cumprir o objetivo (por exemplo, resgatar um alvo)
     *       e continuar vivo.</li>
     *   <li><b>"FALHA"</b>: Se To Cruz foi derrotado ou não conseguiu cumprir o objetivo.</li>
     * </ul>
     *
     * @return Uma string indicando o resultado final da simulação ("SUCESSO" ou "FALHA").
     */
    String getStatus();

    /**
     * Obtém a divisão final onde To Cruz se encontra ao término da simulação.
     * Isto pode ser o local do objetivo, uma saída do edifício, ou qualquer outra
     * divisão relevante após a conclusão da lógica de movimentação.
     *
     * @return A divisão final na qual To Cruz está localizado.
     */
    IDivisao getDivisaoFinal();

    /**
     * Obtém o caminho percorrido por To Cruz durante toda a simulação, incluindo as divisões
     * visitadas no caminho para o objetivo e, possivelmente, no retorno.
     *
     * @return Uma lista não ordenada de divisões representando a sequência exata de locais
     *         percorridos por To Cruz.
     */
    ArrayUnorderedList<IDivisao> getCaminhoPercorrido();

    /**
     * Obtém os itens que foram coletados por To Cruz durante a simulação.
     * Estes itens podem incluir kits de vida, armas, chaves ou outros elementos definidos
     * pelo contexto do jogo.
     *
     * @return Uma lista não ordenada dos itens coletados.
     */
    ArrayUnorderedList<IItem> getItensColetados();

    /**
     * Obtém a lista de inimigos derrotados por To Cruz durante a simulação.
     * Esta informação pode ser útil para relatórios de estatísticas, 
     * pontuações, ou para verificar se todos os inimigos de uma área foram eliminados.
     *
     * @return Uma lista não ordenada dos inimigos derrotados na simulação.
     */
    ArrayUnorderedList<IInimigo> getInimigosDerrotados();
}
