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

        // Obter entradas/saidas disponiveis
        ArrayUnorderedList<String> entradasSaidas = mapa.getEntradasSaidasNomes();
        if (entradasSaidas.isEmpty()) {
            System.err.println("Erro: Nao ha divisoes marcadas como entrada/saida no mapa.");
            return;
        }

        // Pedir ao jogador para escolher uma entrada
        IDivisao posicaoInicial = null;
        while (posicaoInicial == null) {
            System.out.println("Escolha uma das entradas disponiveis:");
            for (int i = 0; i < entradasSaidas.size(); i++) {
                System.out.println("- " + entradasSaidas.getElementAt(i));
            }
            System.out.print("Digite o nome da entrada: ");
            String escolha = scanner.nextLine().trim();

            try {
                posicaoInicial = mapa.getDivisaoPorNome(escolha);
                if (!entradasSaidas.contains(escolha)) {
                    System.out.println("Escolha invalida. Tente novamente.");
                    posicaoInicial = null;
                }
            } catch (RuntimeException e) {
                System.out.println("Divisao nao encontrada. Tente novamente.");
            }
        }

        toCruz.moverPara(posicaoInicial);
        caminhoPercorrido.addToRear(posicaoInicial);

        // Verificar se o objetivo foi definido
        if (divisaoObjetivo == null) {
            System.err.println("Erro: Divisao objetivo invalida.");
            return;
        }

        // Iniciar loop principal da simulacao
        while (toCruz.getVida() > 0) {
            mostrarEstado();
            mostrarConexoesAdjacentes(toCruz.getPosicaoAtual());

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
                default -> {
                    System.out.println("Comando invalido. Tente novamente.");
                    continue; // Reinicia o loop ao detectar comando invalido
                }
            }

            // Mover inimigos apos a acao do jogador
            try {
                mapa.moverInimigos(toCruz, combateService);
            } catch (ElementNotFoundException e) {
                System.err.println("Erro ao mover inimigos: " + e.getMessage());
            }

            // Mostrar o melhor caminho para o alvo
            ArrayUnorderedList<IDivisao> caminhoParaAlvo = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(),
                    divisaoObjetivo);
            System.out.print("Melhor caminho para o alvo: ");
            mostrarCaminho(caminhoParaAlvo);

            // Mostrar o melhor caminho para o kit de recuperacao mais proximo
            ArrayUnorderedList<IItem> kitsDisponiveis = mapa.getItensPorTipo("kit de vida");
            if (!kitsDisponiveis.isEmpty()) {
                IItem kitMaisProximo = encontrarKitMaisProximo(kitsDisponiveis);
                ArrayUnorderedList<IDivisao> caminhoParaKit = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(),
                        kitMaisProximo.getDivisao());
                System.out.print("Melhor caminho para o kit de recuperacao: ");
                mostrarCaminho(caminhoParaKit);
            } else {
                System.out.println("Nenhum kit de recuperacao disponivel no mapa.");
            }

            interagirComAlvo(toCruz.getPosicaoAtual());

            if (toCruz.isAlvoConcluido()) {
                System.out.println("Missao concluida com sucesso! To Cruz capturou o alvo.");
                return;
            }
        }

        System.out.println("To Cruz foi derrotado! Simulacao encerrada.");
    }

    private void mostrarConexoesAdjacentes(IDivisao divisaoAtual) throws ElementNotFoundException {
        ArrayUnorderedList<IDivisao> conexoes = mapa.obterConexoes(divisaoAtual);
        System.out.println("\n--- Divisoes Adjacentes ---");
        for (int i = 0; i < conexoes.size(); i++) {
            IDivisao conexao = conexoes.getElementAt(i);
            if (conexao != null) {
                int numInimigos = conexao.getInimigosPresentes().size();
                int numItens = conexao.getItensPresentes().size();
                System.out.println(conexao.getNomeDivisao() + " -> Inimigos: " + numInimigos + ", Itens: " + numItens);
            }
        }
        System.out.println("---------------------------");
    }

    private void mostrarCaminho(ArrayUnorderedList<IDivisao> caminho) {
        if (caminho == null || caminho.isEmpty()) {
            System.out.println("Nenhum caminho disponivel.");
            return;
        }

        ArrayUnorderedList<IDivisao> caminhoFiltrado = new ArrayUnorderedList<>();
        for (int i = 0; i < caminho.size(); i++) {
            IDivisao divisao = caminho.getElementAt(i);
            if (!mapa.getEntradasSaidasNomes().contains(divisao.getNomeDivisao())) {
                caminhoFiltrado.addToRear(divisao);
            }
        }

        if (caminhoFiltrado.isEmpty()) {
            System.out.println("Nenhum caminho disponivel.");
            return;
        }

        for (int i = 0; i < caminhoFiltrado.size(); i++) {
            System.out.print(caminhoFiltrado.getElementAt(i).getNomeDivisao());
            if (i < caminhoFiltrado.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    private IItem encontrarKitMaisProximo(ArrayUnorderedList<IItem> kits) throws ElementNotFoundException {
        // Assume que o mapa pode retornar uma lista de itens e as divisoes onde estao
        // localizados
        IItem kitMaisProximo = null;
        int menorDistancia = Integer.MAX_VALUE;

        for (int i = 0; i < kits.size(); i++) {
            IItem kit = kits.getElementAt(i);
            int distancia = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(), kit.getDivisao()).size();
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                kitMaisProximo = kit;
            }
        }
        return kitMaisProximo;
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
    
                // Se houver inimigos, Tó Cruz entrou na sala deles (cenário 1 ou 5)
                ArrayUnorderedList<IInimigo> inimigos = proximaDivisao.getInimigosPresentes();
                if (inimigos != null && !inimigos.isEmpty()) {
                    // Tó Cruz ataca primeiro (inimigoEntrouAgora = false)
                    combateService.resolverCombate(toCruz, proximaDivisao, false);
                }
    
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
        // Como o jogador acionou o combate (atacando ou entrando na sala), Tó Cruz ataca primeiro.
        // Portanto, inimigoEntrouAgora = false
        combateService.resolverCombate(toCruz, divisao, false);
    
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
            if ("sim".equals(resposta)) {
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
        } else if (mapa.getEntradasSaidasNomes().contains(divisao.getNomeDivisao()) && toCruz.isAlvoConcluido()) {
            System.out.println("🏆 Missao concluida com sucesso! To Cruz saiu do edificio com o alvo.");
            System.exit(0);
        } else if (mapa.getEntradasSaidasNomes().contains(divisao.getNomeDivisao()) && !toCruz.isAlvoConcluido()) {
            System.out.println("❌ Missao falhou! To Cruz saiu do edificio sem capturar o alvo.");
            System.exit(0);
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
