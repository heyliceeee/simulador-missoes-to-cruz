package org.example.api.implementation.simulation;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.ICombateService;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IInimigo;
import org.example.api.implementation.interfaces.IItem;
import org.example.api.implementation.interfaces.IMapa;
import org.example.api.implementation.interfaces.ISimulacaoAutomatica;
import org.example.api.implementation.models.Predecessor;
import org.example.api.implementation.models.ToCruz;
import org.example.api.implementation.services.CombateServiceImpl;
import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.LinkedQueue;
import org.example.collections.implementation.LinkedStack;

/**
 * Classe responsável por gerenciar a simulação automática da movimentação do personagem
 * To Cruz através de um mapa (edifício), desde uma entrada até um objetivo, e em seguida
 * retornando para uma saída. Durante a simulação, To Cruz pode enfrentar inimigos, coletar
 * itens, e sofrer danos ou recuperar vida, dependendo do caminho percorrido.
 * 
 * Esta classe implementa a interface {@link ISimulacaoAutomatica}, fornecendo uma execução 
 * autônoma e otimizada para atingir o objetivo.
 *
 * Funcionalidades principais:
 * - Determinar e simular o melhor caminho de ida e volta baseado na vida restante de To Cruz.
 * - Resolver combates automaticamente ao entrar em salas com inimigos.
 * - Coletar itens ao passar por divisões que os contenham.
 * - Exibir informações sobre o percurso, vida restante, inimigos e itens coletados.
 */
public class SimulacaoAutomaticaImpl implements ISimulacaoAutomatica {

    /**
     * Mapa (estrutura do edifício) por onde To Cruz irá se movimentar.
     */
    private final IMapa mapa;

    /**
     * Agente controlado pelo jogador (To Cruz), cujo percurso será simulado.
     */
    private final ToCruz toCruz;

    /**
     * Serviço responsável por resolver combates entre To Cruz e inimigos.
     */
    private final ICombateService combateService;

    /**
     * Lista não ordenada que armazena a sequência de divisões visitadas ao longo da simulação.
     */
    private final ArrayUnorderedList<IDivisao> caminhoPercorrido;

    /**
     * Lista não ordenada de inimigos derrotados durante o percurso.
     * (Atualmente, o código não popula explicitamente esta lista, mas ela está disponível
     * caso futuramente se queira registrar os inimigos derrotados.)
     */
    private final ArrayUnorderedList<IInimigo> inimigosDerrotados;

    /**
     * Lista não ordenada de itens coletados durante a simulação.
     */
    private final ArrayUnorderedList<IItem> itensColetados;

    // Emojis utilizados para enriquecer a saída no console.
    public static String checkMark = "\u2705"; // ✅
    public static String crossedSwords = "\uD83D\uDDE1"; // 🗡
    public static String skull = "\uD83D\uDC80"; // 💀
    public static String target = "\uD83C\uDFAF"; // 🎯
    public static String trophy = "\uD83E\uDD47"; // 🥇
    public static String cowboy = "\uD83E\uDD20"; // 🤠
    public static String pin = "\uD83D\uDCCC"; // 📌
    public static String backpack = "\uD83C\uDF92"; // 🎒
    public static String life = "\uD83D\uDC99"; // 💙
    public static String pill = "\uD83D\uDC8A"; // 💊
    public static String vest = "\uD83D\uDEE1\uFE0F"; // 🛡️

