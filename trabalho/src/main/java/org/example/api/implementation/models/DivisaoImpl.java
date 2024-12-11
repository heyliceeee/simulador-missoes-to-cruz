package org.example.api.implementation.models;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IInimigo;
import org.example.api.implementation.interfaces.IItem;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.LinkedList;

/**
 * Representa uma divisao do edificio.
 */
public class DivisaoImpl implements IDivisao {
    private String nomeDivisao;
    private ArrayUnorderedList<IInimigo> inimigosPresentes;
    private ArrayUnorderedList<IItem> itensPresentes;
    private boolean entradaSaida;

    /**
     * Construtor da Divisao.
     *
     * @param nomeDivisao Nome da divisao.
     */
    public DivisaoImpl(String nomeDivisao) {
        this.nomeDivisao = nomeDivisao;
        this.inimigosPresentes = new ArrayUnorderedList<>();
        this.itensPresentes = new ArrayUnorderedList<>();
    }

    /**
     * Obtem o nome da divisao.
     *
     * @return Nome da divisao.
     */
    @Override
    public String getNomeDivisao() {
        return nomeDivisao;
    }

    /**
     * Define o nome da divisao.
     *
     * @param nomeDivisao Nome da divisao a ser definida.
     */
    public void setNomeDivisao(String nomeDivisao) {
        this.nomeDivisao = nomeDivisao;
    }

    /**
     * Adiciona um inimigo à divisao.
     *
     * @param inimigo Inimigo a ser adicionado.
     */
    @Override
    public void adicionarInimigo(IInimigo inimigo) {
        inimigosPresentes.addToRear(inimigo);
    }

    /**
     * Remove um inimigo da divisao.
     *
     * @param inimigo Inimigo a ser removido.
     */
    @Override
    public void removerInimigo(IInimigo inimigo) throws ElementNotFoundException {
        inimigosPresentes.remove(inimigo);
    }

    /**
     * Adiciona um item à divisao.
     *
     * @param item Item a ser adicionado.
     */
    @Override
    public void adicionarItem(IItem item) {
        itensPresentes.addToRear(item);
    }

    /**
     * Remove um item da divisao.
     *
     * @param item Item a ser removido.
     */
    @Override
    public void removerItem(IItem item) throws ElementNotFoundException {
        itensPresentes.remove(item);
    }

    /**
     * Retorna true caso a divisao seja uma entrada ou saida, caso contrario retorna
     * false
     * 
     * @return true caso a divisao seja uma entrada ou saida
     */
    @Override
    public boolean isEntradaSaida() {
        return entradaSaida;
    }

    /**
     * Define a Divisao como do tipo entrada ou saida
     * 
     * @param entradaSaida
     */
    @Override
    public void setEntradaSaida(boolean entradaSaida) {
        this.entradaSaida = entradaSaida;
    }

    /**
     * Obtem os inimigos presentes na divisao.
     *
     * @return Lista de inimigos.
     */
    @Override
    public ArrayUnorderedList<IInimigo> getInimigosPresentes() {
        return inimigosPresentes;
    }

    /**
     * Define os inimigos presentes na divisao.
     *
     * @param inimigosPresentes Lista de inimigos.
     */
    @Override
    public void setInimigosPresentes(ArrayUnorderedList<IInimigo> inimigosPresentes) {
        this.inimigosPresentes = inimigosPresentes;
    }

    /**
     * Obtem os itens presentes na divisao.
     *
     * @return Lista de itens.
     */
    @Override
    public ArrayUnorderedList<IItem> getItensPresentes() {
        return itensPresentes;
    }

    /**
     * Define os itens presentes na divisao.
     *
     * @param itensPresentes Lista de itens.
     */
    @Override
    public void setItensPresentes(ArrayUnorderedList<IItem> itensPresentes) {
        this.itensPresentes = itensPresentes;
    }

    /**
     * Sobrescreve o metodo equals para comparar divisões com base no nome.
     *
     * @param obj Objeto a ser comparado.
     * @return true se os nomes forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        IDivisao divisao = (IDivisao) obj;
        return nomeDivisao.equals(divisao.getNomeDivisao());
    }

    /**
     * Sobrescreve o metodo hashCode para gerar hash baseado no nome da divisao.
     *
     * @return Hash code.
     */
    @Override
    public int hashCode() {
        return nomeDivisao.hashCode();
    }

    /**
     * Sobrescreve o metodo toString para representar a divisao pelo seu nome.
     *
     * @return Nome da divisao.
     */
    @Override
    public String toString() {
        return nomeDivisao;
    }

    /**
     * Verifica se ha um kit na divisao.
     * Um kit pode ser do tipo "kit de vida" ou "colete".
     *
     * @return true se há um kit, false caso contrario.
     */
    @Override
    public boolean temKit() {
        for (int i = 0; i < itensPresentes.size(); i++) {
            IItem item = itensPresentes.getElementAt(i);
            if (item.getTipo().equalsIgnoreCase("kit de vida") ||
                    item.getTipo().equalsIgnoreCase("colete")) {
                return true;
            }
        }
        return false;
    }
}
