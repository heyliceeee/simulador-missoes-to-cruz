package org.example.collections.implementation;

import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.interfaces.GraphADT;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Graph<T> implements GraphADT<T>, Iterable<T> {
    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices; // numero de vertices no grafo
    protected double[][] adjMatrix; // matriz de adjacencia
    protected T[] vertices; // valores dos vertices

    /**
     * cria um grafo vazio
     */
    public Graph() {
        this.numVertices = 0;
        this.adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

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
     * "expanda" a matriz de adjacencia e array de capacidade de vertices para o
     * dobro do atual tamanho
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
     * Remove um unico vertice com o valor dado do grafo
     *
     * @param index que vai ser removido
     */
    public void removeVertex(int index) {
        if (indexIsValid(index)) {
            numVertices--;

            // Remover o vertice da lista de vertices
            for (int i = index; i < numVertices; i++) {
                vertices[i] = vertices[i + 1];
            }

            // Remover a linha correspondente no adjMatrix
            for (int i = index; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    adjMatrix[i][j] = adjMatrix[i + 1][j];
                }
            }

            // Remover a coluna correspondente no adjMatrix
            for (int i = 0; i < numVertices; i++) {
                for (int j = index; j < numVertices; j++) {
                    adjMatrix[i][j] = adjMatrix[i][j + 1];
                }
            }

            // Opcional: Limpar a ultima posicao para evitar referencias residuais
            vertices[numVertices] = null;
            for (int i = 0; i < numVertices; i++) {
                adjMatrix[numVertices][i] = 0;
                adjMatrix[i][numVertices] = 0;
            }
        } else {
            throw new IllegalArgumentException("Indice do vertice invalido");
        }
    }

    @Override
    public void addEdge(T vertex1, T vertex2) {
        this.addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * retorna o indice do vertice
     * 
     * @param vertex a ser procurado
     * @return o indice do vertice
     */
    public int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equals(vertex)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * insere uma aresta entre 2 vertices do grafo
     * 
     * @param index1 o primeiro indice
     * @param index2 o segundo indice
     */
    public void addEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = 1;
            adjMatrix[index2][index1] = 1; // Adiciona a ligacao reversa para tornar o grafo nao direcionado
        } else {
            throw new IllegalArgumentException("Pelo menos um dos vertices nao e valido");
        }
    }

    /**
     * retorna true se o indice e valido, caso contrario false
     * 
     * @param index a ser verificado
     * @return true se o indice e valido, caso contrario false
     */
    private boolean indexIsValid(int index) {
        return ((index < numVertices) && index >= 0);
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        this.removeEdge(getIndex(vertex1), getIndex(vertex2));
    }

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
     * retorna true caso a aresta existe, caso contrario retorna false
     * 
     * @param index1
     * @param index2
     * @return true caso a aresta existe, caso contrario retorna false
     */
    private boolean edgeExists(int index1, int index2) {
        return adjMatrix[index1][index2] != 0 || adjMatrix[index2][index1] != 0;
    }

    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        int startIndex = getIndex(startVertex); // Converte o vertice em indice

        if (startIndex == -1) {
            throw new IllegalArgumentException("Vertice nao encontrado");
        }

        return iteratorBFS(startIndex);
    }

    /**
     * retorna um iterador que executa uma travessia de pesquisa ampla comecando no
     * indice dado
     * 
     * @param startIndex o indice para iniciar a pesquisa
     * @return um iterador que executa uma travessia de pesquisa ampla comecando no
     *         indice dado
     */
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

                // encontre todos os vertices adjacentes a x que nao foram visitados e
                // coloque-os na queue
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

    @Override
    public Iterator<T> iteratorDFS(T startVertex) {
        int startIndex = getIndex(startVertex); // Converte o vertice em indice

        if (startIndex == -1) {
            throw new IllegalArgumentException("Vertice nao encontrado");
        }

        return iteratorDFS(startIndex);
    }

    /**
     * retorna um iterador que executa uma primeira travessia de pesquisa em
     * profundidade comecando no indice dado.
     *
     * @param startIndex o indice para iniciar o percurso de pesquisa em
     *                   profundidade
     * @return iterador que executa uma primeira travessia de pesquisa em
     *         profundidade comecando no indice dado.
     */
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

            // encontre um vertice adjacente a x que nao foi visitado e coloque-o na queue
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

    @Override
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) {
        return iteratorShortestPath(getIndex(startVertex), getIndex(targetVertex));
    }

    /**
     * retorna um iterador que contem o caminho mais curto entre os dois vertices
     *
     * @param startIndex  o indice do vertice inicial
     * @param targetIndex o indice do vertice final
     * @return um iterador que contem o caminho mais curto entre os dois vertices
     */
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

    /**
     * retorna um iterador que contem os indices do caminho mais curto entre dois
     * vertices
     *
     * @param startIndex  o indice do vertice inicial
     * @param targetIndex o indice do vertice final
     * @return um iterador que contem os indices do caminho mais curto entre dois
     *         vertices
     * @throws EmptyCollectionException
     */
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

            // Atualiza o pathLength para cada vertice nao visitado adjacente ao vertice no
            // indice atual
            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[index][i] != 0 && !visited[i]) {
                    pathLength[i] = pathLength[index] + 1;
                    predecessor[i] = index;
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }

        // nenhum caminho deve ter sido encontrado
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

    @Override
    public boolean isEmpty() {
        return this.numVertices == 0;
    }

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

    @Override
    public int size() {
        return this.numVertices;
    }

    /**
     * Obtem os vertices adjacentes a um dado vertice.
     *
     * @param vertex O vertice para o qual os vizinhos serao obtidos.
     * @return Uma lista de vertices adjacentes.
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
     * Retorna uma copia profunda do array de vertices.
     *
     * @return Copia profunda do array de vertices.
     */
    /*public T[] getVertices() {
        T[] verticesCopy = (T[]) new Object[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] != null) {
                verticesCopy[i] = deepCopy(vertices[i]);
            }
        }

        return verticesCopy;
    }
*/
    /**
     * Realiza uma copia profunda de um objeto do tipo T.
     *
     * @param object Objeto a ser copiado.
     * @return Copia profunda do objeto.
     */
   /*  private T deepCopy(T object) {
        try {
            // Verifica se o objeto implementa Cloneable e invoca o metodo clone
            if (object instanceof Cloneable) {
                return (T) object.getClass().getMethod("clone").invoke(object);
            }
            // Se nao implementar Cloneable, lanca excecao
            throw new CloneNotSupportedException("Objeto nao implementa Cloneable: " + object);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao realizar copia profunda", e);
        }
    }*/
    
    /**
     * Verifica se dois vertices sao adjacentes no grafo.
     * 
     * @param vertex1 O primeiro vertice.
     * @param vertex2 O segundo vertice.
     * @return true se os vertices sao adjacentes, false caso contrario.
     */
    public boolean isAdjacent(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (!indexIsValid(index1) || !indexIsValid(index2)) {
            return false;
        }

        return adjMatrix[index1][index2] != 0;
    }

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
