package org.example.api.implementation.simulation;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.*;
import org.example.api.implementation.models.MapaImpl;
import org.example.api.implementation.models.ToCruz;
import org.example.api.implementation.models.Predecessor;
import org.example.api.implementation.services.CombateServiceImpl;
import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.LinkedQueue;
import org.example.collections.implementation.LinkedStack;

/**
 * Gerencia a simulacao automatica para o To Cruz.
 */
public class SimulacaoAutomaticaImpl implements ISimulacaoAutomatica {

    private final IMapa mapa;
    private final ToCruz toCruz;
    private final ICombateService combateService;
    private final ArrayUnorderedList<IDivisao> caminhoPercorrido;
    private final ArrayUnorderedList<IInimigo> inimigosDerrotados;
    private final ArrayUnorderedList<IItem> itensColetados;

    public static String checkMark = "\u2705"; // ✅
    public static String crossedSwords = "\uD83D\uDDE1"; // 🗡
    public static String skull = "\uD83D\uDC80"; // 💀
    public static String target = "\uD83C\uDFAF"; // 🎯
    public static String trophy = "\uD83E\uDD47"; // 🥇
    public static String cowboy = "\uD83E\uDD20"; // 🤠
    public static String pin = "\uD83D\uDCCC"; // 📌
    public static String backpack = "\uD83C\uDF92"; // 🎒
    public static String life = "\uD83D\uDC99"; // 💙
    public static String pill = "\uD83D\uDC8A";// 💊
    public static String vest = "\uD83D\uDEE1\uFE0F"; // 🛡️

    /**
     * Construtor da Simulacao Automatica.
     *
     * @param mapa   O mapa do edificio.
     * @param toCruz O agente controlado pelo jogador.
     */
    public SimulacaoAutomaticaImpl(IMapa mapa, ToCruz toCruz) {
        if (mapa == null || toCruz == null) {
            throw new IllegalArgumentException("Mapa, To Cruz e CombateService nao podem ser nulos.");
        }

        this.mapa = mapa;
        this.toCruz = toCruz;
        this.combateService = new CombateServiceImpl();
        this.caminhoPercorrido = new ArrayUnorderedList<>();
        this.inimigosDerrotados = new ArrayUnorderedList<>();
        this.itensColetados = new ArrayUnorderedList<>();
    }

