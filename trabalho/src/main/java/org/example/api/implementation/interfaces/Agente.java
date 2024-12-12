package org.example.api.implementation.interfaces;

/**
 * Interface que define as operações básicas para um agente no sistema.
 *
 * <p>
 * Um agente representa uma entidade, como o personagem principal, que interage
 * com o mapa,
 * coleta itens, enfrenta inimigos e realiza ações como mover-se entre divisões
 * e usar kits de vida.
 * </p>
 */
public interface Agente {

    /**
     * Obtém o nome do agente.
     *
     * @return O nome do agente.
     */
    String getNome();

    /**
     * Define o nome do agente.
     *
     * @param nome O novo nome do agente.
     * @throws IllegalArgumentException se o nome for nulo ou vazio.
     */
    void setNome(String nome);

    /**
     * Obtém a vida atual do agente.
     *
     * @return A vida atual do agente.
     */
    int getVida();

    /**
     * Define a vida do agente.
     *
     * @param vida A nova quantidade de vida do agente.
     * @throws IllegalArgumentException se a vida for negativa.
     */
    void setVida(int vida);

    /**
     * Obtém a posição atual do agente.
     *
     * @return A divisão em que o agente está atualmente.
     */
    Divisao getPosicaoAtual();

    /**
     * Define a posição atual do agente.
     *
     * @param divisao A nova divisão onde o agente estará.
     * @throws IllegalArgumentException se a divisão for nula.
     */
    void setPosicaoAtual(Divisao divisao);

    /**
     * Move o agente para uma nova divisão.
     *
     * @param novaDivisao A divisão para a qual o agente deve ser movido.
     * @throws IllegalArgumentException se a nova divisão for nula.
     */
    void moverPara(Divisao novaDivisao);

    /**
     * Aplica dano ao agente, reduzindo sua vida.
     *
     * @param dano A quantidade de dano sofrido.
     * @throws IllegalArgumentException se o dano for negativo.
     */
    void sofrerDano(int dano);

    /**
     * Adiciona um item ao inventário do agente.
     *
     * @param item O item a ser adicionado ao inventário.
     * @throws IllegalArgumentException se o item for nulo.
     */
    void adicionarAoInventario(Item item);

    /**
     * Usa um kit de vida do inventário para recuperar a vida do agente.
     *
     * <p>
     * Caso o inventário esteja vazio ou não contenha kits de vida, o método não
     * altera a vida.
     * </p>
     *
     * @throws IllegalStateException se não houver kits de vida no inventário.
     */
    void usarKitDeVida();
}
