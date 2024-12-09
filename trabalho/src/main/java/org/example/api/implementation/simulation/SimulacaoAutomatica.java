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
    private LinkedList<Divisao> caminhoPercorrido;

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
        this.caminhoPercorrido = new LinkedList<>();
    }

    /**
     * Executa a simulação automática para alcançar um objetivo.
     *
     * @param divisaoObjetivo A divisão onde o objetivo está localizado.
     */
    public void executar(Divisao divisaoObjetivo) {
        System.out.println("Início da simulação automática!");
    
        LinkedList<Divisao> fila = new LinkedList<>();
        LinkedList<Divisao> visitados = new LinkedList<>();
        LinkedList<Predecessor> caminho = new LinkedList<>();
    
        fila.add(toCruz.getPosicaoAtual());
        visitados.add(toCruz.getPosicaoAtual());
        caminho.add(new Predecessor(toCruz.getPosicaoAtual(), null));
    
        while (fila.getSize() > 0) {
            Divisao atual = fila.remove(fila.getElementAt(0));
    
            if (atual.equals(divisaoObjetivo)) {
                // Reconstruir o caminho até o objetivo
                reconstruirCaminho(caminho, divisaoObjetivo);
    
                // Após alcançar o objetivo, verifica o trajeto de volta
                if (toCruz.getVida() > 0) {
                    verificarTrajetoDeVolta();
                }
                return;
            }
    
            // Obtém as conexões da divisão atual
            LinkedList<Divisao> conexoes = mapa.obterConexoes(atual);
            for (int i = 0; i < conexoes.getSize(); i++) {
                Divisao vizinho = conexoes.getElementAt(i);
    
                // Verifica se é possível mover para a próxima divisão
                if (mapa.podeMover(atual.getNomeDivisao(), vizinho.getNomeDivisao()) && !visitados.contains(vizinho)) {
                    visitados.add(vizinho);
                    fila.add(vizinho);
                    caminho.add(new Predecessor(vizinho, atual));
                }
            }
        }
    
        System.out.println("Caminho não encontrado para o objetivo.");
    }
    
    

    public LinkedList<Divisao> getCaminhoPercorrido() {
        return caminhoPercorrido;
    }

    public int getVidaRestante() {
        return toCruz.getVida();
    }

    public String getStatus() {
        return toCruz.getVida() > 0 ? "SUCESSO" : "FALHA";
    }

    public Divisao getDivisaoFinal() {
        return toCruz.getPosicaoAtual();
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
    
        // Armazena o caminho percorrido
        for (int i = caminhoReverso.getSize() - 1; i >= 0; i--) {
            Divisao divisao = caminhoReverso.getElementAt(i);
            caminhoPercorrido.add(divisao);
            moverParaDivisao(divisao);
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
        LinkedList<LinkedList<Divisao>> melhoresTrajetos = new LinkedList<>();
    
        LinkedList<String> entradas = mapa.getEntradasSaidasNomes();
        Divisao alvo = mapa.getAlvo().getDivisao();
    
        for (int i = 0; i < entradas.getSize(); i++) {
            String nomeEntrada = entradas.getElementAt(i);
            Divisao entrada = mapa.getDivisaoPorNome(nomeEntrada);
            LinkedList<Divisao> trajeto = calcularMelhorCaminho(entrada, alvo);
            melhoresTrajetos.add(trajeto);
        }
    
        System.out.println("Melhor trajeto considerando todas as entradas:");
        LinkedList<Divisao> melhorTrajeto = melhoresTrajetos.getElementAt(0);
    
        for (int i = 0; i < melhorTrajeto.getSize(); i++) {
            Divisao divisao = melhorTrajeto.getElementAt(i);
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

    private void registrarCaminho(LinkedList<Divisao> caminhoReverso) {
        for (int i = caminhoReverso.getSize() - 1; i >= 0; i--) {
            Divisao divisao = caminhoReverso.getElementAt(i);
            caminhoPercorrido.add(divisao);
        }
    }


 /**
     * Obtém o caminho percorrido em nomes de divisões.
     *
     * @return Uma lista de nomes das divisões percorridas.
     */
    public LinkedList<String> getCaminhoPercorridoNomes() {
        LinkedList<String> nomes = new LinkedList<>();
        for (int i = 0; i < caminhoPercorrido.getSize(); i++) {
            nomes.add(caminhoPercorrido.getElementAt(i).getNomeDivisao());
        }
        return nomes;
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
                return p.getPredecessor();
            }
        }
        return null;
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

    /**
     * Verifica se o Tó Cruz pode retornar ao ponto de saída sem perder todos os pontos de vida.
     */
    public void verificarTrajetoDeVolta() {
        LinkedList<Divisao> saidas = mapa.getEntradasSaidas(); // Obtém todas as saídas disponíveis no mapa

        if (saidas.isEmpty()) {
            System.out.println("Erro: Não foi encontrada uma saída válida no mapa.");
            return;
        }

        // Determinar a melhor saída com base na distância
        Divisao melhorSaida = null;
        LinkedList<Divisao> melhorCaminho = null;
        int menorDistancia = Integer.MAX_VALUE;

        for (int i = 0; i < saidas.getSize(); i++) {
            Divisao saida = saidas.getElementAt(i);
            LinkedList<Divisao> caminhoParaSaida = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(), saida);
            if (caminhoParaSaida.getSize() < menorDistancia) {
                menorDistancia = caminhoParaSaida.getSize();
                melhorSaida = saida;
                melhorCaminho = caminhoParaSaida;
            }
        }

        if (melhorSaida == null || melhorCaminho == null) {
            System.out.println("Erro: Não foi possível calcular o trajeto até uma saída válida.");
            return;
        }

        System.out.println("Verificando trajeto de volta para a saída: " + melhorSaida.getNomeDivisao());
        for (Divisao divisao : melhorCaminho) {
            System.out.println("Movendo para: " + divisao.getNomeDivisao());

            // Resolver combates em divisões do trajeto de volta
            if (divisao.getInimigosPresentes().getSize() > 0) {
                combateService.resolverCombate(toCruz, divisao);
                if (toCruz.getVida() <= 0) {
                    System.out.println("Tó Cruz foi derrotado durante o trajeto de volta!");
                    return;
                }
            }

            // Recuperar vida com itens disponíveis na divisão
            while (divisao.getItensPresentes().getSize() > 0) {
                Item item = divisao.getItensPresentes().getElementAt(0);
                if (item.getTipo().equalsIgnoreCase("kit de vida")) {
                    toCruz.recuperarVida(item.getPontos());
                    divisao.removerItem(item);
                    System.out.println("Tó Cruz usou um kit de vida e recuperou " + item.getPontos() + " pontos de vida.");
                } else if (item.getTipo().equalsIgnoreCase("colete")) {
                    toCruz.adicionarAoInventario(item);
                    divisao.removerItem(item);
                    System.out.println("Tó Cruz coletou um colete.");
                }
            }
        }

        System.out.println("Missão concluída com sucesso! Tó Cruz chegou ao ponto de saída.");
    }
}