    /**
     * Executa a simulacao automatica para alcancar um objetivo.
     *
     * @param divisaoObjetivo A divisao onde o objetivo esta localizado.
     */
    @Override
    public void executar(IDivisao divisaoObjetivo) throws ElementNotFoundException {
        System.out.println("Inicio da simulacao automatica!");

        if (divisaoObjetivo == null) {
            System.err.println("Erro: Divisao objetivo nao encontrada.");
            throw new IllegalArgumentException("Erro: Divisao objetivo nao encontrada.");
        }

        ArrayUnorderedList<String> entradasSaidas = mapa.getEntradasSaidasNomes();
        if (entradasSaidas == null || entradasSaidas.isEmpty()) {
            System.err.println("Erro: Nenhuma entrada ou saida encontrada no mapa.");
            throw new IllegalArgumentException("Erro: Nenhuma entrada ou saida encontrada no mapa.");
        }

        IDivisao melhorEntrada = null;
        ArrayUnorderedList<IDivisao> melhorCaminhoParaObjetivo = null;
        ArrayUnorderedList<IDivisao> melhorCaminhoDeVolta = null;
        int maiorVidaRestante = Integer.MIN_VALUE;

        // Loop para encontrar o melhor caminho com base na vida restante
        for (int i = 0; i < entradasSaidas.size(); i++) {
            IDivisao entradaAtual = mapa.getDivisaoPorNome(entradasSaidas.getElementAt(i));
            if (entradaAtual == null)
                continue;

            ArrayUnorderedList<IDivisao> caminhoParaObjetivo = mapa.calcularMelhorCaminho(entradaAtual,
                    divisaoObjetivo);
            if (caminhoParaObjetivo == null || caminhoParaObjetivo.isEmpty())
                continue;

            ArrayUnorderedList<IDivisao> caminhoDeVolta = mapa.calcularMelhorCaminho(divisaoObjetivo, entradaAtual);
            if (caminhoDeVolta == null || caminhoDeVolta.isEmpty())
                continue;

            int vidaRestante = simularTrajeto(caminhoParaObjetivo, caminhoDeVolta);

            if (vidaRestante > maiorVidaRestante) {
                maiorVidaRestante = vidaRestante;
                melhorEntrada = entradaAtual;
                melhorCaminhoParaObjetivo = caminhoParaObjetivo;
                melhorCaminhoDeVolta = caminhoDeVolta;
            }
        }

        // Se nenhum caminho ideal foi encontrado, tentar alternativas
        if (melhorEntrada == null || melhorCaminhoParaObjetivo == null || melhorCaminhoDeVolta == null) {
            System.err.println("Nenhum trajeto ideal encontrado. Selecionando o primeiro trajeto viavel...");
            for (int i = 0; i < entradasSaidas.size(); i++) {
                IDivisao entradaAlternativa = mapa.getDivisaoPorNome(entradasSaidas.getElementAt(i));
                if (entradaAlternativa != null) {
                    melhorCaminhoParaObjetivo = mapa.calcularMelhorCaminho(entradaAlternativa, divisaoObjetivo);
                    melhorCaminhoDeVolta = mapa.calcularMelhorCaminho(divisaoObjetivo, entradaAlternativa);

                    if (melhorCaminhoParaObjetivo != null && !melhorCaminhoParaObjetivo.isEmpty() &&
                            melhorCaminhoDeVolta != null && !melhorCaminhoDeVolta.isEmpty()) {
                        melhorEntrada = entradaAlternativa;
                        break;
                    }
                }
            }

            // Caso ainda nao encontre uma entrada valida
            if (melhorEntrada == null) {
                System.err.println("Erro: Nenhuma entrada viavel encontrada. Forcando inicio pela primeira entrada.");
                melhorEntrada = mapa.getDivisaoPorNome(entradasSaidas.getElementAt(0));
            }
        }

        // Adiciona o caminho ao registro
        if (melhorEntrada != null && melhorCaminhoParaObjetivo != null) {
            for (int i = 0; i < melhorCaminhoParaObjetivo.size(); i++) {
                IDivisao divisao = melhorCaminhoParaObjetivo.getElementAt(i);
                caminhoPercorrido.addToRear(divisao); // Adiciona ao caminho percorrido
            }
        }

        // Movendo To Cruz
        System.out.println("Movendo-se para a melhor entrada: " + melhorEntrada.getNomeDivisao());
        toCruz.moverPara(melhorEntrada);

        for (int i = 0; i < melhorCaminhoParaObjetivo.size(); i++) {
            IDivisao divisao = melhorCaminhoParaObjetivo.getElementAt(i);
            moverParaDivisao(divisao);

            if (toCruz.getVida() <= 0) {
                System.err.println("💀 To Cruz foi derrotado!");
                return;
            }
        }

        System.out.println("🏁 To Cruz alcancou o objetivo!");

        for (int i = 0; i < melhorCaminhoDeVolta.size(); i++) {
            IDivisao divisao = melhorCaminhoDeVolta.getElementAt(i);
            moverParaDivisao(divisao);

            if (toCruz.getVida() <= 0) {
                System.err.println("💀 To Cruz foi derrotado no retorno!");
                return;
            }
        }

        System.out.println("🏆 Missao concluida com sucesso! To Cruz retornou com o alvo.");
    }

    /**
     * Simula o trajeto de ida e volta, considerando o impacto de inimigos e itens,
     * para calcular a vida restante de To Cruz.
     */
    public int simularTrajeto(ArrayUnorderedList<IDivisao> caminhoParaObjetivo,
                              ArrayUnorderedList<IDivisao> caminhoDeVolta) {
        int vidaSimulada = toCruz.getVida();

        // Simular impacto do caminho para o objetivo
        for (int i = 0; i < caminhoParaObjetivo.size(); i++) {
            IDivisao divisao = caminhoParaObjetivo.getElementAt(i);
            vidaSimulada -= calcularDanoInimigos(divisao);
            vidaSimulada += calcularRecuperacaoItens(divisao);
            if (vidaSimulada <= 0)
                return Integer.MIN_VALUE; // To Cruz nao sobrevive
        }

        // Simular impacto do caminho de volta
        for (int i = 0; i < caminhoDeVolta.size(); i++) {
            IDivisao divisao = caminhoDeVolta.getElementAt(i);
            vidaSimulada -= calcularDanoInimigos(divisao);
            vidaSimulada += calcularRecuperacaoItens(divisao);
            if (vidaSimulada <= 0)
                return Integer.MIN_VALUE; // To Cruz nao sobrevive
        }

        return vidaSimulada;
    }

