package org.example.api.implementation.models;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.Alvo;
import org.example.api.implementation.interfaces.CombateService;
import org.example.api.implementation.interfaces.Divisao;
import org.example.api.implementation.interfaces.Inimigo;
import org.example.api.implementation.interfaces.Item;
import org.example.api.implementation.interfaces.Mapa;
import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.Graph;
import org.example.collections.implementation.LinkedQueue;
import org.example.collections.implementation.LinkedStack;

import java.util.Iterator;
import java.util.Random;

/**
 * Representa o mapa do edifício como um grafo.
 */
public class MapaImpl implements Mapa {
    private Graph<Divisao> grafo;
    private Alvo alvo;

    public MapaImpl() {
        this.grafo = new Graph<>();
    }

    @Override
    public void adicionarDivisao(String nomeDivisao) {
        if (nomeDivisao == null || nomeDivisao.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisão inválido.");
            return;
        }
        Divisao divisao = new DivisaoImpl(nomeDivisao);
        grafo.addVertex(divisao);
    }

    @Override
    public void adicionarLigacao(String nomeDivisao1, String nomeDivisao2) {
        if (nomeDivisao1 == null || nomeDivisao2 == null ||
                nomeDivisao1.trim().isEmpty() || nomeDivisao2.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomes das divisões não podem ser vazios ou nulos.");
        }

        Divisao divisao1 = getDivisaoPorNome(nomeDivisao1);
        Divisao divisao2 = getDivisaoPorNome(nomeDivisao2);

        if (grafo.isAdjacent(divisao1, divisao2)) {
            System.out.println("Ligação já existente entre " + nomeDivisao1 + " e " + nomeDivisao2);
            return;
        }

        grafo.addEdge(divisao1, divisao2);
    }