    /**
     * Construtor da simulação automática.
     *
     * @param mapa   O mapa do edifício, representando as divisões e conexões entre elas.
     * @param toCruz O agente (To Cruz) que irá se deslocar pelo mapa.
     * 
     * @throws IllegalArgumentException caso o mapa ou o toCruz sejam nulos.
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
     * Executa a simulação automática para alcançar um objetivo específico dentro do mapa.
     * A simulação tentará encontrar o melhor caminho de ida e volta, tendo como critério 
     * principal a vida restante de To Cruz após completar a missão (ir até a divisão 
     * objetivo e retornar a uma saída).
     *
     * Caso não seja encontrado o melhor caminho, a simulação tentará um trajeto viável.
     *
     * @param divisaoObjetivo A divisão do mapa onde se encontra o objetivo da missão.
     * 
     * @throws ElementNotFoundException se ocorrer algum problema ao acessar divisões ou itens.
     * @throws IllegalArgumentException se a divisão objetivo não for válida.
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

        // Tenta encontrar o caminho de ida e volta que maximize a vida restante.
        for (int i = 0; i < entradasSaidas.size(); i++) {
            IDivisao entradaAtual = mapa.getDivisaoPorNome(entradasSaidas.getElementAt(i));
            if (entradaAtual == null) continue;

            ArrayUnorderedList<IDivisao> caminhoParaObjetivo = mapa.calcularMelhorCaminho(entradaAtual, divisaoObjetivo);
            if (caminhoParaObjetivo == null || caminhoParaObjetivo.isEmpty()) continue;

            ArrayUnorderedList<IDivisao> caminhoDeVolta = mapa.calcularMelhorCaminho(divisaoObjetivo, entradaAtual);
            if (caminhoDeVolta == null || caminhoDeVolta.isEmpty()) continue;

            int vidaRestante = simularTrajeto(caminhoParaObjetivo, caminhoDeVolta);
            if (vidaRestante > maiorVidaRestante) {
                maiorVidaRestante = vidaRestante;
                melhorEntrada = entradaAtual;
                melhorCaminhoParaObjetivo = caminhoParaObjetivo;
                melhorCaminhoDeVolta = caminhoDeVolta;
            }
        }

        // Se não encontrou um caminho ideal, tenta um caminho viável.
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

            if (melhorEntrada == null) {
                System.err.println("Erro: Nenhuma entrada viavel encontrada. Forcando inicio pela primeira entrada.");
                melhorEntrada = mapa.getDivisaoPorNome(entradasSaidas.getElementAt(0));
            }
        }

        // Registra o caminho a ser percorrido
        if (melhorEntrada != null && melhorCaminhoParaObjetivo != null) {
            for (int i = 0; i < melhorCaminhoParaObjetivo.size(); i++) {
                IDivisao divisao = melhorCaminhoParaObjetivo.getElementAt(i);
                caminhoPercorrido.addToRear(divisao);
            }
        }

        // Executa a simulação do deslocamento, combate e coleta de itens.
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

        // Retorno ao ponto de saída
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
     * Simula o trajeto de ida e volta, avaliando o impacto de inimigos e itens
     * sobre a vida de To Cruz, sem efetivamente movê-lo. Esta simulação é utilizada
     * para escolher o melhor caminho antes de executá-lo.
     *
     * @param caminhoParaObjetivo Lista de divisões representando o caminho de entrada 
     *                            até o objetivo.
     * @param caminhoDeVolta      Lista de divisões representando o caminho de volta
     *                            até a saída.
     * @return A quantidade de vida restante simulada após percorrer ida e volta.
     *         Retorna Integer.MIN_VALUE se To Cruz não sobreviver na simulação.
     */
    public int simularTrajeto(ArrayUnorderedList<IDivisao> caminhoParaObjetivo,
                              ArrayUnorderedList<IDivisao> caminhoDeVolta) {
        int vidaSimulada = toCruz.getVida();

        // Simula impacto na ida
        for (int i = 0; i < caminhoParaObjetivo.size(); i++) {
            IDivisao divisao = caminhoParaObjetivo.getElementAt(i);
            vidaSimulada -= calcularDanoInimigos(divisao);
            vidaSimulada += calcularRecuperacaoItens(divisao);
            if (vidaSimulada <= 0)
                return Integer.MIN_VALUE; // Não sobrevive
        }

        // Simula impacto na volta
        for (int i = 0; i < caminhoDeVolta.size(); i++) {
            IDivisao divisao = caminhoDeVolta.getElementAt(i);
            vidaSimulada -= calcularDanoInimigos(divisao);
            vidaSimulada += calcularRecuperacaoItens(divisao);
            if (vidaSimulada <= 0)
                return Integer.MIN_VALUE; // Não sobrevive
        }

        return vidaSimulada;
    }