    /**
     * Calcula o dano causado pelos inimigos em uma divisao.
     */
    private int calcularDanoInimigos(IDivisao divisao) {
        int dano = 0;
        ArrayUnorderedList<IInimigo> inimigos = divisao.getInimigosPresentes();
        if (inimigos != null) {
            for (int i = 0; i < inimigos.size(); i++) {
                dano += inimigos.getElementAt(i).getPoder();
            }
        }
        return dano;
    }

    /**
     * Calcula a recuperacao de vida proporcionada pelos itens em uma divisao.
     */
    private int calcularRecuperacaoItens(IDivisao divisao) {
        int recuperacao = 0;
        ArrayUnorderedList<IItem> itens = divisao.getItensPresentes();
        if (itens != null) {
            for (int i = 0; i < itens.size(); i++) {
                if ("kit de vida".equalsIgnoreCase(itens.getElementAt(i).getTipo())) {
                    recuperacao += itens.getElementAt(i).getPontos();
                }
            }
        }
        return recuperacao;
    }

    /**
     * Move To Cruz para a divisao especificada, resolve combates e coleta itens.
     *
     * @param divisao Divisao para onde To Cruz deve se mover.
     * @throws ElementNotFoundException
     */
    public void moverParaDivisao(IDivisao divisao) throws ElementNotFoundException {
        if (divisao == null) {
            throw new IllegalArgumentException("Erro: Tentativa de mover para uma divisao nula.");
        }
    
        toCruz.moverPara(divisao);
        caminhoPercorrido.addToRear(divisao);
        System.out.println("🤠 To Cruz moveu-se para a divisao: " + divisao.getNomeDivisao());
    
        ArrayUnorderedList<IInimigo> inimigos = divisao.getInimigosPresentes();
        if (inimigos != null && !inimigos.isEmpty()) {
            // Tó Cruz entrou na sala com inimigos: Tó Cruz ataca primeiro
            combateService.resolverCombate(toCruz, divisao, false); 
        }
    
        // Coletar itens se houver
        ArrayUnorderedList<IItem> itens = divisao.getItensPresentes();
        if (itens != null && !itens.isEmpty()) {
            System.out.println(backpack + " Itens encontrados na divisao: " + divisao.getNomeDivisao());
            while (!itens.isEmpty()) {
                try {
                    IItem item = itens.removeFirst();
                    toCruz.adicionarAoInventario(item);
                    itensColetados.addToRear(item);
                    System.out.println(checkMark + " Item coletado: " + item.getTipo());
                } catch (EmptyCollectionException e) {
                    System.err.println("Erro ao coletar item: " + e.getMessage());
                    break;
                }
            }
        }
    }
    

