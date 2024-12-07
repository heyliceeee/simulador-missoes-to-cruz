package org.example.api.implementation.simulation;

import org.example.api.implementation.models.*;
import org.example.api.implementation.services.CombateService;
import org.example.collections.implementation.LinkedList;

import java.util.Scanner;

/**
 * Gerencia a simulação manual, onde o jogador toma as decisões.
 */
public class SimulacaoManual {

    private Mapa mapa;
    private ToCruz toCruz;
    private Scanner scanner;
    private CombateService combateService;

    /**
     * Construtor da Simulação Manual.
     *
     * @param mapa O mapa do edifício.
     * @param toCruz O agente controlado pelo jogador.
     */
    public SimulacaoManual(Mapa mapa, ToCruz toCruz) {
        this.mapa = mapa;
        this.toCruz = toCruz;
        this.scanner = new Scanner(System.in);
        this.combateService = new CombateService();
    }

    /**
     * Executa o loop principal da simulação manual.
     */
    public void executar() {
        System.out.println("Início da simulação manual!");
        while (toCruz.getVida() > 0) {
            mostrarEstado();
            String comando = obterComando();

            switch (comando.toLowerCase()) {
                case "mover":
                    mover();
                    break;
                case "usar":
                    toCruz.usarKitDeVida();
                    break;
                case "atacar":
                    atacar();
                    break;
                case "sair":
                    System.out.println("Simulação terminada.");
                    return;
                default:
                    System.out.println("Comando inválido. Tente novamente.");
            }
        }
        System.out.println("Tó Cruz foi derrotado! Simulação encerrada.");
    }

    public int getVidaRestante() {
        return toCruz.getVida();
    }

    public String getStatus() {
        return toCruz.getVida() > 0 ? "SUCESSO" : "FALHA";
    }

    public String getDivisaoFinal() {
        return toCruz.getPosicaoAtual().getNomeDivisao();
    }

    /**
     * Mostra o estado atual do jogo.
     */
    private void mostrarEstado() {
        System.out.println("\n--- Estado Atual ---");
        System.out.println("Posição: " + toCruz.getPosicaoAtual().getNomeDivisao());
        System.out.println("Vida: " + toCruz.getVida());
        System.out.println("Inventário: " + toCruz.getInventario().size() + " itens");
        System.out.println("---------------------");
    }

    /**
     * Obtém um comando do jogador.
     *
     * @return O comando inserido pelo jogador.
     */
    private String obterComando() {
        System.out.println("Comandos disponíveis: mover, usar, atacar, sair");
        System.out.print("Digite seu comando: ");
        return scanner.nextLine();
    }

    /**
     * Gerencia o movimento do Tó Cruz.
     */
    private void mover() {
        System.out.print("Digite o nome da divisão para onde deseja mover: ");
        String novaDivisao = scanner.nextLine();

        try {
            if (mapa.podeMover(toCruz.getPosicaoAtual().getNomeDivisao(), novaDivisao)) {
                Divisao proximaDivisao = mapa.getDivisaoPorNome(novaDivisao);
                toCruz.moverPara(proximaDivisao);
                verificarItens(proximaDivisao);
                combateService.resolverCombate(toCruz, proximaDivisao);
            } else {
                System.out.println("Movimento inválido! Divisões não conectadas.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Divisão não encontrada.");
        }
    }

    /**
     * Realiza um ataque contra os inimigos na divisão atual.
     */
    private void atacar() {
        Divisao divisaoAtual = toCruz.getPosicaoAtual();
        combateService.resolverCombate(toCruz, divisaoAtual);
    }

    /**
     * Verifica se há itens na divisão e pergunta ao jogador se deseja pegá-los.
     *
     * @param divisao A divisão atual do Tó Cruz.
     */
    private void verificarItens(Divisao divisao) {
        if (divisao.getItensPresentes().getSize() > 0) {
            System.out.println("Itens encontrados na divisão:");
            for (Item item : divisao.getItensPresentes()) {
                System.out.println("- " + item.getTipo());
            }

            System.out.print("Deseja pegar todos os itens? (sim/não): ");
            String resposta = scanner.nextLine();
            if (resposta.equalsIgnoreCase("sim")) {
                while (divisao.getItensPresentes().getSize() > 0) {
                    Item item = divisao.getItensPresentes().getElementAt(0);
                    toCruz.adicionarAoInventario(item);
                    divisao.removerItem(item);
                }
            }
        }
    }
}
