package org.example.collections.interfaces;

import org.example.api.exceptions.ElementNotFoundException;

/**
 * A interface UnorderedListADT estende a {@link ListADT} e representa uma lista
 * na qual a ordem dos elementos não é necessariamente determinada por critérios
 * de ordenação, permitindo a inserção de elementos em qualquer posição.
 * 
 * Além das operações definidas em {@link ListADT}, esta interface fornece métodos
 * adicionais para inserir elementos na frente, na traseira da lista, bem como
 * após um elemento específico (target). Também oferece métodos para atualizar um 
 * elemento em um índice específico e obter o índice de um elemento.
 * 
 * @param <T> o tipo dos elementos armazenados na lista
 */
public interface UnorderedListADT<T> extends ListADT<T> {

    /**
     * Adiciona o elemento especificado à frente (início) da lista.
     *
     * @param element o elemento a ser adicionado na frente da lista.
     *                Não deve ser nulo. Caso seja nulo, o comportamento depende da implementação.
     */
    public void addToFront(T element);

    /**
     * Adiciona o elemento especificado à traseira (final) da lista.
     *
     * @param element o elemento a ser adicionado no final da lista.
     *                Não deve ser nulo. Caso seja nulo, o comportamento depende da implementação.
     */
    public void addToRear(T element);

    /**
     * Adiciona o elemento especificado após um elemento-alvo (target) já existente na lista.
     * Se o target aparecer múltiplas vezes, a implementação pode decidir após qual ocorrência inserir.
     * Caso o target não seja encontrado, é lançada uma exceção.
     *
     * @param element o elemento a ser adicionado após o target.
     * @param target  o elemento já presente na lista, após o qual o novo elemento será inserido.
     * @throws ElementNotFoundException se o target não for encontrado na lista.
     */
    public void addAfter(T element, T target) throws ElementNotFoundException;

    /**
     * Substitui o elemento na posição especificada pelo novo elemento fornecido.
     * A posição é baseada em índice zero (0-based index).
     * Caso o índice seja inválido (fora do intervalo da lista), 
     * o comportamento depende da implementação (geralmente lança exceção).
     *
     * @param index   o índice do elemento a ser substituído.
     * @param element o novo elemento que substituirá o elemento atual na posição especificada.
     *                Não deve ser nulo. Caso seja nulo, o comportamento depende da implementação.
     * @throws IndexOutOfBoundsException se o índice for inválido.
     */
    public void setElementAt(int index, T element);

    /**
     * Retorna o índice da primeira ocorrência do elemento especificado na lista.
     * Caso o elemento não seja encontrado, o método pode retornar um valor específico 
     * (geralmente -1) para indicar sua ausência.
     *
     * @param element o elemento a ser procurado na lista.
     * @return o índice (0-based) da primeira ocorrência do elemento, 
     *         ou -1 se o elemento não estiver presente.
     */
    public int indexOf(T element);
}