    /**
     * Encontra o caminho para a divisao de saida mais proxima usando BFS.
     *
     * @return Lista de divisoes representando o caminho mais curto para a saida.
     */
    public ArrayUnorderedList<IDivisao> encontrarCaminhoParaSaidaMaisProxima() {
        ArrayUnorderedList<String> entradasSaidas = mapa.getEntradasSaidasNomes();
        if (entradasSaidas.isEmpty()) {
            System.err.println("Erro: Nenhuma divisao de saida definida no mapa.");
            return null;
        }

        LinkedQueue<IDivisao> fila = new LinkedQueue<>();
        ArrayUnorderedList<IDivisao> visitados = new ArrayUnorderedList<>();
        ArrayUnorderedList<Predecessor> predecessores = new ArrayUnorderedList<>();

        IDivisao posicaoInicial = toCruz.getPosicaoAtual();
        if (posicaoInicial == null) {
            System.err.println("Erro: Posicao inicial de To Cruz e nula.");
            return null;
        }

        fila.enqueue(posicaoInicial);
        visitados.addToRear(posicaoInicial);
        predecessores.addToRear(new Predecessor(posicaoInicial, null));

        while (!fila.isEmpty()) {
            IDivisao atual = fila.dequeue();

            // Verifica se a divisao atual e uma das saidas
            for (int i = 0; i < entradasSaidas.size(); i++) {
                String nomeSaida = entradasSaidas.getElementAt(i);
                if (atual.getNomeDivisao().equals(nomeSaida)) {
                    System.out.println("Divisao de saida encontrada: " + atual.getNomeDivisao());
                    // Reconstruir o caminho ate esta divisao
                    ArrayUnorderedList<IDivisao> caminho = new ArrayUnorderedList<>();
                    IDivisao passo = atual;
                    while (passo != null) {
                        caminho.addToRear(passo);
                        passo = getPredecessor(predecessores, passo.getNomeDivisao());
                    }
                    return inverterLista(caminho);
                }
            }

            ArrayUnorderedList<IDivisao> conexoes = mapa.obterConexoes(atual);
            if (conexoes == null || conexoes.isEmpty()) {
                System.out.println("Aviso: Divisao " + atual.getNomeDivisao() + " nao possui conexoes.");
                continue;
            }

            for (int i = 0; i < conexoes.size(); i++) {
                IDivisao vizinho = conexoes.getElementAt(i);
                if (vizinho == null)
                    continue;

                if (!visitados.contains(vizinho) && mapa.podeMover(atual.getNomeDivisao(), vizinho.getNomeDivisao())) {
                    visitados.addToRear(vizinho);
                    fila.enqueue(vizinho);
                    predecessores.addToRear(new Predecessor(vizinho, atual));
                }
            }
        }

        System.err.println("Erro: Nenhuma divisao de saida acessivel foi encontrada.");
        return null;
    }

    /**
     * Reconstroi o caminho percorrido a partir dos predecessores.
     *
     * @param predecessores Lista de predecessores para cada divisao.
     * @param objetivo      Divisao objetivo que foi encontrada.
     */
    /**
     * private void reconstructPath(ArrayUnorderedList<Predecessor> predecessores,
     * Divisao objetivo, ArrayUnorderedList<Divisao> caminho) {
     * Divisao passo = objetivo;
     * while (passo != null) {
     * caminho.addToRear(passo);
     * passo = getPredecessor(predecessores, passo.getNomeDivisao());
     * }
     * }
     */
    /**
     * private void reconstructPath(ArrayUnorderedList<Predecessor> predecessores,
     * Divisao objetivo, ArrayUnorderedList<Divisao> caminho) {
     * Divisao passo = objetivo;
     * while (passo != null) {
     * caminho.addToRear(passo);
     * passo = getPredecessor(predecessores, passo.getNomeDivisao());
     * }
     * }
     */

    /**
     * Recupera o predecessor de uma divisao a partir da lista de predecessores.
     *
     * @param predecessores Lista de predecessores.
     * @param nomeDivisao   Nome da divisao atual.
     * @return Divisao predecessora ou null se nao encontrada.
     */
    private IDivisao getPredecessor(ArrayUnorderedList<Predecessor> predecessores, String nomeDivisao) {
        for (int i = 0; i < predecessores.size(); i++) {
            Predecessor p = predecessores.getElementAt(i);
            if (p != null && p.getAtual().getNomeDivisao().equals(nomeDivisao.trim())) {
                return p.getPredecessor();
            }
        }
        return null;
    }

    /**
     * Inverte uma lista de divisoes para obter a ordem correta do caminho.
     *
     * @param lista Lista original.
     * @return Lista invertida.
     */
    private ArrayUnorderedList<IDivisao> inverterLista(ArrayUnorderedList<IDivisao> lista) {
        LinkedStack<IDivisao> pilha = new LinkedStack<>();
        ArrayUnorderedList<IDivisao> invertida = new ArrayUnorderedList<>();
        for (int i = 0; i < lista.size(); i++) {
            IDivisao divisao = lista.getElementAt(i);
            if (divisao != null) {
                pilha.push(divisao);
            }
        }
        while (!pilha.isEmpty()) {
            try {
                invertida.addToRear(pilha.pop());
            } catch (EmptyCollectionException e) {
                System.err.println("Erro ao inverter a lista: " + e.getMessage());
            }
        }
        return invertida;
    }

