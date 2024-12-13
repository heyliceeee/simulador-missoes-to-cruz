package org.example.collections.interfaces;

import java.util.Iterator;

/**
 * A interface GraphADT define as operações gerais para um Grafo (Graph) abstrato.
 * Um grafo é uma estrutura de dados composta por vértices (ou nós) e arestas (conexões entre vértices).
 * Esta interface fornece métodos para adicionar e remover vértices, adicionar e remover arestas,
 * e percorrer o grafo de diversas maneiras (busca em largura, busca em profundidade), assim como
 * verificar propriedades do grafo, como conectividade e tamanho.
 *
 * <p><b>Operações principais:</b></p>
 * <ul>
 *   <li>Adicionar e remover vértices.</li>
 *   <li>Adicionar e remover arestas entre vértices.</li>
 *   <li>Obter percursos no grafo (BFS, DFS e caminho mais curto).</li>
 *   <li>Verificar se o grafo está vazio, se é conectado, além de obter seu tamanho.</li>
 *   <li>Visualizar a matriz de adjacência representando as conexões.</li>
 * </ul>
 *
 * @param <T> o tipo de elementos (vértices) armazenados no grafo.
 */
public interface GraphADT<T> {

    /**
     * Adiciona um vértice ao grafo, associando o objeto fornecido a um novo vértice.
     * Caso o vértice já exista, o comportamento depende da implementação (pode ignorar ou atualizar).
     *
     * @param vertex o vértice a ser adicionado ao grafo.
     * @return true se o vértice foi adicionado com sucesso, false caso contrário.
     */
    boolean addVertex(T vertex);

    /**
     * Remove um vértice específico do grafo.
     * Todas as arestas incidentes nesse vértice também devem ser removidas.
     *
     * @param vertex o vértice a ser removido.
     * @return true se o vértice foi removido com sucesso, false caso o vértice não exista ou não possa ser removido.
     */
    boolean removeVertex(T vertex);

    /**
     * Adiciona uma aresta entre dois vértices do grafo.
     * Caso já exista uma aresta entre esses vértices, o comportamento é definido pela implementação
     * (pode ignorar ou atualizar a aresta).
     *
     * @param vertex1 o primeiro vértice.
     * @param vertex2 o segundo vértice.
     * @throws IllegalArgumentException se um ou ambos os vértices não existirem no grafo.
     */
    void addEdge(T vertex1, T vertex2);

    /**
     * Remove uma aresta entre dois vértices do grafo.
     * Caso a aresta não exista, a implementação pode apenas ignorar a operação.
     *
     * @param vertex1 o primeiro vértice.
     * @param vertex2 o segundo vértice.
     * @throws IllegalArgumentException se um ou ambos os vértices não existirem no grafo.
     */
    void removeEdge(T vertex1, T vertex2);

    /**
     * Retorna um iterador que percorre o grafo em largura (BFS - Breadth-First Search),
     * começando a partir de um vértice específico.
     * O iterador fornecerá os vértices na ordem em que forem visitados pela busca em largura.
     *
     * @param startVertex o vértice inicial da busca.
     * @return um iterador que percorre o grafo em ordem de largura.
     * @throws IllegalArgumentException se o vértice inicial não existir no grafo.
     */
    Iterator<T> iteratorBFS(T startVertex);

    /**
     * Retorna um iterador que percorre o grafo em profundidade (DFS - Depth-First Search),
     * começando a partir de um vértice específico.
     * O iterador fornecerá os vértices na ordem em que forem visitados pela busca em profundidade.
     *
     * @param startVertex o vértice inicial da busca.
     * @return um iterador que percorre o grafo em ordem de profundidade.
     * @throws IllegalArgumentException se o vértice inicial não existir no grafo.
     */
    Iterator<T> iteratorDFS(T startVertex);

    /**
     * Retorna um iterador contendo o caminho mais curto entre dois vértices, caso exista.
     * Esse iterador apresenta a sequência de vértices que formam o menor caminho do vértice inicial
     * (startVertex) até o vértice alvo (targetVertex).
     *
     * @param startVertex  o vértice inicial.
     * @param targetVertex o vértice final (alvo).
     * @return um iterador com o caminho mais curto entre os dois vértices.
     *         Caso não haja caminho, o iterador pode estar vazio.
     * @throws IllegalArgumentException se um dos vértices (inicial ou alvo) não existir no grafo.
     */
    Iterator<T> iteratorShortestPath(T startVertex, T targetVertex);

    /**
     * Verifica se o grafo está vazio (ou seja, não contém nenhum vértice).
     *
     * @return true se o grafo não contiver vértices, false caso contrário.
     */
    boolean isEmpty();

    /**
     * Verifica se o grafo é conectado. Um grafo é considerado conectado se há um caminho
     * entre qualquer par de vértices.
     *
     * @return true se o grafo é conectado, false caso contrário.
     */
    boolean isConnected();

    /**
     * Retorna o número de vértices no grafo.
     *
     * @return a quantidade de vértices atualmente no grafo.
     */
    int size();

    /**
     * Retorna uma representação em string da matriz de adjacência do grafo,
     * ou outra representação textual da estrutura interna do grafo,
     * dependendo da implementação.
     *
     * @return uma string representando a matriz de adjacência ou a estrutura interna do grafo.
     */
    String toString();
}
