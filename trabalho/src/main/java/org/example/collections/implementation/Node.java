package org.example.collections.implementation;

/**
 * Representa um nó genérico em uma estrutura de dados encadeada.
 * <p>
 * Esta classe encapsula um dado do tipo genérico {@code T} e um ponteiro para o próximo nó na estrutura.
 * Pode ser usada em listas encadeadas, pilhas e filas.
 * </p>
 *
 * @param <T> O tipo de dado armazenado no nó.
 */
public class Node<T> {

    private T data;
    private Node<T> next;

    /**
     * Construtor que cria um nó com o dado especificado.
     * <p>
     * O próximo nó é inicializado como {@code null}.
     * </p>
     *
     * @param data O dado a ser armazenado neste nó.
     */
    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    /**
     * Define o dado armazenado neste nó.
     *
     * @param data O dado a ser armazenado.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Obtém o dado armazenado neste nó.
     *
     * @return O dado armazenado.
     */
    public T getData() {
        return this.data;
    }

    /**
     * Obtém o próximo nó na estrutura encadeada.
     *
     * @return O próximo nó, ou {@code null} se este for o último nó.
     */
    public Node<T> getNext() {
        return this.next;
    }

    /**
     * Define o próximo nó na estrutura encadeada.
     *
     * @param next O próximo nó a ser definido.
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }

    /**
     * Verifica se este nó é igual a outro objeto.
     * <p>
     * Dois nós são considerados iguais se o dado armazenado neles for igual, conforme o método {@code equals}.
     * </p>
     *
     * @param obj O objeto a ser comparado.
     * @return {@code true} se os nós forem iguais, {@code false} caso contrário.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Node<?> other = (Node<?>) obj;
        return this.data.equals(other.data);
    }
}
