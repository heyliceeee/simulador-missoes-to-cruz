package org.example.api.implementation.simulation;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.*;
import org.example.api.implementation.services.CombateServiceImpl;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.implementation.ArrayUnorderedList;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Gerencia a simulacao manual, onde o jogador toma as decisoes.
 */
public class SimulacaoManualImpl implements ISimulacaoManual {

    private final IMapa mapa;
    private final ToCruz toCruz;
    private final Scanner scanner;
    private final CombateServiceImpl combateService;
    private final ArrayUnorderedList<IItem> itensColetados; // Alterado para ArrayUnorderedList
    private final ArrayUnorderedList<IInimigo> inimigosDerrotados;
    private final ArrayUnorderedList<IDivisao> caminhoPercorrido;

    /**
     * Construtor da Simulacao Manual.
     *
     * @param mapa   O mapa do edificio.
     * @param toCruz O agente controlado pelo jogador.
     */
    public SimulacaoManualImpl(IMapa mapa, ToCruz toCruz) {
        if (mapa == null || toCruz == null) {
            throw new IllegalArgumentException("Mapa e To Cruz nao podem ser nulos.");
        }
        this.mapa = mapa;
        this.toCruz = toCruz;
        this.scanner = new Scanner(System.in);
        this.combateService = new CombateServiceImpl();
        this.itensColetados = new ArrayUnorderedList<>(); 
        this.inimigosDerrotados = new ArrayUnorderedList<>();
        this.caminhoPercorrido = new ArrayUnorderedList<>();
    }

    /**
     * Executa o loop principal da simulacao manual.
     */
    @Override
    public void executar(IDivisao divisaoObjetivo) throws ElementNotFoundException {
        System.out.println("Inicio da simulacao manual!");
        if (divisaoObjetivo == null) {
            System.err.println("Erro: Divisao objetivo invalida.");
            return;
        }

        IDivisao posicaoInicial = toCruz.getPosicaoAtual();
        if (posicaoInicial == null) {
            System.err.println("Erro: Posicao inicial de To Cruz e nula.");
            return;
        }

        caminhoPercorrido.addToRear(posicaoInicial);

        while (toCruz.getVida() > 0) {
            mostrarEstado();
            sugerirMelhorCaminhoEDisponibilidade(); // Sugere o melhor caminho
            String comando = obterComando();

            if (comando.isEmpty()) {
                System.out.println("Nenhum comando fornecido. Tente novamente.");
                continue;
            }

            switch (comando.toLowerCase()) {
                case "mover" -> mover();
                case "usar" -> toCruz.usarKitDeVida();
                case "atacar" -> atacar();
                case "sair" -> {
                    System.out.println("Simulacao terminada.");
                    return;
                }
                default -> System.out.println("Comando invalido. Tente novamente.");
            }

            interagirComAlvo(toCruz.getPosicaoAtual());

            if (toCruz.isAlvoConcluido()) {
                System.out.println("Missao concluida com sucesso! To Cruz capturou o alvo.");
                return;
            }
        }
        System.out.println("To Cruz foi derrotado! Simulacao encerrada.");
    }

    /**
     * obter a vida restante do to cruz
     * 
     * @return
     */
    @Override
    public int getVidaRestante() {
        return toCruz.getVida();
    }

    /**
     * saber se passou ou falhou a missao, ou seja, se o to cruz tem vida, salvou o
     * alvo e esta numa divisao de saida
     * 
     * @return sucesso ou falha
     */
    @Override
    public String getStatus() {
        if (toCruz.getVida() > 0 && toCruz.isAlvoConcluido()) {

            for (int i = 0; i < mapa.getEntradasSaidasNomes().size(); i++) {
                if (getDivisaoFinal().equals(mapa.getEntradasSaidasNomes().getElementAt(i))) {
                    return "SUCESSO";
                }
            }
        }

        return "FALHA";
    }

    /**
     * nome da divisao atual do to cruz
     * 
     * @return nome da divisao
     */
    @Override
    public IDivisao getDivisaoFinal() {
        return toCruz.getPosicaoAtual();
    }

    public ArrayUnorderedList<IItem> getItensColetados() {
        return itensColetados;
    }

    public ArrayUnorderedList<IInimigo> getInimigosDerrotados() {
        return inimigosDerrotados;
    }

    public ArrayUnorderedList<IDivisao> getCaminhoPercorrido() {
        return caminhoPercorrido;
    }

    /**
     * Sugerir o melhor caminho e localizar items em cada turno
     */
    private void sugerirMelhorCaminhoEDisponibilidade() throws ElementNotFoundException {
        IDivisao alvo = mapa.getAlvo().getDivisao();
        ArrayUnorderedList<IDivisao> caminhoParaAlvo = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(), alvo);

