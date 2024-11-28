package org.example.api.implementation.models;


import org.example.collections.implementation.ArrayOrderedList;
import org.example.collections.implementation.LinkedList;

/**
 * Representa o grafo do edificio e a matriz de adjacencia
 */
public class Mapa {
    /**
     * Representa o edifício como um grafo não ponderado com uma matriz de adjacência:
     * <br> - 1 significa que há uma ligação entre duas divisões
     * <br> - Cada linha/coluna representa uma divisão , 0 significa que não há
     */
    private int[][] adjMatrix;

    /**
     * Lista de divisões
     */
    private LinkedList<Divisao> divisoes;

    /**
     * Lista de divisões de entrada/saída
     */
    private LinkedList<String> entradasSaidas;

    /**
     * Informações do alvo
     */
    private Alvo alvo;




    /**
     * Construtor do Mapa
     *
     * @param numDivisoes Número total de divisões no edifício.
     */
    public Mapa(int numDivisoes) {
        this.adjMatrix = new int[numDivisoes][numDivisoes];
        this.divisoes = new LinkedList<>();
    }


    public Mapa() {
        this.divisoes = new LinkedList<>();
        this.entradasSaidas = new LinkedList<>();
        this.adjMatrix = new int[1][1];
    }

    /**
     * Adiciona uma divisão ao mapa.
     *
     * @param divisao divisão
     */
    public void adicionarDivisao(Divisao divisao) {
        divisoes.add(divisao);
    }

    /**
     * Adiciona uma divisão ao mapa.
     *
     * @param nomeDivisao divisão
     */
    public void adicionarDivisao(String nomeDivisao) {
        Divisao divisao = new Divisao(nomeDivisao);
        divisoes.add(divisao);

        // Redimensiona a matriz de adjacência, se necessário
        if (divisoes.getSize() > adjMatrix.length) {
            redimensionarMatriz();
        }
    }

    public LinkedList<Divisao> obterConexoes(Divisao divisao) {
        LinkedList<Divisao> conexoes = new LinkedList<>();
        int indice = getIndiceDivisao(divisao.getNomeDivisao());

        for (int i = 0; i < adjMatrix[indice].length; i++) {
            if (adjMatrix[indice][i] == 1) {
                Divisao vizinho = divisoes.getElementAt(i);

                // Verifica se o vizinho é válido e não é a própria divisão
                if (vizinho != null && !vizinho.equals(divisao)) {
                    conexoes.add(vizinho);
                }
            }
        }

        return conexoes;
    }


    /**
     * Cria uma ligação entre duas divisões.
     *
     * @param nomeDivisao1 Nome da divisão de origem.
     * @param nomeDivisao2 Nome da divisão de destino.
     */
    public void adicionarLigacao(String nomeDivisao1, String nomeDivisao2) {
        int indice1 = getIndiceDivisao(nomeDivisao1);
        int indice2 = getIndiceDivisao(nomeDivisao2);

        if (indice1 != -1 && indice2 != -1) {
            adjMatrix[indice1][indice2] = 1;
            adjMatrix[indice2][indice1] = 1; // Ligação bidirecional
        } else {
            System.out.println("Erro: Uma ou ambas as divisões não foram encontradas.");
        }
    }

    private int getIndiceDivisao(String nomeDivisao) {
        for (int i = 0; i < divisoes.getSize(); i++) {
            if (divisoes.getElementAt(i).getNomeDivisao().equals(nomeDivisao)) {
                return i;
            }
        }
        return -1; // Retorna -1 se a divisão não for encontrada
    }

    private void redimensionarMatriz() {
        int novoTamanho = adjMatrix.length * 2; // Dobra o tamanho atual
        int[][] novaAdjMatrix = new int[novoTamanho][novoTamanho];

        // Copia os valores antigos para a nova matriz
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix[i].length; j++) {
                novaAdjMatrix[i][j] = adjMatrix[i][j];
            }
        }

        adjMatrix = novaAdjMatrix; // Substitui pela nova matriz
    }

    /**
     * Verifica se há ligação entre duas divisões.
     *
     * @param de Nome da divisão de origem.
     * @param para Nome da divisão de destino.
     * @return true se há ligação, false caso contrário.
     */
    public boolean podeMover(String de, String para) {
        int i = getIndice(de);
        int j = getIndice(para);
        return adjMatrix[i][j] == 1;
    }

    /**
     * Obtém o indice de uma divisão pelo nome.
     *
     * @param nome Nome da divisão.
     * @return indice da divisão.
     */
    private int getIndice(String nome) {
        for (int i = 0; i < divisoes.getSize(); i++) {
            if (divisoes.getElementAt(i).getNomeDivisao().equals(nome)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Divisão não encontrada: " + nome);
    }

    /**
     * Mostra o mapa do edifício na consola para debug ou visualização.
     */
    public void mostrarMapa() {
        for (int i = 0; i < adjMatrix.length; i++) {
            System.out.println(divisoes.getElementAt(i) + ": " + java.util.Arrays.toString(adjMatrix[i]));
        }
    }

    public void adicionarItem(String nomeDivisao, Item item) {
        for (int i = 0; i < divisoes.getSize(); i++) {
            Divisao divisao = divisoes.getElementAt(i);
            if (divisao.getNomeDivisao().equals(nomeDivisao)) {
                divisao.adicionarItem(item);
                return;
            }
        }
        System.out.println("Divisão não encontrada: " + nomeDivisao);
    }


    public LinkedList<Divisao> getDivisao() {
        return divisoes;
    }

    public void setDivisao(LinkedList<Divisao> divisoes) {
        this.divisoes = divisoes;
    }

    public Divisao getDivisaoPorNome(String nomeDivisao) {
        for (int i = 0; i < divisoes.getSize(); i++) {
            Divisao divisao = divisoes.getElementAt(i);
            if (divisao.getNomeDivisao().equals(nomeDivisao)) {
                return divisao;
            }
        }
        throw new IllegalArgumentException("Divisão não encontrada: " + nomeDivisao);
    }


    public void adicionarEntradaSaida(String divisao) {
        entradasSaidas.add(divisao);
    }

    public void definirAlvo(Divisao divisao, String tipo) {
        alvo = new Alvo(divisao, tipo);
    }

    public Alvo getAlvo() {
        return alvo;
    }

    public LinkedList<String> getEntradasSaidas() {
        return entradasSaidas;
    }
}
