package org.example.api.implementation.models;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.*;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.Graph;
import org.example.collections.implementation.LinkedList;

import java.util.Iterator;
import java.util.Random;

/**
 * Representa o mapa do edifício como um grafo.
 */
public class MapaImpl implements IMapa {
    /**
     * Grafo que representa o edifício e suas conexões.
     */
    private Graph<IDivisao> grafo;

    /**
     * Informações sobre o alvo da missão.
     */
    private IAlvo alvo;

    /**
     * Lista de divisões que são entradas ou saídas do edifício.
     */
    private ArrayUnorderedList<IDivisao> entradasSaidas;

    /**
     * Construtor padrão do Mapa.
     */
    public MapaImpl() {
        this.grafo = new Graph<>();
        this.entradasSaidas = new ArrayUnorderedList<>();
    }

    /**
     * Adiciona uma divisão ao mapa.
     *
     * @param nomeDivisao Nome da divisão.
     */
    @Override
    public void adicionarDivisao(String nomeDivisao) {
        if (nomeDivisao == null || nomeDivisao.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisão inválido.");
            return;
        }
        IDivisao divisao = new DivisaoImpl(nomeDivisao);
        grafo.addVertex(divisao);
        // System.out.println("Divisão adicionada: " + nomeDivisao);
    }

    /**
     * Adiciona uma ligação entre duas divisões.
     *
     * @param nomeDivisao1 Nome da primeira divisão.
     * @param nomeDivisao2 Nome da segunda divisão.
     */
    @Override
    public void adicionarLigacao(String nomeDivisao1, String nomeDivisao2) {
        if (nomeDivisao1 == null || nomeDivisao2 == null ||
                nomeDivisao1.trim().isEmpty() || nomeDivisao2.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomes das divisões não podem ser vazios ou nulos.");
        }

        IDivisao divisao1 = getDivisaoPorNome(nomeDivisao1);
        IDivisao divisao2 = getDivisaoPorNome(nomeDivisao2);

        if (grafo.isAdjacent(divisao1, divisao2)) {
            System.out.println("Ligacao ja existente entre " + nomeDivisao1 + " e " + nomeDivisao2);
            return;
        }

        grafo.addEdge(divisao1, divisao2);
        // System.out.println("Ligação adicionada entre " + nomeDivisao1 + " e " +
        // nomeDivisao2);
    }

    /**
     * Obtém todas as conexões (arestas) do grafo.
     *
     * @return Uma lista de conexões entre divisões.
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
     * Adiciona um inimigo a uma divisão.
     *
     * @param nomeDivisao Nome da divisão.
     * @param inimigo     Inimigo a ser adicionado.
     */
    @Override
    public void adicionarInimigo(String nomeDivisao, IInimigo inimigo) {
        if (nomeDivisao == null || inimigo == null || nomeDivisao.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisão ou inimigo inválido.");
            return;
        }

        IDivisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            divisao.adicionarInimigo(inimigo);
            // System.out.println("Inimigo '" + inimigo.getNome() + "' adicionado à divisão:
            // " + nomeDivisao);
        } else {
            System.err.println("Erro: Divisão '" + nomeDivisao + "' não encontrada.");
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
            System.err.println("Erro: Nome da divisão ou item inválido.");
            return;
        }

        IDivisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            divisao.adicionarItem(item);
            // System.out.println("Item '" + item.getTipo() + "' adicionado à divisão: " +
            // nomeDivisao);
        } else {
            System.err.println("Erro: Divisão '" + nomeDivisao + "' não encontrada.");
        }
    }

    /**
     * Adiciona uma divisão como entrada ou saída.
     *
     * @param nomeDivisao Nome da divisão.
     */
    @Override
    public void adicionarEntradaSaida(String nomeDivisao) {
        if (nomeDivisao == null || nomeDivisao.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisão para entrada/saída inválido.");
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
     * Obtém uma divisão pelo nome.
     *
     * @param nomeDivisao Nome da divisão.
     * @return A divisão encontrada ou null.
     */
    @Override
    public IDivisao getDivisaoPorNome(String nomeDivisao) {
        if (nomeDivisao == null || nomeDivisao.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da divisão não pode ser vazio ou nulo.");
        }

        Iterator<IDivisao> iterator = grafo.iterator();
        while (iterator.hasNext()) {
            IDivisao divisao = iterator.next();
            if (divisao.getNomeDivisao().equalsIgnoreCase(nomeDivisao.trim())) {
                return divisao;
            }
        }

        throw new RuntimeException("Divisão '" + nomeDivisao + "' não encontrada.");
    }

    /**
     * Verifica se é possível mover de uma divisão para outra.
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
     * Define o alvo da missão.
     *
     * @param nomeDivisao Nome da divisão onde o alvo está localizado.
     * @param tipo        Tipo do alvo.
     */
    @Override
    public void definirAlvo(String nomeDivisao, String tipo) {
        if (nomeDivisao == null || tipo == null ||
                nomeDivisao.trim().isEmpty() || tipo.trim().isEmpty()) {
            System.err.println("Erro: Nome da divisão ou tipo do alvo inválido.");
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
     * @return Alvo da missão.
     */
    @Override
    public IAlvo getAlvo() {
        return this.alvo;
    }

    /**
     * Obtém todas as conexões (divisões acessíveis) a partir de uma divisão
     * específica.
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
     * Obtém todas as divisões presentes no grafo.
     *
     * @return Lista de todas as divisões.
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
     * Obtém os nomes das divisões que são entradas ou saídas.
     *
     * @return Lista de nomes das entradas e saídas.
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
     * Obtém as divisões que são entradas ou saídas.
     *
     * @return Lista de entradas e saídas.
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
     * Move os inimigos aleatoriamente para divisões conectadas até duas divisões de
     * distância.
     */
    @Override
    public void moverInimigos() throws ElementNotFoundException {
        Random random = new Random();
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();

        if (divisoes == null || divisoes.isEmpty()) {
            throw new IllegalStateException("Nenhuma divisão disponível para mover inimigos.");
        }

        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao divisao = divisoes.getElementAt(i);
            if (divisao == null)
                continue;

            ArrayUnorderedList<IInimigo> inimigos = divisao.getInimigosPresentes();
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

                ArrayUnorderedList<IDivisao> conexoes = obterConexoes(divisao);
                if (conexoes == null || conexoes.isEmpty()) {
                    throw new IllegalStateException(
                            "Nenhuma conexão disponível para mover o inimigo '" + inimigo.getNome() + "'.");
                }

                IDivisao novaDivisao = conexoes.getElementAt(random.nextInt(conexoes.size()));
                if (novaDivisao != null) {
                    novaDivisao.adicionarInimigo(inimigo);
                    divisao.removerInimigo(inimigo);
                    System.out
                            .println("Inimigo '" + inimigo.getNome() + "' movido para " + novaDivisao.getNomeDivisao());
                } else {
                    throw new IllegalStateException("A conexão selecionada é inválida (nula).");
                }
            }
        }
    }

    /**
     * Mostra o mapa do edifício, exibindo as divisões (vértices) e suas conexões
     * (arestas).
     */
    @Override
    public void mostrarMapa() {
        System.out.println("===== MAPA DO EDIFICIO =====");
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();

        for (int i = 0; i < divisoes.size(); i++) {
            IDivisao divisao = divisoes.getElementAt(i);
            if (divisao == null)
                continue;

            // Exibir o nome da divisão principal
            System.out.println("\uD83D\uDCCD " + divisao.getNomeDivisao());

            // Obter as conexões
            ArrayUnorderedList<IDivisao> conexoes = obterConexoes(divisao);
            if (conexoes.isEmpty()) {
                System.out.println("   ↳ Sem conexões");
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
     * Obtém o primeiro vértice (divisão) no grafo.
     *
     * @return A primeira divisão ou null se o grafo estiver vazio.
     */
    @Override
    public IDivisao getPrimeiroVertice() {
        Iterator<IDivisao> iterator = grafo.iterator(); // Usar o iterador padrão
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * Expande as conexões de uma divisão para incluir divisões conectadas a até
     * duas conexões de distância.
     *
     * @param divisaoAtual A divisão atual de onde partirá a expansão.
     * @return Uma lista de divisões acessíveis a até duas conexões de distância.
     */
    @Override
    public ArrayUnorderedList<IDivisao> expandirConexoes(IDivisao divisaoAtual) {
        ArrayUnorderedList<IDivisao> conexoesDiretas = obterConexoes(divisaoAtual);
        ArrayUnorderedList<IDivisao> conexoesExpandida = new ArrayUnorderedList<>();

        // Adiciona conexões de segunda distância
        for (int i = 0; i < conexoesDiretas.size(); i++) {
            IDivisao conexao = conexoesDiretas.getElementAt(i);
            ArrayUnorderedList<IDivisao> conexoesSegundaDistancia = obterConexoes(divisaoAtual);

            for (int j = 0; j < conexoesSegundaDistancia.size(); j++) {
                IDivisao segundaConexao = conexoesSegundaDistancia.getElementAt(j);

                // Adiciona a conexão se ela não estiver na lista inicial e não for a própria
                // divisão atual
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
     * Encontrar o melhor caminho ate o alvo
     * 
     * @param origem  divisao atual
     * @param destino divisao destino
     * @return o percurso de divisoes
     */
    @Override
    public ArrayUnorderedList<IDivisao> calcularMelhorCaminho(IDivisao origem, IDivisao destino) {
        if (origem == null || destino == null) {
            throw new IllegalArgumentException("Origem ou destino inválido");
        }

        Iterator<IDivisao> caminhoIterator = grafo.iteratorShortestPath(origem, destino);
        ArrayUnorderedList<IDivisao> caminho = new ArrayUnorderedList<>();

        while (caminhoIterator.hasNext()) {
            caminho.addToRear(caminhoIterator.next());
        }

        return caminho;
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
            throw new IllegalArgumentException("Origem inválida");
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

            ArrayUnorderedList<IDivisao> adjacentes = obterConexoes(atual);
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
}
