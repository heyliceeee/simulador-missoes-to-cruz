package org.example.collections.implementation;

import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.interfaces.GraphADT;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementacao de um grafo utilizando uma matriz de adjacencia.
 *
 * @param <T> O tipo de elementos armazenados nos vertices do grafo.
 */
public class Graph<T> implements GraphADT<T>, Iterable<T> {
    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices;
    protected double[][] adjMatrix;
    protected T[] vertices;

    /**
     * Construtor padrao que inicializa o grafo com capacidade padrao.
     */
    public Graph() {
        this.numVertices = 0;
        this.adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Adiciona um vertice ao grafo.
     *
     * @param vertex O vertice a ser adicionado.
     * @return true se o vertice foi adicionado com sucesso.
     * @throws IllegalArgumentException se o vertice for nulo.
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
     * Retorna um iterador para os vertices do grafo.
     *
     * @return Iterador dos vertices.
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
     * Remove um vertice do grafo.
     *
     * @param vertex O vertice a ser removido.
     * @return true se o vertice foi removido com sucesso.
     * @throws NoSuchElementException se o vertice nao for encontrado.
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
     * Remove um vertice do grafo pelo indice.
     *
     * @param index O indice do vertice a ser removido.
     * @throws IllegalArgumentException se o indice for invalido.
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
            throw new IllegalArgumentException("Indice do vertice invalido");
        }
    }

    /**
     * Adiciona uma aresta entre dois vertices.
     *
     * @param vertex1 O primeiro vertice.
     * @param vertex2 O segundo vertice.
     * @throws IllegalArgumentException se algum dos vertices for invalido.
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
     * Adiciona uma aresta entre dois indices de vertices.
     *
     * @param index1 indice do primeiro vertice.
     * @param index2 indice do segundo vertice.
     * @throws IllegalArgumentException se algum dos indices for invalido.
     */
    public void addEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = 1;
            adjMatrix[index2][index1] = 1;
        } else {
            throw new IllegalArgumentException("Pelo menos um dos vertices nao e valido");
        }
    }

    /**
     * Verifica se o indice de um vertice e valido.
     *
     * @param index O indice a ser verificado.
     * @return true se o indice for valido, false caso contrario.
     */
    private boolean indexIsValid(int index) {
        return ((index < numVertices) && index >= 0);
    }

    /**
     * Remove uma aresta entre dois vertices.
     *
     * @param vertex1 O primeiro vertice.
     * @param vertex2 O segundo vertice.
     * @throws IllegalArgumentException se a aresta nao existir ou os vertices forem
     *                                  invalidos.
     */
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        this.removeEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Remove uma aresta entre dois indices de vertices.
     *
     * @param index1 indice do primeiro vertice.
     * @param index2 indice do segundo vertice.
     * @throws IllegalArgumentException se algum dos indices for invalido ou a
     *                                  aresta nao existir.
     */
    public void removeEdge(int index1, int index2) {
        if (!indexIsValid(index1)) {
            throw new IllegalArgumentException("O primeiro vertice nao e valido");
        }

        if (!indexIsValid(index2)) {
            throw new IllegalArgumentException("O segundo vertice nao e valido");
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
     * Verifica se uma aresta existe entre dois indices de vertices.
     *
     * @param index1 indice do primeiro vertice.
     * @param index2 indice do segundo vertice.
     * @return true se a aresta existir, false caso contrario.
     */
    private boolean edgeExists(int index1, int index2) {
        return adjMatrix[index1][index2] != 0 || adjMatrix[index2][index1] != 0;
    }

    /**
     * Retorna um iterador para a travessia em largura (BFS) a partir de um vertice
     * inicial.
     *
     * @param startVertex O vertice inicial.
     * @return Iterador para a travessia em largura.
     * @throws IllegalArgumentException se o vertice nao for encontrado.
     */
    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        int startIndex = getIndex(startVertex);

        if (startIndex == -1) {
            throw new IllegalArgumentException("Vertice nao encontrado");
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
     * vertice inicial.
     *
     * @param startVertex O vertice inicial.
     * @return Iterador para a travessia em profundidade.
     * @throws IllegalArgumentException se o vertice nao for encontrado.
     */
    @Override
    public Iterator<T> iteratorDFS(T startVertex) {
        int startIndex = getIndex(startVertex);
        if (startIndex == -1) {
            throw new IllegalArgumentException("Vertice nao encontrado");
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
     * Retorna um iterador para o caminho mais curto entre dois vertices.
     *
     * @param startVertex  O vertice de origem.
     * @param targetVertex O vertice de destino.
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
     * Verifica se o grafo esta vazio.
     *
     * @return true se o grafo estiver vazio, false caso contrario.
     */
    @Override
    public boolean isEmpty() {
        return this.numVertices == 0;
    }

    /**
     * Verifica se o grafo e conexo.
     *
     * @return true se o grafo for conexo, false caso contrario.
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
     * Retorna o numero de vertices no grafo.
     *
     * @return O numero de vertices no grafo.
     */
    @Override
    public int size() {
        return this.numVertices;
    }

    /**
     * Retorna os vertices adjacentes a um dado vertice.
     *
     * @param vertex O vertice a ser analisado.
     * @return Uma lista de vertices adjacentes.
     * @throws IllegalArgumentException se o vertice nao for encontrado.
     */
    public LinkedList<T> getAdjacentes(T vertex) {
        int index = getIndex(vertex);
        if (index == -1) {
            throw new IllegalArgumentException("Vertice nao encontrado");
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
     * Verifica se dois vertices sao adjacentes.
     *
     * @param vertex1 O primeiro vertice.
     * @param vertex2 O segundo vertice.
     * @return true se os vertices forem adjacentes, false caso contrario.
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
     * Retorna uma representacao textual do grafo.
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
