package org.example.api.implementation.models;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.*;
import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.implementation.*;

import java.util.Iterator;
import java.util.Random;

import static org.example.api.implementation.simulation.SimulacaoAutomaticaImpl.*;

/**
 * Representa o mapa do edificio como um grafo.
 */
public class MapaImpl implements IMapa {
    /**
     * Grafo que representa o edificio e suas conexoes.
     */
    private Graph<IDivisao> grafo;

    /**
     * Informacoes sobre o alvo da missao.
     */
    private IAlvo alvo;

    /**
     * Lista de divisoes que sao entradas ou saidas do edificio.
     */
    private ArrayUnorderedList<IDivisao> entradasSaidas;

    /**
     * Construtor padrao do Mapa.
     */
    public MapaImpl() {
        this.grafo = new Graph<>();
        this.entradasSaidas = new ArrayUnorderedList<>();
    }

    /**
     * Adiciona uma divisao ao mapa.
     *
     * @param nomeDivisao Nome da divisao.
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
     * Adiciona uma ligacao entre duas divisoes.
     *
     * @param nomeDivisao1 Nome da primeira divisao.
     * @param nomeDivisao2 Nome da segunda divisao.
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
     * Obtem todas as conexoes (arestas) do grafo.
     *
     * @return Uma lista de conexoes entre divisoes.
     */
    @Override
    public ArrayUnorderedList<Ligacao> getLigacoes() {
        ArrayUnorderedList<Ligacao> ligacoes = new ArrayUnorderedList<>();

        // Itera por cada divisao no grafo
        for (IDivisao divisao : getDivisoes()) {
            LinkedList<IDivisao> adjacentes = grafo.getAdjacentes(divisao);

            for (IDivisao adjacente : adjacentes) {
                // Adiciona a ligacao se ela ainda nao foi registada
                Ligacao novaLigacao = new Ligacao(divisao, adjacente);
                if (!ligacoes.contains(novaLigacao)) {
                    ligacoes.addToRear(novaLigacao);
                }
            }
        }
        return ligacoes;
    }

    /**
     * Adiciona um inimigo a uma divisao.
     *
     * @param nomeDivisao Nome da divisao.
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
     * Adiciona um item a uma divisao.
     *
     * @param nomeDivisao Nome da divisao.
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
            item.setDivisao(divisao); // Vincula o item à divisao
            divisao.adicionarItem(item);
        } else {
            System.err.println("Erro: Divisao '" + nomeDivisao + "' nao encontrada.");
        }
    }

    /**
     * Adiciona uma divisao como entrada ou saida.
     *
     * @param nomeDivisao Nome da divisao.
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
     * Obtem uma divisao pelo nome.
     *
     * @param nomeDivisao Nome da divisao.
     * @return A divisao encontrada ou null.
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
     * Verifica se e possivel mover de uma divisao para outra.
     *
     * @param divisao1 Nome da divisao de origem.
     * @param divisao2 Nome da divisao de destino.
     * @return true se for possivel mover, false caso contrario.
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
     * Define o alvo da missao.
     *
     * @param nomeDivisao Nome da divisao onde o alvo esta localizado.
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
     * Obtem o alvo da missao.
     *
     * @return Alvo da missao.
     */
    @Override
    public IAlvo getAlvo() {
        return this.alvo;
    }

