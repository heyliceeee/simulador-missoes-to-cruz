package org.example.api.implementation.simulation;

import org.example.api.implementation.models.*;
import org.example.api.implementation.services.CombateService;
import org.example.collections.implementation.LinkedList;

/**
 * Gerencia a simulação automática para o Tó Cruz.
 */
public class SimulacaoAutomatica {

    private Mapa mapa;
    private ToCruz toCruz;
    private CombateService combateService;

    /**
     * Construtor da Simulação Automática.
     *
     * @param mapa O mapa do edifício.
     * @param toCruz O agente controlado pelo jogador.
     */
    public SimulacaoAutomatica(Mapa mapa, ToCruz toCruz) {
        this.mapa = mapa;
        this.toCruz = toCruz;
        this.combateService = new CombateService();
    }

    /**
     * Executa a simulação automática para alcançar um objetivo.
     *
     * @param divisaoObjetivo A divisão onde o objetivo está localizado.
     */
    public void executar(Divisao divisaoObjetivo) {
        System.out.println("Início da simulação automática!");

        // Utilizando LinkedList para BFS
        LinkedList<Divisao> fila = new LinkedList<>();
        LinkedList<Divisao> visitados = new LinkedList<>();
        LinkedList<Predecessor> caminho = new LinkedList<>(); // Predecessores para reconstrução do caminho

        fila.add(toCruz.getPosicaoAtual());
        visitados.add(toCruz.getPosicaoAtual());
        caminho.add(new Predecessor(toCruz.getPosicaoAtual(), null));

        while (fila.getSize() > 0) {
            Divisao atual = fila.remove(fila.getElementAt(0));

            if (atual.equals(divisaoObjetivo)) {
                reconstruirCaminho(caminho, divisaoObjetivo);
                return;
            }

            for (int i = 0; i < mapa.getDivisao().getSize(); i++) {
                Divisao vizinho = mapa.getDivisao().getElementAt(i);
                if (mapa.podeMover(atual.getNomeDivisao(), vizinho.getNomeDivisao()) && !visitados.contains(vizinho)) {
                    visitados.add(vizinho);

                    if (!visitados.contains(vizinho) && !fila.contains(vizinho)) {
                        fila.add(vizinho);
                    }

                    caminho.add(new Predecessor(vizinho, atual));
                }
            }
        }

        System.out.println("Caminho não encontrado para o objetivo.");
    }

    /**
     * Reconstrói o caminho e realiza os movimentos automáticos.
     *
     * @param caminho A lista de predecessores.
     * @param objetivo A divisão objetivo.
     */
    private void reconstruirCaminho(LinkedList<Predecessor> caminho, Divisao objetivo) {
        LinkedList<Divisao> caminhoReverso = new LinkedList<>();
        Divisao atual = objetivo;

        while (atual != null) {
            caminhoReverso.add(atual);
            atual = getPredecessor(caminho, atual.getNomeDivisao());
        }

        System.out.println("Caminho encontrado:");
        for (int i = caminhoReverso.getSize() - 1; i >= 0; i--) {
            Divisao divisao = caminhoReverso.getElementAt(i);
            System.out.print(divisao.getNomeDivisao() + " -> ");
        }
        System.out.println("Objetivo");

        // Realiza os movimentos automáticos
        for (int i = caminhoReverso.getSize() - 1; i >= 0; i--) {
            moverParaDivisao(caminhoReverso.getElementAt(i));
            if (toCruz.getVida() <= 0) {
                System.out.println("Tó Cruz foi derrotado durante a simulação!");
                return;
            }
        }

        System.out.println("Tó Cruz alcançou o objetivo com sucesso!");
    }

    /**
     * Move o Tó Cruz para a divisão especificada e resolve eventos.
     *
     * @param divisao A divisão para onde mover.
     */
    private void moverParaDivisao(Divisao divisao) {
        toCruz.moverPara(divisao);
        System.out.println("Tó Cruz moveu-se para: " + divisao.getNomeDivisao());

        // Resolver combates
        combateService.resolverCombate(toCruz, divisao);

        // Coletar itens automaticamente
        while (divisao.getItensPresentes().getSize() > 0) {
            Item item = divisao.getItensPresentes().getElementAt(0);
            toCruz.adicionarAoInventario(item);
            divisao.removerItem(item);
            System.out.println("Tó Cruz coletou: " + item.getTipo());
        }
    }