    @Override
    public void adicionarInimigo(String nomeDivisao, Inimigo inimigo) {
        if (nomeDivisao == null || inimigo == null || nomeDivisao.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisão ou inimigo inválido.");
            return;
        }

        Divisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            divisao.adicionarInimigo(inimigo);
        } else {
            System.err.println("Erro: Divisão '" + nomeDivisao + "' não encontrada.");
        }
    }

    @Override
    public void adicionarItem(String nomeDivisao, Item item) {
        if (nomeDivisao == null || item == null || nomeDivisao.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisão ou item inválido.");
            return;
        }

        Divisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            item.setDivisao(divisao); // Vincula o item à divisão
            divisao.adicionarItem(item);
        } else {
            System.err.println("Erro: Divisão '" + nomeDivisao + "' não encontrada.");
        }
    }

    @Override
    public void adicionarEntradaSaida(String nomeDivisao) {
        if (nomeDivisao == null || nomeDivisao.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisão para entrada/saída inválido.");
            return;
        }

        Divisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            divisao.setEntradaSaida(true);
            System.out.println("Divisão '" + nomeDivisao + "' marcada como entrada/saída.");
        } else {
            System.err.println("Erro: Divisão '" + nomeDivisao + "' não encontrada.");
        }
    }

    @Override
    public Divisao getDivisaoPorNome(String nomeDivisao) {
        if (nomeDivisao == null || nomeDivisao.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da divisão não pode ser vazio ou nulo.");
        }

        Iterator<Divisao> iterator = grafo.iterator();
        while (iterator.hasNext()) {
            Divisao divisao = iterator.next();
            if (divisao.getNomeDivisao().equalsIgnoreCase(nomeDivisao.trim())) {
                return divisao;
            }
        }

        throw new RuntimeException("Divisão '" + nomeDivisao + "' não encontrada.");
    }

    @Override
    public boolean podeMover(String divisao1, String divisao2) {
        if (divisao1 == null || divisao2 == null ||
                divisao1.trim().isEmpty() || divisao2.trim().isEmpty()) {
            return false;
        }

        Divisao d1 = getDivisaoPorNome(divisao1);
        Divisao d2 = getDivisaoPorNome(divisao2);
        return d1 != null && d2 != null && grafo.isAdjacent(d1, d2);
    }

    @Override
    public void definirAlvo(String nomeDivisao, String tipo) {
        if (nomeDivisao == null || tipo == null ||
                nomeDivisao.trim().isEmpty() || tipo.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisão ou tipo do alvo inválido.");
            return;
        }

        Divisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            this.alvo = new AlvoImpl(divisao, tipo);
            System.out.println("Alvo definido na divisão " + nomeDivisao + " de tipo " + tipo);
        } else {
            System.err.println("Erro: Divisão '" + nomeDivisao + "' não encontrada para definir o alvo.");
        }
    }

    @Override
    public void removerAlvo() {
        if (this.alvo != null) {
            System.out.println("Alvo removido do mapa.");
            this.alvo = null;
        } else {
            System.out.println("Nenhum alvo para remover.");
        }
    }

    @Override
    public Alvo getAlvo() {
        return this.alvo;
    }

    @Override
    public ArrayUnorderedList<Divisao> obterConexoes(Divisao divisao) {
        ArrayUnorderedList<Divisao> conexoes = new ArrayUnorderedList<>();
        if (divisao == null) {
            return conexoes;
        }
        Iterator<Divisao> iterator = grafo.iteratorBFS(divisao);
        while (iterator.hasNext()) {
            Divisao conexao = iterator.next();
            if (conexao != null && !conexao.equals(divisao)) {
                conexoes.addToRear(conexao);
            }
        }
        return conexoes;
    }

    @Override
    public ArrayUnorderedList<Divisao> getDivisoes() {
        ArrayUnorderedList<Divisao> divisoes = new ArrayUnorderedList<>();
        Iterator<Divisao> iterator = grafo.iterator();
        while (iterator.hasNext()) {
            Divisao divisao = iterator.next();
            if (divisao != null) {
                divisoes.addToRear(divisao);
            }
        }
        return divisoes;
    }

    @Override
    public ArrayUnorderedList<String> getEntradasSaidasNomes() {
        ArrayUnorderedList<String> entradasSaidas = new ArrayUnorderedList<>();
        ArrayUnorderedList<Divisao> divisoes = getDivisoes();
        for (int i = 0; i < divisoes.size(); i++) {
            Divisao divisao = divisoes.getElementAt(i);
            if (divisao != null && divisao.isEntradaSaida()) {
                entradasSaidas.addToRear(divisao.getNomeDivisao());
            }
        }
        return entradasSaidas;
    }

    @Override
    public void moverInimigos(ToCruz toCruz, CombateService combateService) throws ElementNotFoundException {
        Random random = new Random();
        ArrayUnorderedList<Divisao> divisoes = getDivisoes();

        if (divisoes == null || divisoes.isEmpty()) {
            throw new IllegalStateException("Nenhuma divisão disponível para mover inimigos.");
        }

        for (int i = 0; i < divisoes.size(); i++) {
            Divisao divisaoAtual = divisoes.getElementAt(i);
            if (divisaoAtual == null)
                continue;

            ArrayUnorderedList<Inimigo> inimigos = divisaoAtual.getInimigosPresentes();
            if (inimigos == null || inimigos.isEmpty())
                continue;

            ArrayUnorderedList<Inimigo> inimigosCopy = new ArrayUnorderedList<>();
            for (int j = 0; j < inimigos.size(); j++) {
                inimigosCopy.addToRear(inimigos.getElementAt(j));
            }

            for (int j = 0; j < inimigosCopy.size(); j++) {
                Inimigo inimigo = inimigosCopy.getElementAt(j);
                if (inimigo == null)
                    continue;

                Divisao origem = divisaoAtual;
                Divisao destino = origem;

                // Movimentar até 2 divisões aleatoriamente
                for (int movimentos = 0; movimentos < 2; movimentos++) {
                    ArrayUnorderedList<Divisao> conexoes = obterConexoes(destino);
                    if (conexoes.isEmpty())
                        break;

                    Divisao novaDivisao = conexoes.getElementAt(random.nextInt(conexoes.size()));
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

                    // Verificar se o inimigo entrou na sala de Tó Cruz
                    if (destino.equals(toCruz.getPosicaoAtual())) {
                        System.out.println("⚔️ Inimigo entrou na sala de Tó Cruz! Combate iniciado.");
                        combateService.resolverCombate(toCruz, destino);

                        // Verificar se Tó Cruz foi derrotado
                        if (toCruz.getVida() <= 0) {
                            System.err.println("💀 Tó Cruz foi derrotado durante o ataque dos inimigos!");
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public ArrayUnorderedList<Divisao> calcularMelhorCaminho(Divisao origem, Divisao destino)
            throws ElementNotFoundException {
        if (origem == null || destino == null) {
            System.err.println("Erro: Origem ou destino inválidos.");
            return new ArrayUnorderedList<>();
        }

        // Estruturas de suporte
        LinkedQueue<Divisao> fila = new LinkedQueue<>();
        ArrayUnorderedList<Divisao> visitados = new ArrayUnorderedList<>();
        ArrayUnorderedList<Predecessor> predecessores = new ArrayUnorderedList<>();
        ArrayUnorderedList<Integer> custos = new ArrayUnorderedList<>(); // Custos paralelos aos visitados

        // Inicialização
        fila.enqueue(origem);
        visitados.addToRear(origem);
        predecessores.addToRear(new Predecessor(origem, null));
        custos.addToRear(0); // Custo inicial é zero

        while (!fila.isEmpty()) {
            // Retirar o próximo nó da fila
            Divisao atual = fila.dequeue();

            // Encontrar índice do nó atual em `visitados`
            int indiceAtual = findIndex(visitados, atual);
            int custoAtual = custos.getElementAt(indiceAtual);

            // Se chegamos ao destino, reconstruir o caminho
            if (atual.equals(destino)) {
                ArrayUnorderedList<Divisao> caminho = new ArrayUnorderedList<>();
                reconstruirCaminho(predecessores, destino, caminho);
                return caminho;
            }

            // Obter conexões da divisão atual
            ArrayUnorderedList<Divisao> conexoes = obterConexoes(atual);
            if (conexoes == null || conexoes.isEmpty()) {
                continue;
            }

            // Processar conexões
            for (int i = 0; i < conexoes.size(); i++) {
                Divisao vizinho = conexoes.getElementAt(i);
                if (vizinho == null)
                    continue;

                // Calcular custo para alcançar o vizinho
                int custoMovimento = calcularCusto(atual, vizinho);
                int novoCusto = custoAtual + custoMovimento;

                // Verificar se o vizinho já foi visitado
                int indiceVizinho = findIndex(visitados, vizinho);
                if (indiceVizinho == -1) { // Não visitado
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
                .println("Caminho não encontrado entre " + origem.getNomeDivisao() + " e " + destino.getNomeDivisao());
        return new ArrayUnorderedList<>();
    }

    private int findIndex(ArrayUnorderedList<Divisao> list, Divisao target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.getElementAt(i).equals(target)) {
                return i;
            }
        }
        return -1; // Retorna -1 se não encontrado
    }

    private void replaceElementAt(ArrayUnorderedList<Integer> list, int index, int value) {
        try {
            list.remove(list.getElementAt(index)); // Remove o elemento atual
            if (index == 0) {
                list.addToFront(value); // Adiciona na frente se for o primeiro elemento
            } else {
                list.addAfter(value, list.getElementAt(index - 1)); // Adiciona após o anterior
            }
        } catch (ElementNotFoundException e) {
            System.err.println("Erro ao substituir elemento na lista: " + e.getMessage());
        }
    }

    /**
     * Calcula o custo de mover-se de uma divisão para outra.
     */
    private int calcularCusto(Divisao atual, Divisao vizinho) {
        int custo = 0;

        // Adicionar custo por inimigos (usando o poder dos inimigos)
        ArrayUnorderedList<Inimigo> inimigos = vizinho.getInimigosPresentes();
        if (inimigos != null && !inimigos.isEmpty()) {
            for (int i = 0; i < inimigos.size(); i++) {
                custo += inimigos.getElementAt(i).getPoder(); // Usar `getPoder()` como dano
            }
        }

        // Subtrair custo por itens de recuperação (usando os pontos do item)
        ArrayUnorderedList<Item> itens = vizinho.getItensPresentes();
        if (itens != null && !itens.isEmpty()) {
            for (int i = 0; i < itens.size(); i++) {
                Item item = itens.getElementAt(i);
                if ("kit de vida".equalsIgnoreCase(item.getTipo())) {
                    custo -= item.getPontos(); // Usar `getPontos()` para recuperação
                }
            }
        }

        return Math.max(custo, 0); // Evitar custos negativos
    }

    private void reconstruirCaminho(ArrayUnorderedList<Predecessor> predecessores, Divisao objetivo,
            ArrayUnorderedList<Divisao> caminho) {
        LinkedStack<Divisao> pilha = new LinkedStack<>();
        Divisao atual = objetivo;

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

    private Divisao getPredecessor(ArrayUnorderedList<Predecessor> predecessores, String nomeDivisao) {
        for (int i = 0; i < predecessores.size(); i++) {
            Predecessor p = predecessores.getElementAt(i);
            if (p != null && p.getAtual().getNomeDivisao().equalsIgnoreCase(nomeDivisao)) {
                return p.getPredecessor();
            }
        }
        return null;
    }

    @Override
    public ArrayUnorderedList<Item> getItensPorTipo(String tipo) {
        ArrayUnorderedList<Item> itens = new ArrayUnorderedList<>();
        ArrayUnorderedList<Divisao> divisoes = getDivisoes();

        for (int i = 0; i < divisoes.size(); i++) {
            Divisao divisao = divisoes.getElementAt(i);
            if (divisao != null) {
                ArrayUnorderedList<Item> itensDivisao = divisao.getItensPresentes();
                for (int j = 0; j < itensDivisao.size(); j++) {
                    Item item = itensDivisao.getElementAt(j);
                    if (item != null && item.getTipo().equalsIgnoreCase(tipo)) {
                        itens.addToRear(item);
                    }
                }
            }
        }

        return itens;
    }

    @Override
    public void mostrarMapa() {
        System.out.println("===== MAPA DO EDIFÍCIO =====");
        ArrayUnorderedList<Divisao> divisoes = getDivisoes();

        for (int i = 0; i < divisoes.size(); i++) {
            Divisao divisao = divisoes.getElementAt(i);
            if (divisao == null)
                continue;

            // Obter informações da divisão
            ArrayUnorderedList<Inimigo> inimigos = divisao.getInimigosPresentes();
            ArrayUnorderedList<Item> itens = divisao.getItensPresentes();

            // Exibir o nome da divisão com símbolos adicionais
            System.out.print("📍 " + divisao.getNomeDivisao());

            if (inimigos != null && !inimigos.isEmpty()) {
                System.out.print(" ⚔️ (" + inimigos.size() + " inimigos)");
            }
            if (itens != null && !itens.isEmpty()) {
                System.out.print(" 🎒 (" + itens.size() + " itens)");
            }
            if (divisao.isEntradaSaida()) {
                System.out.print(" 🚪 [Entrada/Saída]");
            }

            System.out.println();

            // Obter as conexões
            ArrayUnorderedList<Divisao> conexoes = obterConexoes(divisao);
            if (conexoes.isEmpty()) {
                System.out.println("   ↳ Sem conexões");
            } else {
                for (int j = 0; j < conexoes.size(); j++) {
                    Divisao conexao = conexoes.getElementAt(j);
                    System.out.println("   ↳ Conecta com: " + conexao.getNomeDivisao());
                }
            }
            System.out.println();
        }
        System.out.println("=============================");
    }

}
