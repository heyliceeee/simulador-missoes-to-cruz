package org.example;

import org.example.api.implementation.models.*;
import org.example.api.implementation.simulation.SimulacaoAutomatica;
import org.example.collections.implementation.LinkedList;

public class Main {
    public static void main(String[] args) {
        Mapa mapa = new Mapa();
        mapa.adicionarDivisao("Entrada");
        mapa.adicionarDivisao("Sala A");
        mapa.adicionarDivisao("Sala B");
        mapa.adicionarDivisao("Alvo");

        mapa.adicionarLigacao("Entrada", "Sala A");
        mapa.adicionarLigacao("Sala A", "Sala B");
        mapa.adicionarLigacao("Sala B", "Alvo");

        SimulacaoAutomatica simulacao = new SimulacaoAutomatica(mapa, new ToCruz("Tó Cruz", 100));
        LinkedList<Divisao> caminho = simulacao.calcularMelhorCaminho(
                mapa.getDivisaoPorNome("Entrada"),
                mapa.getDivisaoPorNome("Alvo")
        );

        System.out.println("Caminho calculado:");
        for (int i = 0; i < caminho.getSize(); i++) {
            System.out.print(caminho.getElementAt(i).getNomeDivisao() + " -> ");
        }
        System.out.println("Objetivo");

    }
}