    public void calcularTrajetoCompleto() {
        LinkedList<LinkedList<Divisao>> melhoresTrajetos = new LinkedList<>(); // Lista de trajetos

        LinkedList<String> entradas = mapa.getEntradasSaidas();
        Divisao alvo = mapa.getAlvo().getDivisao(); // Obtem o alvo como uma Divisao

        for (int i = 0; i < entradas.getSize(); i++) {
            String nomeEntrada = entradas.getElementAt(i);
            Divisao entrada = mapa.getDivisaoPorNome(nomeEntrada); // Converte nome para Divisao
            LinkedList<Divisao> trajeto = calcularMelhorCaminho(entrada, alvo);
            melhoresTrajetos.add(trajeto); // Adiciona o trajeto completo
        }

        System.out.println("Melhor trajeto considerando todas as entradas:");
        LinkedList<Divisao> melhorTrajeto = melhoresTrajetos.getElementAt(0); // Supõe que o primeiro trajeto é o melhor

        for (int i = 0; i < melhorTrajeto.getSize(); i++) {
            Divisao divisao = melhorTrajeto.getElementAt(i); // Acessa cada divisão no trajeto
            System.out.print(divisao.getNomeDivisao() + " -> ");
        }
        System.out.println("Objetivo");
    }



    public LinkedList<Divisao> calcularMelhorCaminho(Divisao origem, Divisao destino) {
        LinkedList<Divisao> caminho = new LinkedList<>();
        LinkedList<Divisao> visitados = new LinkedList<>();
        LinkedList<Divisao> fila = new LinkedList<>();
        LinkedList<Predecessor> predecessores = new LinkedList<>();

        fila.add(origem);
        int maxIteracoes = 1000; // Limite de segurança
        int iteracoes = 0;

        while (fila.getSize() > 0 && iteracoes < maxIteracoes) {
            iteracoes++;

            // Bloco de depuração para inspecionar fila e visitados
            System.out.println("Fila Atual: ");
            for (int i = 0; i < fila.getSize(); i++) {
                System.out.print(fila.getElementAt(i).getNomeDivisao() + " ");
            }
            System.out.println("\nVisitados: ");
            for (int i = 0; i < visitados.getSize(); i++) {
                System.out.print(visitados.getElementAt(i).getNomeDivisao() + " ");
            }
            System.out.println("\n");

            Divisao atual = fila.remove(fila.getElementAt(0));

            if (atual.equals(destino)) {
                Divisao passo = destino;

                while (passo != null) {
                    caminho.add(passo);
                    Divisao anterior = getPredecessor(predecessores, passo.getNomeDivisao());
                    if (anterior == null) {
                        break;
                    }
                    passo = anterior;
                }
                break;
            }

            LinkedList<Divisao> conexoes = mapa.obterConexoes(atual);
            for (int i = 0; i < conexoes.getSize(); i++) {
                Divisao vizinho = conexoes.getElementAt(i);

                if (!visitados.contains(vizinho) && !fila.contains(vizinho)) {
                    visitados.add(vizinho);
                    fila.add(vizinho);

                    if (!predecessores.contains(new Predecessor(vizinho, atual))) {
                        predecessores.add(new Predecessor(vizinho, atual));
                    }
                }
            }

            // Logs para depuração
            System.out.println("Fila: " + fila.getSize() + ", Visitados: " + visitados.getSize());
        }

        if (iteracoes >= maxIteracoes) {
            System.err.println("Limite de iterações atingido! Verifica a lógica.");
        }

        LinkedList<Divisao> caminhoInvertido = new LinkedList<>();
        for (int i = caminho.getSize() - 1; i >= 0; i--) {
            caminhoInvertido.add(caminho.getElementAt(i));
        }

        return caminhoInvertido;
    }







    /**
     * Obtém o predecessor de uma divisão.
     *
     * @param predecessores A lista de predecessores.
     * @param nomeDivisao A divisão atual.
     * @return A divisão predecessora.
     */
    private Divisao getPredecessor(LinkedList<Predecessor> predecessores, String nomeDivisao) {
        for (int i = 0; i < predecessores.getSize(); i++) {
            Predecessor p = predecessores.getElementAt(i);
            if (p.getAtual().getNomeDivisao().equals(nomeDivisao)) {
                return p.getPredecessor(); // Retorna o predecessor correto
            }
        }
        return null; // Se não há predecessor, retorna null
    }





    /**
     * Classe auxiliar para armazenar predecessores.
     */
    private static class Predecessor {
        private Divisao atual;
        private Divisao predecessor;

        public Predecessor(Divisao atual, Divisao predecessor) {
            this.atual = atual;
            this.predecessor = predecessor;
        }

        public Divisao getAtual() {
            return atual;
        }

        public Divisao getPredecessor() {
            return predecessor;
        }
    }
}