    /**
     * Obtem todas as conexoes (divisoes acessiveis) a partir de uma divisao
     * especifica.
     *
     * @param divisao Divisao de origem.
     * @return Lista de divisoes conectadas.
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
     * Obtem todas as divisoes presentes no grafo.
     *
     * @return Lista de todas as divisoes.
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
     * Obtem os nomes das divisoes que sao entradas ou saidas.
     *
     * @return Lista de nomes das entradas e saidas.
     */
    @Override
    public ArrayUnorderedList<String> getEntradasSaidasNomes() {
        ArrayUnorderedList<String> entradasSaidas = new ArrayUnorderedList<>();
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();
        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao divisao = divisoes.getElementAt(i);
            if (divisao != null && divisao.isEntradaSaida()) {
                entradasSaidas.addToRear(divisao.getNomeDivisao());
            }
        }
        return entradasSaidas;
    }

    /**
     * Obtem as divisoes que sao entradas ou saidas.
     *
     * @return Lista de entradas e saidas.
     */
    @Override
    public ArrayUnorderedList<IDivisao> getEntradasSaidas() {
        ArrayUnorderedList<IDivisao> entradasSaidas = new ArrayUnorderedList<>();
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();
        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao divisao = divisoes.getElementAt(i);
            if (divisao != null && divisao.isEntradaSaida()) {
                entradasSaidas.addToRear(divisao);
            }
        }
        return entradasSaidas;
    }

    /**
     * Move os inimigos aleatoriamente para divisoes conectadas ate duas divisoes de
     * distancia.
     */
    @Override
    public void moverInimigos(ToCruz toCruz, ICombateService combateService) throws ElementNotFoundException {
        Random random = new Random();
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();

        if (divisoes == null || divisoes.isEmpty()) {
            throw new IllegalStateException("Nenhuma divisao disponivel para mover inimigos.");
        }

        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao divisaoAtual = divisoes.getElementAt(i);
            if (divisaoAtual == null)
                continue;

            ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
            if (inimigos == null || inimigos.isEmpty())
                continue;

            ArrayUnorderedList<IInimigo> inimigosCopy = new ArrayUnorderedList<>();
            for (int j = 0; j < inimigos.size(); j++) {
                inimigosCopy.addToRear(inimigos.getElementAt(j));
            }

            for (int j = 0; j < inimigosCopy.size(); j++) {
                IInimigo inimigo = inimigosCopy.getElementAt(j);
                if (inimigo == null)
                    continue;

                IDivisao origem = divisaoAtual;
                IDivisao destino = origem;

                // Movimentar ate 2 divisoes aleatoriamente
                for (int movimentos = 0; movimentos < 2; movimentos++) {
                    ArrayUnorderedList<Ligacao> ligacoes = new ArrayUnorderedList<>(); // ligacoes diretas do destino
                    LinkedList<IDivisao> adjacentes = grafo.getAdjacentes(destino); // todas as divisoes perto de
                                                                                    // destino

                    for (IDivisao adjacente : adjacentes) {
                        // Adiciona a ligacao se ela ainda nao foi registada
                        Ligacao novaLigacao = new Ligacao(destino, adjacente);
                        if (!ligacoes.contains(novaLigacao)) {
                            ligacoes.addToRear(novaLigacao);
                        }
                    }

                    if (ligacoes.isEmpty())
                        break;

                    IDivisao novaDivisao = ligacoes.getElementAt(random.nextInt(ligacoes.size()))
                            .getDivisao1() == destino
                                    ? ligacoes.getElementAt(random.nextInt(ligacoes.size())).getDivisao2()
                                    : ligacoes.getElementAt(random.nextInt(ligacoes.size())).getDivisao1();
                    if (novaDivisao != null) {
                        destino = novaDivisao;
                    }
                }

                // Mover inimigo para o destino final
                if (!destino.equals(origem)) {
                    destino.adicionarInimigo(inimigo);
                    origem.removerInimigo(inimigo);
                    System.out.println("Inimigo '" + inimigo.getNome() + "' movimentou de " +
                            origem.getNomeDivisao() + " para " + destino.getNomeDivisao());

                    // Verificar se o inimigo entrou na sala de To Cruz
                    if (destino.equals(toCruz.getPosicaoAtual())) {
                        System.out.println(crossedSwords + "  Inimigo entrou na sala de To Cruz! Combate iniciado.");
                        combateService.resolverCombate(toCruz, destino);

                        // Verificar se To Cruz foi derrotado
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
     * Mostra o mapa do edificio, exibindo as divisoes (vertices) e suas conexoes
     * (arestas).
     */
    @Override
    public ArrayUnorderedList<IDivisao> calcularMelhorCaminho(IDivisao origem, IDivisao destino) {
        if (origem == null || destino == null) {
            System.err.println("Erro: Origem ou destino invalidos.");
            return new ArrayUnorderedList<>();
        }

        // Estruturas de suporte
        LinkedQueue<IDivisao> fila = new LinkedQueue<>();
        ArrayUnorderedList<IDivisao> visitados = new ArrayUnorderedList<>();
        ArrayUnorderedList<Predecessor> predecessores = new ArrayUnorderedList<>();
        ArrayUnorderedList<Integer> custos = new ArrayUnorderedList<>(); // Custos paralelos aos visitados

        // Inicializacao
        fila.enqueue(origem);
        visitados.addToRear(origem);
        predecessores.addToRear(new Predecessor(origem, null));
        custos.addToRear(0); // Custo inicial e zero

        while (!fila.isEmpty()) {
            // Retirar o proximo no da fila
            IDivisao atual = fila.dequeue();

            // Encontrar indice do no atual em `visitados`
            int indiceAtual = findIndex(visitados, atual);
            int custoAtual = custos.getElementAt(indiceAtual);

            // Se chegamos ao destino, reconstruir o caminho
            if (atual.equals(destino)) {
                ArrayUnorderedList<IDivisao> caminho = new ArrayUnorderedList<>();
                reconstruirCaminho(predecessores, destino, caminho);
                return caminho;
            }

            // Obter conexoes da divisao atual
            LinkedList<IDivisao> conexoes = grafo.getAdjacentes(atual);
            if (conexoes == null || conexoes.isEmpty()) {
                continue;
            }

            // Processar conexoes
            for (int i = 0; i < conexoes.size(); i++) {
                IDivisao vizinho = conexoes.getElementAt(i);
                if (vizinho == null)
                    continue;

                // Calcular custo para alcancar o vizinho
                int custoMovimento = calcularCusto(atual, vizinho);
                int novoCusto = custoAtual + custoMovimento;

                // Verificar se o vizinho ja foi visitado
                int indiceVizinho = findIndex(visitados, vizinho);
                if (indiceVizinho == -1) { // Nao visitado
                    visitados.addToRear(vizinho);
                    custos.addToRear(novoCusto);
                    fila.enqueue(vizinho);
                    predecessores.addToRear(new Predecessor(vizinho, atual));
                } else if (novoCusto < custos.getElementAt(indiceVizinho)) { // Melhor custo
                    // Atualizar custo manualmente
                    replaceElementAt(custos, indiceVizinho, novoCusto);
                    predecessores.addToRear(new Predecessor(vizinho, atual));
                }
            }
        }

        System.err
                .println("Caminho nao encontrado entre " + origem.getNomeDivisao() + " e " + destino.getNomeDivisao());
        return new ArrayUnorderedList<>();
    }

    private int findIndex(ArrayUnorderedList<IDivisao> list, IDivisao target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.getElementAt(i).equals(target)) {
                return i;
            }
        }
        return -1; // Retorna -1 se nao encontrado
    }

    private void replaceElementAt(ArrayUnorderedList<Integer> list, int index, int value) {
        try {
            list.remove(list.getElementAt(index)); // Remove o elemento atual
            if (index == 0) {
                list.addToFront(value); // Adiciona na frente se for o primeiro elemento
            } else {
                list.addAfter(value, list.getElementAt(index - 1)); // Adiciona apos o anterior
            }
        } catch (ElementNotFoundException e) {
            System.err.println("Erro ao substituir elemento na lista: " + e.getMessage());
        }
    }

    /**
     * Calcula o custo de mover-se de uma divisao para outra.
     */
    private int calcularCusto(IDivisao atual, IDivisao vizinho) {
        int custo = 0;

        // Adicionar custo por inimigos (usando o poder dos inimigos)
        ArrayUnorderedList<IInimigo> inimigos = vizinho.getInimigosPresentes();
        if (inimigos != null && !inimigos.isEmpty()) {
            for (int i = 0; i < inimigos.size(); i++) {
                custo += inimigos.getElementAt(i).getPoder(); // Usar `getPoder()` como dano
            }
        }

        // Subtrair custo por itens de recuperacao (usando os pontos do item)
        ArrayUnorderedList<IItem> itens = vizinho.getItensPresentes();
        if (itens != null && !itens.isEmpty()) {
            for (int i = 0; i < itens.size(); i++) {
                IItem item = itens.getElementAt(i);
                if ("kit de vida".equalsIgnoreCase(item.getTipo())) {
                    custo -= item.getPontos(); // Usar `getPontos()` para recuperacao
                }
            }
        }

        return Math.max(custo, 0); // Evitar custos negativos
    }

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

    private IDivisao getPredecessor(ArrayUnorderedList<Predecessor> predecessores, String nomeDivisao) {
        for (int i = 0; i < predecessores.size(); i++) {
            Predecessor p = predecessores.getElementAt(i);
            if (p != null && p.getAtual().getNomeDivisao().equalsIgnoreCase(nomeDivisao)) {
                return p.getPredecessor();
            }
        }
        return null;
    }

    @Override
    public void mostrarMapa() {
        System.out.println("===== MAPA DO EDIFiCIO =====");
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();

        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao divisao = divisoes.getElementAt(i);
            if (divisao == null)
                continue;

            // Obter informacoes da divisao
            ArrayUnorderedList<IInimigo> inimigos = divisao.getInimigosPresentes();
            ArrayUnorderedList<IItem> itens = divisao.getItensPresentes();

            // Exibir o nome da divisao com simbolos adicionais
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

            // Obter as conexoes
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
     * Obtem o primeiro vertice (divisao) no grafo.
     *
     * @return A primeira divisao ou null se o grafo estiver vazio.
     */
    @Override
    public IDivisao getPrimeiroVertice() {
        Iterator<IDivisao> iterator = grafo.iterator(); // Usar o iterador padrao
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * Expande as conexoes de uma divisao para incluir divisoes conectadas a ate
     * duas conexoes de distancia.
     *
     * @param divisaoAtual A divisao atual de onde partira a expansao.
     * @return Uma lista de divisoes acessiveis a ate duas conexoes de distancia.
     */
    @Override
    public ArrayUnorderedList<IDivisao> expandirConexoes(IDivisao divisaoAtual) {
        LinkedList<IDivisao> conexoesDiretas = grafo.getAdjacentes(divisaoAtual);
        ArrayUnorderedList<IDivisao> conexoesExpandida = new ArrayUnorderedList<>();

        // Adiciona conexoes de segunda distancia
        for (int i = 0; i < conexoesDiretas.size(); i++) {
            IDivisao conexao = conexoesDiretas.getElementAt(i);
            LinkedList<IDivisao> conexoesSegundaDistancia = grafo.getAdjacentes(divisaoAtual);

            for (int j = 0; j < conexoesSegundaDistancia.size(); j++) {
                IDivisao segundaConexao = conexoesSegundaDistancia.getElementAt(j);

                // Adiciona a conexao se ela nao estiver na lista inicial e nao for a propria
                // divisao atual
                if (!conexoesDiretas.contains(segundaConexao) && !conexoesExpandida.contains(segundaConexao)
                        && !segundaConexao.equals(divisaoAtual)) {
                    conexoesExpandida.addToRear(segundaConexao);
                }
            }
        }

        // Adiciona conexoes diretas a lista expandida
        for (int i = 0; i < conexoesDiretas.size(); i++) {
            IDivisao conexao = conexoesDiretas.getElementAt(i);

            if (!conexoesExpandida.contains(conexao)) {
                conexoesExpandida.addToRear(conexao);
            }
        }

        return conexoesExpandida;
    }

    /**
     * Localizar o kit de vida mais proximo
     * 
     * @param origem divisao atual
     * @return divisao
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

            if (atual.temKit()) { // verifica se ha kit na divisao
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

        return null; // Caso nenhum kit seja encontrado
    }

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
                    //if (item != null && item.getTipo().equalsIgnoreCase(tipo)) {
                        itens.addToRear(item);
                    //}
                }
            }
        }

        return itens;
    }
}
