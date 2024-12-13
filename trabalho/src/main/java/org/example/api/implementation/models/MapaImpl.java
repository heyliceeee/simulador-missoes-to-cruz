package org.example.api.implementation.models;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.*;
import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.implementation.*;

import java.util.Iterator;
import java.util.Random;

import static org.example.api.implementation.simulation.SimulacaoAutomaticaImpl.*;

/**
 * Implementação do mapa do edifício como um grafo. Cada vértice do grafo representa 
 * uma divisão do edifício, e cada aresta representa uma conexão entre duas divisões.
 * 
 * Esta classe oferece funcionalidades para:
 * <ul>
 *   <li>Adicionar divisões, conexões, inimigos e itens.</li>
 *   <li>Marcar divisões como entrada/saída.</li>
 *   <li>Definir e remover o alvo da missão.</li>
 *   <li>Realizar buscas, encontrar caminhos e mover inimigos.</li>
 *   <li>Calcular o melhor caminho considerando inimigos (como custo) e itens (como redução de custo).</li>
 * </ul>
 *
 * O grafo interno armazena objetos do tipo {@link IDivisao}, permitindo assim que todo 
 * o comportamento do edifício seja modelado através de conexões entre divisões.
 */
public class MapaImpl implements IMapa {

    /**
     * Grafo que representa o edifício e suas conexões entre divisões.
     */
    private Graph<IDivisao> grafo;

    /**
     * Informações sobre o alvo da missão, se houver.
     */
    private IAlvo alvo;

    /**
     * Lista de divisões que funcionam como entradas ou saídas do edifício.
     */
    private ArrayUnorderedList<IDivisao> entradasSaidas;

    /**
     * Construtor padrão do Mapa.
     * Inicializa o grafo vazio e a lista de entradas/saídas.
     */
    public MapaImpl() {
        this.grafo = new Graph<>();
        this.entradasSaidas = new ArrayUnorderedList<>();
    }

    /**
     * Adiciona uma nova divisão ao mapa.
     *
     * @param nomeDivisao Nome da divisão a ser adicionada.
     *                    Não deve ser nulo ou vazio.
     */
    @Override
    public void adicionarDivisao(String nomeDivisao) {
        if (nomeDivisao == null || nomeDivisao.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisao invalido.");
            return;
        }
        IDivisao divisao = new DivisaoImpl(nomeDivisao);
        grafo.addVertex(divisao);
    }

    /**
     * Cria uma ligação (aresta) entre duas divisões.
     *
     * @param nomeDivisao1 Nome da primeira divisão.
     * @param nomeDivisao2 Nome da segunda divisão.
     * @throws IllegalArgumentException se os nomes fornecidos forem nulos ou vazios.
     */
    @Override
    public void adicionarLigacao(String nomeDivisao1, String nomeDivisao2) {
        if (nomeDivisao1 == null || nomeDivisao2 == null ||
                nomeDivisao1.trim().isEmpty() || nomeDivisao2.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomes das divisoes nao podem ser vazios ou nulos.");
        }

        IDivisao divisao1 = getDivisaoPorNome(nomeDivisao1);
        IDivisao divisao2 = getDivisaoPorNome(nomeDivisao2);

        if (grafo.isAdjacent(divisao1, divisao2)) {
            System.out.println("Ligacao ja existente entre " + nomeDivisao1 + " e " + nomeDivisao2);
            return;
        }

        grafo.addEdge(divisao1, divisao2);
    }

    /**
     * Retorna todas as conexões (ligações) do grafo.
     *
     * @return Uma lista de objetos {@link Ligacao} representando as arestas do grafo.
     */
    @Override
    public ArrayUnorderedList<Ligacao> getLigacoes() {
        ArrayUnorderedList<Ligacao> ligacoes = new ArrayUnorderedList<>();

        for (IDivisao divisao : getDivisoes()) {
            LinkedList<IDivisao> adjacentes = grafo.getAdjacentes(divisao);
            for (IDivisao adjacente : adjacentes) {
                Ligacao novaLigacao = new Ligacao(divisao, adjacente);
                if (!ligacoes.contains(novaLigacao)) {
                    ligacoes.addToRear(novaLigacao);
                }
            }
        }
        return ligacoes;
    }