        System.out.println("Sugestao de trajeto ate o alvo:");
        for (int i = 0; i < caminhoParaAlvo.size(); i++) {
            System.out.print(caminhoParaAlvo.getElementAt(i).getNomeDivisao());
            if (i < caminhoParaAlvo.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();

        IDivisao kitMaisProximo = mapa.encontrarKitMaisProximo(toCruz.getPosicaoAtual());
        if (kitMaisProximo != null) {
            System.out.println("Kit de recuperacao mais proximo: " + kitMaisProximo.getNomeDivisao());
        } else {
            System.out.println("Nenhum kit de recuperacao disponivel.");
        }
    }

    /**
     * Mostra o estado atual do jogo.
     */
    private void mostrarEstado() {
        System.out.println("\n--- Estado Atual ---");
        IDivisao posicao = toCruz.getPosicaoAtual();
        String nomeDivisao = (posicao != null) ? posicao.getNomeDivisao() : "Nenhuma";
        System.out.println("Posicao: " + nomeDivisao);
        System.out.println("Vida: " + toCruz.getVida());
        System.out.println("Inventario: " + toCruz.getInventario().size() + " itens");
        System.out.println("---------------------");
    }

    /**
     * Obtem um comando do jogador.
     *
     * @return O comando inserido pelo jogador.
     */
    private String obterComando() {
        try {
            System.out.println("Comandos disponiveis: mover, usar, atacar, sair");
            System.out.print("Digite seu comando: ");
            return scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.err.println("Erro ao receber comando: " + e.getMessage());
            return "";
        }
    }

    /**
     * Gerencia o movimento do To Cruz.
     */
    private void mover() throws ElementNotFoundException {
        System.out.print("Digite o nome da divisao para onde deseja mover: ");
        String novaDivisao = scanner.nextLine().trim();

        if (novaDivisao.isEmpty()) {
            System.out.println("Divisao invalida.");
            return;
        }

        if (mapa.podeMover(toCruz.getPosicaoAtual().getNomeDivisao(), novaDivisao)) {
            IDivisao proximaDivisao = mapa.getDivisaoPorNome(novaDivisao);
            if (proximaDivisao != null) {
                toCruz.moverPara(proximaDivisao);
                caminhoPercorrido.addToRear(proximaDivisao);
                verificarItens(proximaDivisao);
                combater(proximaDivisao);
            } else {
                System.err.println("Erro: Divisao '" + novaDivisao + "' nao encontrada.");
            }
        } else {
            System.out.println("Movimento invalido! Divisoes nao conectadas.");
        }
    }

    /**
     * Realiza um ataque contra os inimigos na divisao atual.
     */
    private void atacar() throws ElementNotFoundException {
        IDivisao divisaoAtual = toCruz.getPosicaoAtual();
        if (divisaoAtual == null) {
            System.err.println("Erro: Divisao atual e nula.");
            return;
        }
        combater(divisaoAtual);
    }

    private void combater(IDivisao divisao) throws ElementNotFoundException {
        combateService.resolverCombate(toCruz, divisao);
        ArrayUnorderedList<IInimigo> inimigos = divisao.getInimigosPresentes();
        while (inimigos != null && !inimigos.isEmpty()) {
            try {
                IInimigo inimigo = inimigos.removeFirst();
                inimigosDerrotados.addToRear(inimigo);
                System.out.println("To Cruz derrotou: " + inimigo.getNome());
            } catch (EmptyCollectionException e) {
                System.err.println("Erro ao remover inimigo: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * Verifica se ha itens na divisao e pergunta ao jogador se deseja pega-los.
     *
     * @param divisao A divisao atual do To Cruz.
     */
    private void verificarItens(IDivisao divisao) throws ElementNotFoundException {
        ArrayUnorderedList<IItem> itens = divisao.getItensPresentes();
        if (itens != null && !itens.isEmpty()) {
            System.out.println("Itens encontrados na divisao:");
            for (int i = 0; i < itens.size(); i++) {
                IItem item = itens.getElementAt(i);
                if (item != null) {
                    System.out.println("- " + item.getTipo());
                }
            }

            System.out.print("Deseja pegar todos os itens? (sim/nao): ");
            String resposta = scanner.nextLine().trim();
            if ("sim".equalsIgnoreCase(resposta)) {
                while (!itens.isEmpty()) {
                    try {
                        IItem item = itens.removeFirst();
                        itensColetados.addToRear(item);
                        toCruz.adicionarAoInventario(item);
                        System.out.println("To Cruz coletou: " + item.getTipo());
                    } catch (EmptyCollectionException e) {
                        System.err.println("Erro ao coletar item: " + e.getMessage());
                        break;
                    }
                }
            }
        }
    }

    /**
     * Interage com o alvo se estiver na mesma divisao.
     * Se houver inimigos, avisa o jogador que deve elimina-los antes.
     *
     * @param divisao A divisao onde o To Cruz esta.
     */
    private void interagirComAlvo(IDivisao divisao) {
        IAlvo alvo = mapa.getAlvo();
        if (alvo != null && divisao.equals(alvo.getDivisao())) {
            if (divisao.getInimigosPresentes() != null && !divisao.getInimigosPresentes().isEmpty()) {
                System.out.println("O alvo esta nesta sala, mas ha inimigos! Elimine-os primeiro.");
            } else {
                System.out.println("O alvo foi resgatado com sucesso!");
                mapa.removerAlvo();
                toCruz.setAlvoConcluido(true);
            }
        }
    }

    @Override
    public ArrayUnorderedList<String> getCaminhoPercorridoNomes() {
        ArrayUnorderedList<String> nomes = new ArrayUnorderedList<>();
        for (int i = 0; i < caminhoPercorrido.size(); i++) {
            IDivisao divisao = caminhoPercorrido.getElementAt(i);
            if (divisao != null) {
                nomes.addToRear(divisao.getNomeDivisao());
            }
        }
        return nomes;
    }
}
