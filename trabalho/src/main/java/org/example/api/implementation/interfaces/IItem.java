package org.example.api.implementation.interfaces;

/**
 * Representa a interface para um item no sistema.
 * 
 * <p>
 * Um item pode ter um tipo, pontos associados, e estar vinculado a uma divisao
 * especifica no mapa.
 * </p>
 */
public interface IItem {

    /**
     * Obtem o tipo do item.
     *
     * @return O tipo do item.
     */
    String getTipo();

    /**
     * Define o tipo do item.
     *
     * @param tipo O novo tipo do item.
     * @throws IllegalArgumentException Se o tipo fornecido for nulo ou vazio.
     */
    void setTipo(String tipo);

    /**
     * Obtem os pontos associados ao item.
     *
     * @return Os pontos do item.
     */
    int getPontos();

    /**
     * Define os pontos associados ao item.
     *
     * @param pontos Os novos pontos do item.
     * @throws IllegalArgumentException Se os pontos fornecidos forem negativos.
     */
    void setPontos(int pontos);

    /**
     * Obtem a divisao a qual o item esta associado.
     *
     * @return A divisao do item.
     */
    IDivisao getDivisao();

    /**
     * Define a divisao a qual o item esta associado.
     *
     * @param divisao A nova divisao do item.
     * @throws IllegalArgumentException Se a divisao fornecida for nula.
     */
    void setDivisao(IDivisao divisao);
}