    /**
     * Adiciona um inimigo a uma divisão específica.
     *
     * @param nomeDivisao Nome da divisão.
     * @param inimigo     Inimigo a ser adicionado.
     */
    @Override
    public void adicionarInimigo(String nomeDivisao, IInimigo inimigo) {
        if (nomeDivisao == null || inimigo == null || nomeDivisao.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisao ou inimigo invalido.");
            return;
        }

        IDivisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            divisao.adicionarInimigo(inimigo);
        } else {
            System.err.println("Erro: Divisao '" + nomeDivisao + "' nao encontrada.");
        }
    }

    /**
     * Adiciona um item a uma divisão.
     *
     * @param nomeDivisao Nome da divisão.
     * @param item        Item a ser adicionado.
     */
    @Override
    public void adicionarItem(String nomeDivisao, IItem item) {
        if (nomeDivisao == null || item == null || nomeDivisao.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisao ou item invalido.");
            return;
        }

        IDivisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            item.setDivisao(divisao);
            divisao.adicionarItem(item);
        } else {
            System.err.println("Erro: Divisao '" + nomeDivisao + "' nao encontrada.");
        }
    }

    /**
     * Marca uma divisão como entrada/saída.
     *
     * @param nomeDivisao Nome da divisão a ser marcada.
     */
    @Override
    public void adicionarEntradaSaida(String nomeDivisao) {
        if (nomeDivisao == null || nomeDivisao.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisao para entrada/saida invalido.");
            return;
        }

        IDivisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            divisao.setEntradaSaida(true);
            System.out.println("Divisao '" + nomeDivisao + "' marcada como entrada/saida.");
        } else {
            System.err.println("Erro: Divisao '" + nomeDivisao + "' nao encontrada.");
        }
    }

    /**
     * Obtém uma divisão pelo seu nome.
     *
     * @param nomeDivisao Nome da divisão.
     * @return A divisão correspondente.
     * @throws RuntimeException se a divisão não for encontrada.
     * @throws IllegalArgumentException se o nome fornecido for nulo ou vazio.
     */
    @Override
    public IDivisao getDivisaoPorNome(String nomeDivisao) {
        if (nomeDivisao == null || nomeDivisao.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da divisao nao pode ser vazio ou nulo.");
        }

        Iterator<IDivisao> iterator = grafo.iterator();
        while (iterator.hasNext()) {
            IDivisao divisao = iterator.next();
            if (divisao.getNomeDivisao().equals(nomeDivisao.trim())) {
                return divisao;
            }
        }

        throw new RuntimeException("Divisao '" + nomeDivisao + "' nao encontrada.");
    }

    /**
     * Verifica se é possível mover de uma divisão para outra diretamente (ou seja, 
     * se há uma aresta entre elas).
     *
     * @param divisao1 Nome da divisão de origem.
     * @param divisao2 Nome da divisão de destino.
     * @return true se for possível mover, false caso contrário.
     */
    @Override
    public boolean podeMover(String divisao1, String divisao2) {
        if (divisao1 == null || divisao2 == null ||
                divisao1.trim().isEmpty() || divisao2.trim().isEmpty()) {
            return false;
        }

        IDivisao d1 = getDivisaoPorNome(divisao1);
        IDivisao d2 = getDivisaoPorNome(divisao2);
        return d1 != null && d2 != null && grafo.isAdjacent(d1, d2);
    }

    /**
     * Define o alvo da missão em uma determinada divisão.
     *
     * @param nomeDivisao Nome da divisão onde o alvo está localizado.
     * @param tipo        Tipo do alvo.
     */
    @Override
    public void definirAlvo(String nomeDivisao, String tipo) {
        if (nomeDivisao == null || tipo == null ||
                nomeDivisao.trim().isEmpty() || tipo.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisao ou tipo do alvo invalido.");
            return;
        }

        IDivisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            this.alvo = new AlvoImpl(divisao, tipo);
            System.out.println("Alvo definido na divisao " + nomeDivisao + " de tipo " + tipo);
        } else {
            System.err.println("Erro: Divisao '" + nomeDivisao + "' nao encontrada para definir o alvo.");
        }
    }

    /**
     * Remove o alvo do mapa.
     */
    @Override
    public void removerAlvo() {
        if (this.alvo != null) {
            System.out.println("Alvo removido do mapa.");
            this.alvo = null;
        } else {
            System.out.println("Nenhum alvo para remover.");
        }
    }

    /**
     * Obtém o alvo da missão.
     *
     * @return O alvo atual, ou null se não houver.
     */
    @Override
    public IAlvo getAlvo() {
        return this.alvo;
    }

    /**
     * Obtém as conexões (divisões adjacentes) a partir de uma divisão especifica.
     * 
     * Realiza uma busca a partir da divisão fornecida, retornando as conexões.
     *
     * @param divisao Divisão de origem.
     * @return Lista de divisões conectadas.
     */
    @Override
    public ArrayUnorderedList<IDivisao> obterConexoes(IDivisao divisao) {
        ArrayUnorderedList<IDivisao> conexoes = new ArrayUnorderedList<>();
        if (divisao == null) {
            return conexoes;
        }
        Iterator<IDivisao> iterator = grafo.iteratorBFS(divisao);
        while (iterator.hasNext()) {
            IDivisao conexao = iterator.next();
            if (conexao != null && !conexao.equals(divisao)) {
                conexoes.addToRear(conexao);
            }
        }
        return conexoes;
    }

    /**
     * Retorna todas as divisões presentes no mapa.
     *
     * @return Uma lista de divisões.
     */
    @Override
    public ArrayUnorderedList<IDivisao> getDivisoes() {
        ArrayUnorderedList<IDivisao> divisoes = new ArrayUnorderedList<>();
        Iterator<IDivisao> iterator = grafo.iterator();
        while (iterator.hasNext()) {
            IDivisao divisao = iterator.next();
            if (divisao != null) {
                divisoes.addToRear(divisao);
            }
        }
        return divisoes;
    }

    /**
     * Obtém os nomes das divisões marcadas como entrada/saída.
     *
     * @return Lista de nomes de divisões de entrada/saída.
     */
    @Override
    public ArrayUnorderedList<String> getEntradasSaidasNomes() {
        ArrayUnorderedList<String> entradasSaidas = new ArrayUnorderedList<>();
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();
        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao d = divisoes.getElementAt(i);
            if (d != null && d.isEntradaSaida()) {
                entradasSaidas.addToRear(d.getNomeDivisao());
            }
        }
        return entradasSaidas;
    }

    /**
     * Obtém as divisões que são entradas/saídas.
     *
     * @return Lista de divisões de entrada/saída.
     */
    @Override
    public ArrayUnorderedList<IDivisao> getEntradasSaidas() {
        ArrayUnorderedList<IDivisao> resultado = new ArrayUnorderedList<>();
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();
        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao d = divisoes.getElementAt(i);
            if (d != null && d.isEntradaSaida()) {
                resultado.addToRear(d);
            }
        }
        return resultado;
    }

    /**
     * Move os inimigos de forma aleatória, podendo deslocá-los por até duas divisões.
     * Caso um inimigo entre na mesma divisão de To Cruz, ocorre um combate imediato.
     *
     * @param toCruz          Personagem principal (To Cruz).
     * @param combateService  Serviço de combate para resolver embates.
     * @throws ElementNotFoundException se ocorrer erro ao acessar divisões ou inimigos.
     */
    @Override
    public void moverInimigos(ToCruz toCruz, ICombateService combateService) throws ElementNotFoundException {
        Random random = new Random();
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();

        if (divisoes == null || divisoes.isEmpty()) {
            throw new IllegalStateException("Nenhuma divisao disponivel para mover inimigos.");
        }

        // Percorre todas as divisões e move os inimigos de forma aleatória
        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao divisaoAtual = divisoes.getElementAt(i);
            if (divisaoAtual == null) continue;

            ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
            if (inimigos == null || inimigos.isEmpty()) continue;

            ArrayUnorderedList<IInimigo> inimigosCopy = new ArrayUnorderedList<>();
            for (int j = 0; j < inimigos.size(); j++) {
                inimigosCopy.addToRear(inimigos.getElementAt(j));
            }

            for (int j = 0; j < inimigosCopy.size(); j++) {
                IInimigo inimigo = inimigosCopy.getElementAt(j);
                if (inimigo == null) continue;

                IDivisao origem = divisaoAtual;
                IDivisao destino = origem;

                // Tenta mover o inimigo por até 2 divisões
                for (int movimentos = 0; movimentos < 2; movimentos++) {
                    LinkedList<IDivisao> adjacentes = grafo.getAdjacentes(destino);
                    if (adjacentes.isEmpty()) break;
                    IDivisao novaDivisao = adjacentes.getElementAt(random.nextInt(adjacentes.size()));
                    if (novaDivisao != null) {
                        destino = novaDivisao;
                    }
                }

                // Caso o destino seja diferente da origem, move o inimigo
                if (!destino.equals(origem)) {
                    destino.adicionarInimigo(inimigo);
                    origem.removerInimigo(inimigo);
                    System.out.println("Inimigo '" + inimigo.getNome() + "' movimentou de " +
                            origem.getNomeDivisao() + " para " + destino.getNomeDivisao());

                    // Se inimigo entrar na sala do To Cruz, combate imediato (inimigos atacam primeiro)
                    if (destino.equals(toCruz.getPosicaoAtual())) {
                        System.out.println(crossedSwords
                                + " Inimigo entrou na sala de To Cruz! Combate iniciado (inimigos primeiro).");
                        combateService.resolverCombate(toCruz, destino, true);

                        if (toCruz.getVida() <= 0) {
                            System.err.println(skull + " To Cruz foi derrotado durante o ataque dos inimigos!");
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * Calcula o melhor caminho entre duas divisões (origem e destino), 
     * levando em consideração custos baseados em inimigos (aumentam custo) 
     * e kits de vida (diminuem o custo).
     * 
     * @param origem  Divisão inicial.
     * @param destino Divisão final.
     * @return Lista de divisões representando o caminho ótimo encontrado, 
     *         ou uma lista vazia se não houver caminho.
     */
    @Override
    public ArrayUnorderedList<IDivisao> calcularMelhorCaminho(IDivisao origem, IDivisao destino) {
        if (origem == null || destino == null) {
            System.err.println("Erro: Origem ou destino invalidos.");
            return new ArrayUnorderedList<>();
        }

        LinkedQueue<IDivisao> fila = new LinkedQueue<>();
        ArrayUnorderedList<IDivisao> visitados = new ArrayUnorderedList<>();
        ArrayUnorderedList<Predecessor> predecessores = new ArrayUnorderedList<>();
        ArrayUnorderedList<Integer> custos = new ArrayUnorderedList<>();

        fila.enqueue(origem);
        visitados.addToRear(origem);
        predecessores.addToRear(new Predecessor(origem, null));
        custos.addToRear(0);

        while (!fila.isEmpty()) {
            IDivisao atual = fila.dequeue();
            int indiceAtual = findIndex(visitados, atual);
            int custoAtual = custos.getElementAt(indiceAtual);

            if (atual.equals(destino)) {
                ArrayUnorderedList<IDivisao> caminho = new ArrayUnorderedList<>();
                reconstruirCaminho(predecessores, destino, caminho);
                return caminho;
            }

            LinkedList<IDivisao> conexoes = grafo.getAdjacentes(atual);
            if (conexoes == null || conexoes.isEmpty()) continue;

            for (int i = 0; i < conexoes.size(); i++) {
                IDivisao vizinho = conexoes.getElementAt(i);
                if (vizinho == null) continue;

                int custoMovimento = calcularCusto(atual, vizinho);
                int novoCusto = custoAtual + custoMovimento;
                int indiceVizinho = findIndex(visitados, vizinho);

                if (indiceVizinho == -1) {
                    visitados.addToRear(vizinho);
                    custos.addToRear(novoCusto);
                    fila.enqueue(vizinho);
                    predecessores.addToRear(new Predecessor(vizinho, atual));
                } else if (novoCusto < custos.getElementAt(indiceVizinho)) {
                    replaceElementAt(custos, indiceVizinho, novoCusto);
                    predecessores.addToRear(new Predecessor(vizinho, atual));
                }
            }
        }

        System.err.println("Caminho nao encontrado entre " + origem.getNomeDivisao() + " e " + destino.getNomeDivisao());
        return new ArrayUnorderedList<>();
    }

    /**
     * Mostra o mapa do edifício (divisões e conexões) no console, incluindo informações 
     * sobre inimigos, itens, e se a divisão é entrada/saída.
     */
    @Override
    public void mostrarMapa() {
        System.out.println("===== MAPA DO EDIFICIO =====");
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();

        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao divisao = divisoes.getElementAt(i);
            if (divisao == null) continue;

            ArrayUnorderedList<IInimigo> inimigos = divisao.getInimigosPresentes();
            ArrayUnorderedList<IItem> itens = divisao.getItensPresentes();

            System.out.print(pin + divisao.getNomeDivisao());
            if (inimigos != null && !inimigos.isEmpty()) {
                System.out.print(crossedSwords + " (" + inimigos.size() + " inimigos)");
            }
            if (itens != null && !itens.isEmpty()) {
                System.out.print(" 🎒 (" + itens.size() + " itens)");
            }
            if (divisao.isEntradaSaida()) {
                System.out.print(" 🚪 [Entrada/Saida]");
            }

            System.out.println();

            LinkedList<IDivisao> conexoes = grafo.getAdjacentes(divisao);
            if (conexoes.isEmpty()) {
                System.out.println("   ↳ Sem conexoes");
            } else {
                for (int j = 0; j < conexoes.size(); j++) {
                    IDivisao conexao = conexoes.getElementAt(j);
                    System.out.println("   ↳ Conecta com: " + conexao.getNomeDivisao());
                }
            }
            System.out.println();
        }
        System.out.println("=============================");
    }

    /**
     * Obtém o primeiro vértice (divisão) do grafo.
     *
     * @return A primeira divisão ou null se o grafo estiver vazio.
     */
    @Override
    public IDivisao getPrimeiroVertice() {
        Iterator<IDivisao> iterator = grafo.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * Expande as conexões de uma divisão para incluir divisões conectadas 
     * a até duas conexões de distância.
     *
     * @param divisaoAtual A divisão atual de onde partirá a expansão.
     * @return Uma lista de divisões acessíveis a até duas conexões de distância.
     */
    @Override
    public ArrayUnorderedList<IDivisao> expandirConexoes(IDivisao divisaoAtual) {
        LinkedList<IDivisao> conexoesDiretas = grafo.getAdjacentes(divisaoAtual);
        ArrayUnorderedList<IDivisao> conexoesExpandida = new ArrayUnorderedList<>();

        // Conexões de segunda distância (repetição na lógica original, porém mantida)
        for (int i = 0; i < conexoesDiretas.size(); i++) {
            IDivisao conexao = conexoesDiretas.getElementAt(i);
            LinkedList<IDivisao> conexoesSegundaDistancia = grafo.getAdjacentes(divisaoAtual);

            for (int j = 0; j < conexoesSegundaDistancia.size(); j++) {
                IDivisao segundaConexao = conexoesSegundaDistancia.getElementAt(j);
                if (!conexoesDiretas.contains(segundaConexao)
                        && !conexoesExpandida.contains(segundaConexao)
                        && !segundaConexao.equals(divisaoAtual)) {
                    conexoesExpandida.addToRear(segundaConexao);
                }
            }
        }

        // Adiciona conexões diretas à lista expandida
        for (int i = 0; i < conexoesDiretas.size(); i++) {
            IDivisao conexao = conexoesDiretas.getElementAt(i);
            if (!conexoesExpandida.contains(conexao)) {
                conexoesExpandida.addToRear(conexao);
            }
        }

        return conexoesExpandida;
    }

    /**
     * Encontra a divisão com um kit de vida mais próximo da divisão de origem,
     * utilizando uma busca em largura (BFS).
     *
     * @param origem Divisão atual do personagem.
     * @return A divisão contendo um kit de vida mais próximo, ou null se nenhum for encontrado.
     * @throws ElementNotFoundException se houver erro ao acessar divisões.
     * @throws IllegalArgumentException se a origem for nula.
     */
    @Override
    public IDivisao encontrarKitMaisProximo(IDivisao origem) throws ElementNotFoundException {
        if (origem == null) {
            throw new IllegalArgumentException("Origem invalida");
        }

        ArrayUnorderedList<IDivisao> divisoesVisitadas = new ArrayUnorderedList<>();
        ArrayUnorderedList<IDivisao> fila = new ArrayUnorderedList<>();
        fila.addToRear(origem);

        while (!fila.isEmpty()) {
            IDivisao atual = fila.getElementAt(0);
            fila.remove(atual);

            if (atual.temKit()) {
                return atual;
            }

            LinkedList<IDivisao> adjacentes = grafo.getAdjacentes(atual);
            for (int i = 0; i < adjacentes.size(); i++) {
                IDivisao vizinho = adjacentes.getElementAt(i);
                if (!divisoesVisitadas.contains(vizinho)) {
                    fila.addToRear(vizinho);
                    divisoesVisitadas.addToRear(vizinho);
                }
            }
        }

        return null;
    }

    /**
     * Obtém todos os itens de um tipo específico presentes no mapa.
     *
     * @param tipo Tipo de item a ser buscado.
     * @return Lista de itens do tipo especificado.
     */
    @Override
    public ArrayUnorderedList<IItem> getItensPorTipo(String tipo) {
        ArrayUnorderedList<IItem> itens = new ArrayUnorderedList<>();
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();

        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao divisao = divisoes.getElementAt(i);
            if (divisao != null) {
                ArrayUnorderedList<IItem> itensDivisao = divisao.getItensPresentes();
                for (int j = 0; j < itensDivisao.size(); j++) {
                    IItem item = itensDivisao.getElementAt(j);
                    if (item != null && item.getTipo().equalsIgnoreCase(tipo)) {
                        itens.addToRear(item);
                    }
                }
            }
        }

        return itens;
    }

    /**
     * Obtém todos os itens presentes no mapa, independente do tipo.
     *
     * @return Lista de todos os itens no mapa.
     */
    @Override
    public ArrayUnorderedList<IItem> getItens() {
        ArrayUnorderedList<IItem> itens = new ArrayUnorderedList<>();
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();

        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao divisao = divisoes.getElementAt(i);
            if (divisao != null) {
                ArrayUnorderedList<IItem> itensDivisao = divisao.getItensPresentes();
                for (int j = 0; j < itensDivisao.size(); j++) {
                    IItem item = itensDivisao.getElementAt(j);
                    if (item != null) {
                        itens.addToRear(item);
                    }
                }
            }
        }

        return itens;
    }

    // Métodos privados auxiliares

    /**
     * Encontra o índice de um determinado elemento em uma lista.
     *
     * @param list   Lista a ser pesquisada.
     * @param target Elemento alvo.
     * @return Índice do elemento ou -1 se não encontrado.
     */
    private int findIndex(ArrayUnorderedList<IDivisao> list, IDivisao target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.getElementAt(i).equals(target)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Substitui um elemento em uma lista de custos.
     *
     * @param list  Lista de custos.
     * @param index Índice a ser substituído.
     * @param value Novo valor de custo.
     */
    private void replaceElementAt(ArrayUnorderedList<Integer> list, int index, int value) {
        try {
            list.remove(list.getElementAt(index));
            if (index == 0) {
                list.addToFront(value);
            } else {
                list.addAfter(value, list.getElementAt(index - 1));
            }
        } catch (ElementNotFoundException e) {
            System.err.println("Erro ao substituir elemento na lista: " + e.getMessage());
        }
    }

    /**
     * Calcula o custo de atravessar de uma divisão para outra, considerando 
     * inimigos (aumentam o custo) e kits de vida (reduzem o custo).
     *
     * @param atual   Divisão atual.
     * @param vizinho Divisão vizinha.
     * @return Custo calculado.
     */
    private int calcularCusto(IDivisao atual, IDivisao vizinho) {
        int custo = 0;

        ArrayUnorderedList<IInimigo> inimigos = vizinho.getInimigosPresentes();
        if (inimigos != null && !inimigos.isEmpty()) {
            for (int i = 0; i < inimigos.size(); i++) {
                custo += inimigos.getElementAt(i).getPoder();
            }
        }

        ArrayUnorderedList<IItem> itens = vizinho.getItensPresentes();
        if (itens != null && !itens.isEmpty()) {
            for (int i = 0; i < itens.size(); i++) {
                IItem item = itens.getElementAt(i);
                if ("kit de vida".equalsIgnoreCase(item.getTipo())) {
                    custo -= item.getPontos();
                }
            }
        }

        return Math.max(custo, 0);
    }

    /**
     * Reconstrói o caminho a partir dos predecessores após encontrar o destino.
     *
     * @param predecessores Lista de predecessores.
     * @param objetivo      Divisão objetivo.
     * @param caminho       Lista onde o caminho será armazenado.
     */
    private void reconstruirCaminho(ArrayUnorderedList<Predecessor> predecessores, IDivisao objetivo,
            ArrayUnorderedList<IDivisao> caminho) {
        LinkedStack<IDivisao> pilha = new LinkedStack<>();
        IDivisao atual = objetivo;

        while (atual != null) {
            pilha.push(atual);
            atual = getPredecessor(predecessores, atual.getNomeDivisao());
        }

        while (!pilha.isEmpty()) {
            try {
                caminho.addToRear(pilha.pop());
            } catch (EmptyCollectionException e) {
                System.err.println("Erro ao reconstruir o caminho: " + e.getMessage());
            }
        }
    }

    /**
     * Obtém o predecessor de uma divisão a partir da lista de predecessores.
     *
     * @param predecessores Lista de predecessores.
     * @param nomeDivisao   Nome da divisão atual.
     * @return A divisão predecessora ou null se não encontrada.
     */
    private IDivisao getPredecessor(ArrayUnorderedList<Predecessor> predecessores, String nomeDivisao) {
        for (int i = 0; i < predecessores.size(); i++) {
            Predecessor p = predecessores.getElementAt(i);
            if (p != null && p.getAtual().getNomeDivisao().equalsIgnoreCase(nomeDivisao)) {
                return p.getPredecessor();
            }
        }
        return null;
    }
}