    /**
     * Verifica e executa o trajeto de volta para a divisao de saida mais proxima.
     * 
     * @throws ElementNotFoundException
     */
    /*
     * private void verificarTrajetoDeVolta() throws ElementNotFoundException {
     * ArrayUnorderedList<Divisao> caminhoDeVolta =
     * encontrarCaminhoParaSaidaMaisProxima();
     * 
     * if (caminhoDeVolta == null || caminhoDeVolta.isEmpty()) {
     * System.err.println("Erro: Caminho de volta nao encontrado.");
     * return;
     * }
     * 
     * System.out.println("🎯 Divisao de saida encontrada: "
     * + caminhoDeVolta.getElementAt(caminhoDeVolta.size() - 1).getNomeDivisao());
     * 
     * for (int i = 0; i < caminhoDeVolta.size(); i++) {
     * Divisao divisao = caminhoDeVolta.getElementAt(i);
     * if (divisao == null)
     * continue;
     * 
     * // Combate com inimigos na divisao de volta
     * if (divisao.getInimigosPresentes() != null &&
     * !divisao.getInimigosPresentes().isEmpty()) {
     * System.out.println("⚔️ Combate iniciado na divisao: " +
     * divisao.getNomeDivisao());
     * combateService.resolverCombate(toCruz, divisao);
     * 
     * // Verificar se To Cruz foi derrotado
     * if (toCruz.getVida() <= 0) {
     * System.err.println("💀 To Cruz foi derrotado durante o retorno!");
     * mostrarMapaInterativo(toCruz, divisao, false);
     * return;
     * }
     * }
     * 
     * // Mover para a divisao
     * toCruz.moverPara(divisao);
     * caminhoPercorrido.addToRear(divisao);
     * mostrarMapaInterativo(toCruz, divisao, true);
     * }
     * 
     * System.out.
     * println("🏆 Missao concluida com sucesso! To Cruz retornou com o alvo.");
     * }
     */

    @Override
    public int getVidaRestante() {
        return toCruz.getVida();
    }

    @Override
    public String getStatus() {
        return toCruz.getVida() > 0 ? "SUCESSO" : "FALHA";
    }

    @Override
    public IDivisao getDivisaoFinal() {
        return toCruz.getPosicaoAtual();
    }

    @Override
    public ArrayUnorderedList<IDivisao> getCaminhoPercorrido() {
        return caminhoPercorrido;
    }

    public ArrayUnorderedList<IInimigo> getInimigosDerrotados() {
        return inimigosDerrotados;
    }

    public ArrayUnorderedList<IItem> getItensColetados() {
        return itensColetados;
    }

    public ArrayUnorderedList<String> getCaminhoPercorridoNomes() {
        ArrayUnorderedList<String> nomes = new ArrayUnorderedList<>();
        if (caminhoPercorrido != null) {
            for (int i = 0; i < caminhoPercorrido.size(); i++) {
                IDivisao divisao = caminhoPercorrido.getElementAt(i);
                if (divisao != null) {
                    nomes.addToRear(divisao.getNomeDivisao());
                }
            }
        }
        return nomes;
    }

    public void mostrarMapaInterativo(ToCruz toCruz, IDivisao divisaoAtual, boolean sucesso) {
        System.out.println("===== MAPA ATUAL =====");
        ArrayUnorderedList<IDivisao> divisoes = mapa.getDivisoes();

        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao divisao = divisoes.getElementAt(i);
            if (divisao == null)
                continue;

            // Mostrar o icone de To Cruz na posicao atual
            if (divisao.equals(divisaoAtual)) {
                String icone = sucesso ? cowboy : skull;
                System.out.print(icone + " " + divisao.getNomeDivisao() + " <- To Cruz esta aqui");
            } else {
                System.out.print("   " + divisao.getNomeDivisao());
            }
            System.out.println();
        }
        System.out.println("=======================");
    }
}
