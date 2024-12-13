package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IItem;

/**
 * Implementação de um item no mapa.
 * <p>
 * Representa um item associado a uma divisão no mapa. Cada item possui um tipo,
 * um valor de pontos e pode estar vinculado a uma divisão específica.
 * </p>
 *
 * @see IItem
 * @see IDivisao
 */
public class ItemImpl implements IItem {

    private String tipo;
    private int pontos;
    private IDivisao divisao;

    /**
     * Construtor do item.
     *
     * @param tipo   O tipo do item. Não pode ser nulo ou vazio.
     * @param pontos Os pontos associados ao item. Deve ser um valor não negativo.
     * @throws IllegalArgumentException se o tipo for nulo ou vazio, ou se os pontos
     *                                  forem negativos.
     */
    public ItemImpl(String tipo, int pontos) {
        validarTipo(tipo);
        validarPontos(pontos);
        this.tipo = tipo.trim();
        this.pontos = pontos;
    }

    /**
     * Retorna o tipo do item.
     *
     * @return o tipo do item.
     */
    @Override
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo do item.
     *
     * @param tipo O tipo a ser definido. Não pode ser nulo ou vazio.
     * @throws IllegalArgumentException se o tipo for nulo ou vazio.
     */
    @Override
    public void setTipo(String tipo) {
        validarTipo(tipo);
        this.tipo = tipo.trim();
    }

    /**
     * Retorna os pontos associados ao item.
     *
     * @return os pontos do item.
     */
    @Override
    public int getPontos() {
        return pontos;
    }

    /**
     * Define os pontos associados ao item.
     *
     * @param pontos Os pontos a serem definidos. Deve ser um valor não negativo.
     * @throws IllegalArgumentException se os pontos forem negativos.
     */
    @Override
    public void setPontos(int pontos) {
        validarPontos(pontos);
        this.pontos = pontos;
    }

    /**
     * Retorna a divisão associada ao item.
     *
     * @return a divisão associada, ou {@code null} se nenhuma estiver definida.
     */
    @Override
    public IDivisao getDivisao() {
        return divisao;
    }

    /**
     * Define a divisão associada ao item.
     *
     * @param divisao A divisão a ser associada.
     */
    @Override
    public void setDivisao(IDivisao divisao) {
        this.divisao = divisao;
    }

    /**
     * Valida o tipo do item.
     *
     * @param tipo O tipo a ser validado.
     * @throws IllegalArgumentException se o tipo for nulo ou vazio.
     */
    private void validarTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo do item não pode ser nulo ou vazio.");
        }
    }

    /**
     * Valida os pontos do item.
     *
     * @param pontos Os pontos a serem validados.
     * @throws IllegalArgumentException se os pontos forem negativos.
     */
    private void validarPontos(int pontos) {
        if (pontos < 0) {
            throw new IllegalArgumentException("Os pontos do item não podem ser negativos.");
        }
    }

    /**
     * Retorna a representação em string do item.
     *
     * @return a representação do item no formato {@code ItemImpl{tipo='tipo', pontos=valor, divisao=nomeDivisao}}.
     *         Se a divisão não estiver definida, será exibido {@code "N/A"} no campo divisão.
     */
    @Override
    public String toString() {
        return String.format("ItemImpl{tipo='%s', pontos=%d, divisao=%s}",
                tipo, pontos, divisao != null ? divisao.getNomeDivisao() : "N/A");
    }

    /**
     * Compara este item com outro objeto.
     * <p>
     * Dois itens são considerados iguais se tiverem o mesmo tipo (ignorando maiúsculas e minúsculas).
     * </p>
     *
     * @param o O objeto a ser comparado.
     * @return {@code true} se os objetos forem iguais, {@code false} caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IItem item = (IItem) o;

        return tipo.equalsIgnoreCase(item.getTipo());
    }

    /**
     * Retorna o código hash do item com base no tipo.
     *
     * @return o código hash do item.
     */
    @Override
    public int hashCode() {
        return tipo != null ? tipo.toLowerCase().hashCode() : 0;
    }
}
