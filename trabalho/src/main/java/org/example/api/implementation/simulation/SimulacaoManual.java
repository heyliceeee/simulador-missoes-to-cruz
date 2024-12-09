package org.example.api.implementation.simulation;

import org.example.api.implementation.models.*;
import org.example.api.implementation.services.CombateService;
import org.example.collections.implementation.LinkedList;

import java.util.NoSuchElementException;
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
            sugerirMelhorCaminhoEDisponibilidade(); // Sugere o melhor caminho
            String comando = obterComando().trim(); // Remove espaços extras

        // Verifica se o comando é válido
        if (comando.isEmpty()) {
            System.out.println("Nenhum comando fornecido. Tente novamente.");
            continue;
        }
    
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
    
            // Interação com o alvo ao final de cada turno
            interagirComAlvo(toCruz.getPosicaoAtual());
            
            // Verificar se a missão foi concluída
            if (toCruz.isAlvoConcluido()) {
                System.out.println("Missão concluída com sucesso! Tó Cruz capturou o alvo.");
                return;
            }
        }
        System.out.println("To Cruz foi derrotado! Simulação encerrada.");
    }


    /**
     * Sugerir o melhor caminho e localizar items em cada turno
     */
    private void sugerirMelhorCaminhoEDisponibilidade() {
        Divisao alvo = mapa.getAlvo().getDivisao();
        LinkedList<Divisao> caminhoParaAlvo = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(), alvo);

        System.out.println("Sugestão de trajeto até o alvo:");
        for (int i = 0; i < caminhoParaAlvo.getSize(); i++) {
            System.out.print(caminhoParaAlvo.getElementAt(i).getNomeDivisao());
            if (i < caminhoParaAlvo.getSize() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();

        Divisao kitMaisProximo = mapa.encontrarKitMaisProximo(toCruz.getPosicaoAtual());
        if (kitMaisProximo != null) {
            System.out.println("Kit de recuperação mais próximo: " + kitMaisProximo.getNomeDivisao());
        } else {
            System.out.println("Nenhum kit de recuperação disponível.");
        }
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
    try {
        System.out.println("Comandos disponíveis: mover, usar, atacar, sair");
        System.out.print("Digite seu comando: ");
        return scanner.nextLine(); // Aguarda a entrada do usuário
    } catch (NoSuchElementException e) {
        System.out.println("Erro ao receber comando. Por favor, tente novamente.");
        return ""; // Retorna string vazia para evitar falha
    }
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

    /**
 * Interage com o alvo se estiver na mesma divisão.
 * Se houver inimigos, avisa o jogador que deve eliminá-los antes.
 *
 * @param divisao A divisão onde o Tó Cruz está.
 */
private void interagirComAlvo(Divisao divisao) {
    Alvo alvo = mapa.getAlvo();
    if (alvo != null && alvo.getDivisao().equals(divisao)) {
        if (divisao.getInimigosPresentes().getSize() > 0) {
            System.out.println("O alvo está nesta sala, mas há inimigos! Elimine-os primeiro.");
        } else {
            System.out.println("O alvo foi resgatado com sucesso!");
            mapa.removerAlvo();
            toCruz.setAlvoConcluido(true);
        }
    }
}

}
