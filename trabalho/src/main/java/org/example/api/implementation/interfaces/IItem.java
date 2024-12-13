package org.example.api.implementation.interfaces;

/**
 * Representa a interface para um item no sistema.
 * 
 * <p>
 * Um item pode ter um tipo, pontos associados, e estar vinculado a uma divisão
 * específica no mapa.
 * </p>
 */
public interface IItem {

    /**
     * Obtém o tipo do item.
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
     * Obtém os pontos associados ao item.
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
     * Obtém a divisão à qual o item está associado.
     *
     * @return A divisão do item.
     */
    IDivisao getDivisao();

    /**
     * Define a divisão à qual o item está associado.
     *
     * @param divisao A nova divisão do item.
     * @throws IllegalArgumentException Se a divisão fornecida for nula.
     */
    void setDivisao(IDivisao divisao);
}
