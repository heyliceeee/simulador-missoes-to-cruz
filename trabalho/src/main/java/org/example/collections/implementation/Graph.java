package org.example.collections.implementation;

import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.interfaces.GraphADT;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementação de um grafo utilizando uma matriz de adjacência.
 *
 * @param <T> O tipo de elementos armazenados nos vértices do grafo.
 */
public class Graph<T> implements GraphADT<T>, Iterable<T> {
    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices;
    protected double[][] adjMatrix;
    protected T[] vertices;

    /**
     * Construtor padrão que inicializa o grafo com capacidade padrão.
     */
    public Graph() {
        this.numVertices = 0;
        this.adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Adiciona um vértice ao grafo.
     *
     * @param vertex O vértice a ser adicionado.
     * @return true se o vértice foi adicionado com sucesso.
     * @throws IllegalArgumentException se o vértice for nulo.
     */
    @Override
    public boolean addVertex(T vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("vertice nulo");
        }

        if (numVertices == vertices.length) {
            expandCapacity();
        }

        vertices[numVertices] = vertex;

        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = 0;
            adjMatrix[i][numVertices] = 0;
        }

        numVertices++;

        return true;
    }

    /**
     * Retorna um iterador para os vértices do grafo.
     *
     * @return Iterador dos vértices.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex = 0;
            private final int size = numVertices;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return vertices[currentIndex++];
            }
        };
    }

    /**
     * Expande a capacidade do grafo, dobrando o tamanho atual.
     */
    private void expandCapacity() {
        T[] verticesTmp = (T[]) (new Object[this.vertices.length * 2]);
        double[][] adjMatrixTmp = new double[this.vertices.length * 2][this.vertices.length * 2];

        for (int i = 0; i < this.numVertices; i++) {
            for (int j = 0; j < this.numVertices; j++) {
                adjMatrixTmp[i][j] = this.adjMatrix[i][j];
            }

            verticesTmp[i] = this.vertices[i];
        }
        this.vertices = verticesTmp;
        this.adjMatrix = adjMatrixTmp;
    }

    /**
     * Remove um vértice do grafo.
     *
     * @param vertex O vértice a ser removido.
     * @return true se o vértice foi removido com sucesso.
     * @throws NoSuchElementException se o vértice não for encontrado.
     */
    @Override
    public boolean removeVertex(T vertex) {
        if (vertex == null) {
            throw new NoSuchElementException("vertice nulo");
        }

        for (int i = 0; i < numVertices; i++) {
            if (vertex.equals(vertices[i])) {
                removeVertex(i);
            }
        }

        return true;
    }

    /**
     * Remove um vértice do grafo pelo índice.
     *
     * @param index O índice do vértice a ser removido.
     * @throws IllegalArgumentException se o índice for inválido.
     */
    public void removeVertex(int index) {
        if (indexIsValid(index)) {
            numVertices--;

            for (int i = index; i < numVertices; i++) {
                vertices[i] = vertices[i + 1];
            }

            for (int i = index; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    adjMatrix[i][j] = adjMatrix[i + 1][j];
                }
            }

            for (int i = 0; i < numVertices; i++) {
                for (int j = index; j < numVertices; j++) {
                    adjMatrix[i][j] = adjMatrix[i][j + 1];
                }
            }

            vertices[numVertices] = null;
            for (int i = 0; i < numVertices; i++) {
                adjMatrix[numVertices][i] = 0;
                adjMatrix[i][numVertices] = 0;
            }
        } else {
            throw new IllegalArgumentException("Índice do vértice inválido");
        }
    }

    /**
     * Adiciona uma aresta entre dois vértices.
     *
     * @param vertex1 O primeiro vértice.
     * @param vertex2 O segundo vértice.
     * @throws IllegalArgumentException se algum dos vértices for inválido.
     */
    @Override
    public void addEdge(T vertex1, T vertex2) {
        this.addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    public int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equals(vertex)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Adiciona uma aresta entre dois índices de vértices.
     *
     * @param index1 Índice do primeiro vértice.
     * @param index2 Índice do segundo vértice.
     * @throws IllegalArgumentException se algum dos índices for inválido.
     */
    public void addEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = 1;
            adjMatrix[index2][index1] = 1;
        } else {
            throw new IllegalArgumentException("Pelo menos um dos vértices não é válido");
        }
    }

    /**
     * Verifica se o índice de um vértice é válido.
     *
     * @param index O índice a ser verificado.
     * @return true se o índice for válido, false caso contrário.
     */
    private boolean indexIsValid(int index) {
        return ((index < numVertices) && index >= 0);
    }

    /**
     * Remove uma aresta entre dois vértices.
     *
     * @param vertex1 O primeiro vértice.
     * @param vertex2 O segundo vértice.
     * @throws IllegalArgumentException se a aresta não existir ou os vértices forem
     *                                  inválidos.
     */
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        this.removeEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Remove uma aresta entre dois índices de vértices.
     *
     * @param index1 Índice do primeiro vértice.
     * @param index2 Índice do segundo vértice.
     * @throws IllegalArgumentException se algum dos índices for inválido ou a
     *                                  aresta não existir.
     */
    public void removeEdge(int index1, int index2) {
        if (!indexIsValid(index1)) {
            throw new IllegalArgumentException("O primeiro vertice nao é valido");
        }

        if (!indexIsValid(index2)) {
            throw new IllegalArgumentException("O segundo vertice nao é valido");
        }

        if (!edgeExists(index1, index2)) {
            throw new IllegalArgumentException("Aresta entre os vertices nao existe");
        }

        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = 0;
            adjMatrix[index2][index1] = 0;
        }
    }

    /**
     * Verifica se uma aresta existe entre dois índices de vértices.
     *
     * @param index1 Índice do primeiro vértice.
     * @param index2 Índice do segundo vértice.
     * @return true se a aresta existir, false caso contrário.
     */
    private boolean edgeExists(int index1, int index2) {
        return adjMatrix[index1][index2] != 0 || adjMatrix[index2][index1] != 0;
    }

    /**
     * Retorna um iterador para a travessia em largura (BFS) a partir de um vértice
     * inicial.
     *
     * @param startVertex O vértice inicial.
     * @return Iterador para a travessia em largura.
     * @throws IllegalArgumentException se o vértice não for encontrado.
     */
    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        int startIndex = getIndex(startVertex);

        if (startIndex == -1) {
            throw new IllegalArgumentException("Vértice não encontrado");
        }

        return iteratorBFS(startIndex);
    }

    public Iterator<T> iteratorBFS(int startIndex) {
        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();

        if (!indexIsValid(startIndex)) {
            return resultList.iterator();
        }

        boolean[] visited = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty()) {
            try {
                x = traversalQueue.dequeue();
                resultList.addToRear(vertices[x]);

                for (int i = 0; i < numVertices; i++) {
                    if (adjMatrix[x][i] != 0 && !visited[i]) {
                        traversalQueue.enqueue(i);
                        visited[i] = true;
                    }
                }
            } catch (EmptyCollectionException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return resultList.iterator();
    }

    /**
     * Retorna um iterador para a travessia em profundidade (DFS) a partir de um
     * vértice inicial.
     *
     * @param startVertex O vértice inicial.
     * @return Iterador para a travessia em profundidade.
     * @throws IllegalArgumentException se o vértice não for encontrado.
     */
    @Override
    public Iterator<T> iteratorDFS(T startVertex) {
        int startIndex = getIndex(startVertex);
        if (startIndex == -1) {
            throw new IllegalArgumentException("Vértice não encontrado");
        }

        return iteratorDFS(startIndex);
    }

    public Iterator<T> iteratorDFS(int startIndex) {
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();

        boolean[] visited = new boolean[numVertices];

        if (!indexIsValid(startIndex)) {
            return resultList.iterator();
        }

        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        traversalStack.push(startIndex);
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;

        while (!traversalStack.isEmpty()) {
            x = traversalStack.peek();
            found = false;

            for (int i = 0; (i < numVertices) && !found; i++) {
                if (adjMatrix[x][i] != 0 && !visited[i]) {
                    traversalStack.push(i);
                    resultList.addToRear(vertices[i]);
                    visited[i] = true;
                    found = true;
                }
            }
            if (!found && !traversalStack.isEmpty()) {
                traversalStack.pop();
            }
        }

        return resultList.iterator();
    }

    /**
     * Retorna um iterador para o caminho mais curto entre dois vértices.
     *
     * @param startVertex  O vértice de origem.
     * @param targetVertex O vértice de destino.
     * @return Iterador para o caminho mais curto.
     */
    @Override
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) {
        return iteratorShortestPath(getIndex(startVertex), getIndex(targetVertex));
    }

    public Iterator<T> iteratorShortestPath(int startIndex, int targetIndex) {
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();

        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
            return resultList.iterator();
        }

        Iterator<Integer> it;

        try {
            it = iteratorShortestPathIndices(startIndex, targetIndex);

            while (it.hasNext()) {
                resultList.addToRear(vertices[it.next()]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList.iterator();
    }

    private Iterator<Integer> iteratorShortestPathIndices(int startIndex, int targetIndex)
            throws EmptyCollectionException {
        int index = startIndex;
        int[] pathLength = new int[numVertices];
        int[] predecessor = new int[numVertices];
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<Integer> resultList = new ArrayUnorderedList<>();

        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex) || (startIndex == targetIndex)) {
            return resultList.iterator();
        }

        boolean[] visited = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;
        pathLength[startIndex] = 0;
        predecessor[startIndex] = -1;

        while (!traversalQueue.isEmpty() && (index != targetIndex)) {
            index = (traversalQueue.dequeue());

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[index][i] != 0 && !visited[i]) {
                    pathLength[i] = pathLength[index] + 1;
                    predecessor[i] = index;
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }

        if (index != targetIndex) {
            return resultList.iterator();
        }

        LinkedStack<Integer> stack = new LinkedStack<>();
        index = targetIndex;
        stack.push(index);

        do {
            index = predecessor[index];
            stack.push(index);

        } while (index != startIndex);

        while (!stack.isEmpty()) {
            resultList.addToRear(stack.pop());
        }

        return resultList.iterator();
    }

    /**
     * Verifica se o grafo está vazio.
     *
     * @return true se o grafo estiver vazio, false caso contrário.
     */
    @Override
    public boolean isEmpty() {
        return this.numVertices == 0;
    }

    /**
     * Verifica se o grafo é conexo.
     *
     * @return true se o grafo for conexo, false caso contrário.
     */
    @Override
    public boolean isConnected() {
        if (isEmpty()) {
            return false;
        }

        Iterator<T> it = iteratorBFS(0);
        int count = 0;

        while (it.hasNext()) {
            it.next();
            count++;
        }

        return (count == numVertices);
    }

    /**
     * Retorna o número de vértices no grafo.
     *
     * @return O número de vértices no grafo.
     */
    @Override
    public int size() {
        return this.numVertices;
    }

    /**
     * Retorna os vértices adjacentes a um dado vértice.
     *
     * @param vertex O vértice a ser analisado.
     * @return Uma lista de vértices adjacentes.
     * @throws IllegalArgumentException se o vértice não for encontrado.
     */
    public LinkedList<T> getAdjacentes(T vertex) {
        int index = getIndex(vertex);
        if (index == -1) {
            throw new IllegalArgumentException("Vértice não encontrado");
        }

        LinkedList<T> adjacentes = new LinkedList<>();
        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[index][i] != 0) {
                adjacentes.add(vertices[i]);
            }
        }
        return adjacentes;
    }

    /**
     * Verifica se dois vértices são adjacentes.
     *
     * @param vertex1 O primeiro vértice.
     * @param vertex2 O segundo vértice.
     * @return true se os vértices forem adjacentes, false caso contrário.
     */
    public boolean isAdjacent(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (!indexIsValid(index1) || !indexIsValid(index2)) {
            return false;
        }

        return adjMatrix[index1][index2] != 0;
    }

    /**
     * Retorna uma representação textual do grafo.
     *
     * @return Uma string representando o grafo.
     */
    @Override
    public String toString() {
        if (numVertices == 0) {
            return "grafo vazio";
        }

        String result = "";

        result += "\n\t\tMatriz de Adjacencia\n";
        result += "\t\t-----------------------------------------\n";
        result += "\t\tindice\t";

        for (int i = 0; i < numVertices; i++) {
            result += " " + i;
        }

        result += "\n\n";

        for (int i = 0; i < numVertices; i++) {
            result += "" + i + "\t";

            for (int j = 0; j < numVertices; j++) {
                if (adjMatrix[i][j] != 0) {
                    result += "1 ";

                } else {
                    result += "0 ";
                }
            }

            result += "\n";
        }

        result += "\nValores Vertice";
        result += "\n-------------\n";
        result += "indice\tvalor\n\n";

        for (int i = 0; i < numVertices; i++) {
            result += "" + i + "\t";
            result += vertices[i].toString() + "\n";
        }

        return result;
    }
}
