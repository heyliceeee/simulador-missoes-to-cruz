package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Representa a interface para o mapa do sistema.
 * 
 * <p>
 * O mapa é estruturado como um grafo que contém divisões, conexões entre elas,
 * inimigos, itens e alvos. Essa interface define as operações relacionadas à
 * manipulação e consulta do mapa.
 * </p>
 */
public interface Mapa {

    /**
     * Adiciona uma nova divisão ao mapa.
     *
     * @param nomeDivisao O nome da divisão a ser adicionada.
     * @throws IllegalArgumentException Se o nome da divisão for nulo ou vazio.
     */
    void adicionarDivisao(String nomeDivisao);

    /**
     * Adiciona uma ligação entre duas divisões no mapa.
     *
     * @param nomeDivisao1 O nome da primeira divisão.
     * @param nomeDivisao2 O nome da segunda divisão.
     * @throws IllegalArgumentException Se qualquer nome de divisão for nulo ou
     *                                  vazio.
     */
    void adicionarLigacao(String nomeDivisao1, String nomeDivisao2);

    /**
     * Adiciona um inimigo a uma divisão específica.
     *
     * @param nomeDivisao O nome da divisão.
     * @param inimigo     O inimigo a ser adicionado.
     * @throws IllegalArgumentException Se o nome da divisão ou o inimigo for nulo.
     */
    void adicionarInimigo(String nomeDivisao, Inimigo inimigo);

    /**
     * Adiciona um item a uma divisão específica.
     *
     * @param nomeDivisao O nome da divisão.
     * @param item        O item a ser adicionado.
     * @throws IllegalArgumentException Se o nome da divisão ou o item for nulo.
     */
    void adicionarItem(String nomeDivisao, Item item);

    /**
     * Marca uma divisão como entrada/saída.
     *
     * @param nomeDivisao O nome da divisão a ser marcada.
     * @throws IllegalArgumentException Se o nome da divisão for nulo ou vazio.
     */
    void adicionarEntradaSaida(String nomeDivisao);

    /**
     * Retorna uma divisão pelo nome.
     *
     * @param nomeDivisao O nome da divisão a ser buscada.
     * @return A divisão correspondente.
     * @throws IllegalArgumentException Se o nome da divisão for nulo ou vazio.
     * @throws RuntimeException         Se a divisão não for encontrada.
     */
    Divisao getDivisaoPorNome(String nomeDivisao);

    /**
     * Verifica se é possível mover de uma divisão para outra.
     *
     * @param divisao1 O nome da primeira divisão.
     * @param divisao2 O nome da segunda divisão.
     * @return {@code true} se o movimento for possível; {@code false} caso
     *         contrário.
     */
    boolean podeMover(String divisao1, String divisao2);

    /**
     * Define um alvo em uma divisão específica.
     *
     * @param nomeDivisao O nome da divisão.
     * @param tipo        O tipo do alvo.
     * @throws IllegalArgumentException Se o nome da divisão ou o tipo do alvo for
     *                                  nulo ou vazio.
     */
    void definirAlvo(String nomeDivisao, String tipo);

    /**
     * Remove o alvo do mapa.
     */
    void removerAlvo();

    /**
     * Obtém o alvo atual do mapa.
     *
     * @return O alvo atual, ou {@code null} se nenhum alvo estiver definido.
     */
    Alvo getAlvo();

    /**
     * Obtém as conexões de uma divisão.
     *
     * @param divisao A divisão cujas conexões devem ser buscadas.
     * @return Uma lista de divisões conectadas.
     */
    ArrayUnorderedList<Divisao> obterConexoes(Divisao divisao);

    /**
     * Obtém todas as divisões do mapa.
     *
     * @return Uma lista de todas as divisões.
     */
    ArrayUnorderedList<Divisao> getDivisoes();

    /**
     * Obtém os nomes das divisões marcadas como entradas/saídas.
     *
     * @return Uma lista de nomes das divisões de entrada/saída.
     */
    ArrayUnorderedList<String> getEntradasSaidasNomes();

    /**
     * Move os inimigos no mapa, potencialmente interagindo com Tó Cruz.
     *
     * @param toCruz         O agente Tó Cruz.
     * @param combateService O serviço de combate a ser utilizado.
     * @throws ElementNotFoundException Se um inimigo não for encontrado durante o
     *                                  processo.
     */
    void moverInimigos(ToCruz toCruz, CombateService combateService) throws ElementNotFoundException;

    /**
     * Calcula o melhor caminho entre duas divisões.
     *
     * @param origem  A divisão de origem.
     * @param destino A divisão de destino.
     * @return Uma lista de divisões representando o melhor caminho.
     * @throws ElementNotFoundException Se a origem ou o destino não forem
     *                                  encontrados.
     */
    ArrayUnorderedList<Divisao> calcularMelhorCaminho(Divisao origem, Divisao destino) throws ElementNotFoundException;

    /**
     * Obtém todos os itens de um tipo específico no mapa.
     *
     * @param tipo O tipo de item a ser buscado.
     * @return Uma lista de itens do tipo especificado.
     */
    ArrayUnorderedList<Item> getItensPorTipo(String tipo);

    /**
     * Mostra uma representação textual do mapa.
     */
    void mostrarMapa();
}
