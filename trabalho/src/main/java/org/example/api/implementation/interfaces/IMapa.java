package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.models.Ligacao;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.LinkedList;

/**
 * Representa a interface para o mapa do sistema.
 *
 * <p>
 * O mapa e estruturado como um grafo que contem divisoes, conexoes entre elas,
 * inimigos, itens e alvos. Essa interface define as operacoes relacionadas à
 * manipulacao e consulta do mapa.
 * </p>
 */
public interface IMapa {

    /**
     * Adiciona uma nova divisao ao mapa.
     *
     * @param nomeDivisao O nome da divisao a ser adicionada.
     * @throws IllegalArgumentException Se o nome da divisao for nulo ou vazio.
     */
    void adicionarDivisao(String nomeDivisao);

    /**
     * Adiciona uma ligacao entre duas divisoes no mapa.
     *
     * @param nomeDivisao1 O nome da primeira divisao.
     * @param nomeDivisao2 O nome da segunda divisao.
     * @throws IllegalArgumentException Se qualquer nome de divisao for nulo ou
     *                                  vazio.
     */
    void adicionarLigacao(String nomeDivisao1, String nomeDivisao2);

    ArrayUnorderedList<Ligacao> getLigacoes();

    /**
     * Adiciona um inimigo a uma divisao especifica.
     *
     * @param nomeDivisao O nome da divisao.
     * @param inimigo     O inimigo a ser adicionado.
     * @throws IllegalArgumentException Se o nome da divisao ou o inimigo for nulo.
     */
    void adicionarInimigo(String nomeDivisao, IInimigo inimigo);

    /**
     * Adiciona um item a uma divisao especifica.
     *
     * @param nomeDivisao O nome da divisao.
     * @param item        O item a ser adicionado.
     * @throws IllegalArgumentException Se o nome da divisao ou o item for nulo.
     */
    void adicionarItem(String nomeDivisao, IItem item);

    /**
     * Marca uma divisao como entrada/saida.
     *
     * @param nomeDivisao O nome da divisao a ser marcada.
     * @throws IllegalArgumentException Se o nome da divisao for nulo ou vazio.
     */
    void adicionarEntradaSaida(String nomeDivisao);

    /**
     * Retorna uma divisao pelo nome.
     *
     * @param nomeDivisao O nome da divisao a ser buscada.
     * @return A divisao correspondente.
     * @throws IllegalArgumentException Se o nome da divisao for nulo ou vazio.
     * @throws RuntimeException         Se a divisao nao for encontrada.
     */
    IDivisao getDivisaoPorNome(String nomeDivisao);

    /**
     * Verifica se e possivel mover de uma divisao para outra.
     *
     * @param divisao1 O nome da primeira divisao.
     * @param divisao2 O nome da segunda divisao.
     * @return {@code true} se o movimento for possivel; {@code false} caso
     *         contrario.
     */
    boolean podeMover(String divisao1, String divisao2);

    /**
     * Define um alvo em uma divisao especifica.
     *
     * @param nomeDivisao O nome da divisao.
     * @param tipo        O tipo do alvo.
     * @throws IllegalArgumentException Se o nome da divisao ou o tipo do alvo for
     *                                  nulo ou vazio.
     */
    void definirAlvo(String nomeDivisao, String tipo);

    /**
     * Remove o alvo do mapa.
     */
    void removerAlvo();

    /**
     * Obtem o alvo atual do mapa.
     *
     * @return O alvo atual, ou {@code null} se nenhum alvo estiver definido.
     */
    IAlvo getAlvo();

    /**
     * Obtem as conexoes de uma divisao.
     *
     * @param divisao A divisao cujas conexoes devem ser buscadas.
     * @return Uma lista de divisoes conectadas.
     */
    ArrayUnorderedList<IDivisao> obterConexoes(IDivisao divisao);

    /**
     * Obtem todas as divisoes do mapa.
     *
     * @return Uma lista de todas as divisoes.
     */
    ArrayUnorderedList<IDivisao> getDivisoes();

    /**
     * Obtem os nomes das divisoes marcadas como entradas/saidas.
     *
     * @return Uma lista de nomes das divisoes de entrada/saida.
     */
    ArrayUnorderedList<String> getEntradasSaidasNomes();

    ArrayUnorderedList<IDivisao> getEntradasSaidas();

    /**
     * Move os inimigos no mapa, potencialmente interagindo com To Cruz.
     *
     * @param toCruz         O agente To Cruz.
     * @param combateService O servico de combate a ser utilizado.
     * @throws ElementNotFoundException Se um inimigo nao for encontrado durante o
     *                                  processo.
     */
    void moverInimigos(ToCruz toCruz, ICombateService combateService) throws ElementNotFoundException;

    /**
     * Mostra uma representacao textual do mapa.
     */
    void mostrarMapa();

    IDivisao getPrimeiroVertice();

    ArrayUnorderedList<IDivisao> expandirConexoes(IDivisao divisaoAtual);

    /**
     * Calcula o melhor caminho entre duas divisoes.
     *
     * @param origem  A divisao de origem.
     * @param destino A divisao de destino.
     * @return Uma lista de divisoes representando o melhor caminho.
     * @throws ElementNotFoundException Se a origem ou o destino nao forem
     *                                  encontrados.
     */
    ArrayUnorderedList<IDivisao> calcularMelhorCaminho(IDivisao origem, IDivisao destino);

    IDivisao encontrarKitMaisProximo(IDivisao origem) throws ElementNotFoundException;

    /**
     * Obtem todos os itens de um tipo especifico no mapa.
     *
     * @param tipo O tipo de item a ser buscado.
     * @return Uma lista de itens do tipo especificado.
     */
    ArrayUnorderedList<IItem> getItensPorTipo(String tipo);

    ArrayUnorderedList<IItem> getItens();
}
