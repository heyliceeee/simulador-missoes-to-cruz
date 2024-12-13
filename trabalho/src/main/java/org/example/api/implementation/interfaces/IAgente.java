package org.example.api.implementation.interfaces;

/**
 * Interface que define as operacoes basicas para um agente no sistema.
 *
 * <p>
 * Um agente representa uma entidade, como o personagem principal, que interage
 * com o mapa,
 * coleta itens, enfrenta inimigos e realiza acoes como mover-se entre divisoes
 * e usar kits de vida.
 * </p>
 */
public interface IAgente {

    /**
     * Obtem o nome do agente.
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
     * Obtem a vida atual do agente.
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
     * Obtem a posicao atual do agente.
     *
     * @return A divisao em que o agente esta atualmente.
     */
    IDivisao getPosicaoAtual();

    /**
     * Define a posicao atual do agente.
     *
     * @param divisao A nova divisao onde o agente estara.
     * @throws IllegalArgumentException se a divisao for nula.
     */
    void setPosicaoAtual(IDivisao divisao);

    /**
     * Move o agente para uma nova divisao.
     *
     * @param novaDivisao A divisao para a qual o agente deve ser movido.
     * @throws IllegalArgumentException se a nova divisao for nula.
     */
    void moverPara(IDivisao novaDivisao);

    /**
     * Aplica dano ao agente, reduzindo sua vida.
     *
     * @param dano A quantidade de dano sofrido.
     * @throws IllegalArgumentException se o dano for negativo.
     */
    void sofrerDano(int dano);

    /**
     * Adiciona um item ao inventario do agente.
     *
     * @param item O item a ser adicionado ao inventario.
     * @throws IllegalArgumentException se o item for nulo.
     */
    void adicionarAoInventario(IItem item);

    /**
     * Usa um kit de vida do inventario para recuperar a vida do agente.
     *
     * <p>
     * Caso o inventario esteja vazio ou nao contenha kits de vida, o metodo nao
     * altera a vida.
     * </p>
     *
     * @throws IllegalStateException se nao houver kits de vida no inventario.
     */
    void usarKitDeVida();
}
