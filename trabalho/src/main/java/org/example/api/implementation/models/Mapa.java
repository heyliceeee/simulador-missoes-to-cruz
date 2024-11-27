package org.example.api.implementation.models;

import java.util.Arrays;


/**
 * Representa o grafo do edificio e a matriz de adjacencia
 */
public class Mapa {
    private int[][] adjMatrix; // Matriz de adjacência
    private String[] divisaoNomes; // Nomes das divisões


    //Representa o edifício como um grafo não ponderado com uma matriz de adjacência:
    //  - Cada linha/coluna representa uma divisão
    //  - 1 significa que há uma ligação entre duas divisões, 0 significa que não há


    /**
     * verifica se ha ligacao entre duas divisoes
     * @param de
     * @param para
     * @return
     */
    public boolean podeMover(Divisao de, Divisao para) {
        int i = getIndice(de);
        int j = getIndice(para);
        return adjMatrix[i][j] == 1;
    }


    /**
     * mostra o mapa do edificio na consola para debug ou visualizacao
     */
    public void mostrarMapa() {
        for (int i = 0; i < adjMatrix.length; i++) {
            System.out.println(divisaoNomes[i] + ": " + Arrays.toString(adjMatrix[i]));
        }
    }
}