    /**
     * Calcula o dano total causado pelos inimigos presentes em uma determinada divisão.
     *
     * @param divisao Divisão cuja presença de inimigos será avaliada.
     * @return Valor do dano causado pelos inimigos desta divisão.
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
     * Calcula a recuperação de vida proporcionada pelos itens presentes em uma divisão.
     * Apenas itens do tipo "kit de vida" são considerados para recuperar pontos de vida.
     *
     * @param divisao Divisão onde serão verificados os itens.
     * @return Pontos de vida recuperados pelos itens nesta divisão.
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
     * Move To Cruz para a divisão especificada, resolvendo automaticamente combates 
     * caso haja inimigos, e coletando itens disponíveis. Esta ação é efetiva e altera 
     * o estado real do jogo/simulação.
     *
     * @param divisao Divisão para a qual To Cruz deverá se mover.
     * 
     * @throws ElementNotFoundException se ocorrer um problema ao acessar dados da divisão.
     * @throws IllegalArgumentException se a divisão for nula.
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
            // Resolver combate: To Cruz ataca primeiro
            combateService.resolverCombate(toCruz, divisao, false);
        }

        // Coleta de itens
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
     * Encontra o caminho para a saída mais próxima da posição atual de To Cruz, 
     * usando uma busca em largura (BFS). Retorna a lista de divisões que formam 
     * o caminho mais curto até uma divisão de saída.
     *
     * @return Uma lista não ordenada com o caminho para a saída mais próxima, ou null 
     *         se não for encontrada uma saída acessível.
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

            // Verifica se a divisão atual é uma saída
            for (int i = 0; i < entradasSaidas.size(); i++) {
                String nomeSaida = entradasSaidas.getElementAt(i);
                if (atual.getNomeDivisao().equals(nomeSaida)) {
                    System.out.println("Divisao de saida encontrada: " + atual.getNomeDivisao());

                    // Reconstrói o caminho até esta divisão
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
                if (vizinho == null) continue;

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
     * Recupera o predecessor de uma determinada divisão a partir de uma lista de predecessores.
     *
     * @param predecessores Lista de objetos Predecessor, que vinculam cada divisão a seu antecessor.
     * @param nomeDivisao   Nome da divisão cuja predecessora se deseja encontrar.
     * @return A divisão antecessora, ou null se não encontrada.
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
     * Inverte a ordem de uma lista de divisões, útil após a reconstrução do caminho.
     *
     * @param lista Lista original a ser invertida.
     * @return Nova lista com a ordem invertida.
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
     * @return A vida restante de To Cruz no momento.
     */
    @Override
    public int getVidaRestante() {
        return toCruz.getVida();
    }

    /**
     * Retorna o status da missão após a simulação.
     * 
     * @return "SUCESSO" se To Cruz estiver vivo e o objetivo tiver sido concluído;
     *         "FALHA" caso contrário.
     */
    @Override
    public String getStatus() {
        if (toCruz.getVida() > 0 && toCruz.isAlvoConcluido()) {
            return "SUCESSO";
        }
        return "FALHA";
    }

    /**
     * @return A divisão final onde To Cruz se encontra após a simulação.
     */
    @Override
    public IDivisao getDivisaoFinal() {
        return toCruz.getPosicaoAtual();
    }

    /**
     * @return Lista com as divisões percorridas durante toda a simulação.
     */
    @Override
    public ArrayUnorderedList<IDivisao> getCaminhoPercorrido() {
        return caminhoPercorrido;
    }

    /**
     * @return Lista com os inimigos derrotados durante a simulação.
     *         (Atualmente não populada, mas disponibilizada para uso futuro.)
     */
    public ArrayUnorderedList<IInimigo> getInimigosDerrotados() {
        return inimigosDerrotados;
    }

    /**
     * @return Lista com os itens coletados por To Cruz durante a simulação.
     */
    public ArrayUnorderedList<IItem> getItensColetados() {
        return itensColetados;
    }

    /**
     * Retorna uma lista com os nomes das divisões percorridas, útil para exibição simplificada.
     *
     * @return Lista de nomes de divisões percorridas.
     */
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

    /**
     * Mostra um mapa interativo simplificado no console, indicando a posição atual de To Cruz,
     * bem como o sucesso ou fracasso da missão.
     *
     * @param toCruz       Referência ao agente To Cruz, para obter posição atual.
     * @param divisaoAtual Divisão atual onde To Cruz se encontra.
     * @param sucesso      Indica se a missão teve êxito (true) ou não (false).
     */
    public void mostrarMapaInterativo(ToCruz toCruz, IDivisao divisaoAtual, boolean sucesso) {
        System.out.println("===== MAPA ATUAL =====");
        ArrayUnorderedList<IDivisao> divisoes = mapa.getDivisoes();

        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao divisao = divisoes.getElementAt(i);
            if (divisao == null)
                continue;

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
