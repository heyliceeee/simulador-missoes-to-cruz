package org.example.api.implementation.models;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.*;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.Graph;
import org.example.collections.implementation.LinkedList;

import java.util.Iterator;
import java.util.Random;

import static org.example.api.implementation.simulation.SimulacaoAutomaticaImpl.pin;

/**
 * Representa o mapa do edificio como um grafo.
 */
public class MapaImpl implements IMapa {
    /**
     * Grafo que representa o edificio e suas conexões.
     */
    private Graph<IDivisao> grafo;

    /**
     * Informacões sobre o alvo da missao.
     */
    private IAlvo alvo;

    /**
     * Lista de divisões que sao entradas ou saidas do edificio.
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
        // System.out.println("Divisao adicionada: " + nomeDivisao);
    }

    /**
     * Adiciona uma ligacao entre duas divisões.
     *
     * @param nomeDivisao1 Nome da primeira divisao.
     * @param nomeDivisao2 Nome da segunda divisao.
     */
    @Override
    public void adicionarLigacao(String nomeDivisao1, String nomeDivisao2) {
        if (nomeDivisao1 == null || nomeDivisao2 == null ||
                nomeDivisao1.trim().isEmpty() || nomeDivisao2.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomes das divisões nao podem ser vazios ou nulos.");
        }

        IDivisao divisao1 = getDivisaoPorNome(nomeDivisao1);
        IDivisao divisao2 = getDivisaoPorNome(nomeDivisao2);

        if (grafo.isAdjacent(divisao1, divisao2)) {
            System.out.println("Ligacao ja existente entre " + nomeDivisao1 + " e " + nomeDivisao2);
            return;
        }

        grafo.addEdge(divisao1, divisao2);
        // System.out.println("Ligacao adicionada entre " + nomeDivisao1 + " e " +
        // nomeDivisao2);
    }

    /**
     * Obtem todas as conexões (arestas) do grafo.
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
            // System.out.println("Inimigo '" + inimigo.getNome() + "' adicionado à divisao:
            // " + nomeDivisao);
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
            divisao.adicionarItem(item);
            // System.out.println("Item '" + item.getTipo() + "' adicionado à divisao: " +
            // nomeDivisao);
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
            if (divisao.getNomeDivisao().equalsIgnoreCase(nomeDivisao.trim())) {
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
     * @return true se for possivel mover, false caso contrário.
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
     * @param nomeDivisao Nome da divisao onde o alvo está localizado.
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
     * Obtem todas as conexões (divisões acessiveis) a partir de uma divisao
     * especifica.
     *
     * @param divisao Divisao de origem.
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
     * Obtem todas as divisões presentes no grafo.
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
     * Obtem os nomes das divisões que sao entradas ou saidas.
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
     * Obtem as divisões que sao entradas ou saidas.
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
     * Move os inimigos aleatoriamente para divisões conectadas ate duas divisões de
     * distância.
     */
    @Override
    public void moverInimigos() throws ElementNotFoundException {
        Random random = new Random();
        ArrayUnorderedList<IDivisao> divisoes = getDivisoes();

        if (divisoes == null || divisoes.isEmpty()) {
            throw new IllegalStateException("Nenhuma divisao disponivel para mover inimigos.");
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
                            "Nenhuma conexao disponivel para mover o inimigo '" + inimigo.getNome() + "'.");
                }

                IDivisao novaDivisao = conexoes.getElementAt(random.nextInt(conexoes.size()));
                if (novaDivisao != null) {
                    novaDivisao.adicionarInimigo(inimigo);
                    divisao.removerInimigo(inimigo);
                    System.out
                            .println("Inimigo '" + inimigo.getNome() + "' movido para " + novaDivisao.getNomeDivisao());
                } else {
                    throw new IllegalStateException("A conexao selecionada e invalida (nula).");
                }
            }
        }
    }

    /**
     * Mostra o mapa do edificio, exibindo as divisões (vertices) e suas conexões
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

            // Exibir o nome da divisao principal
            System.out.println(pin + divisao.getNomeDivisao());

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
     * Expande as conexões de uma divisao para incluir divisões conectadas a ate
     * duas conexões de distância.
     *
     * @param divisaoAtual A divisao atual de onde partirá a expansao.
     * @return Uma lista de divisões acessiveis a ate duas conexões de distância.
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

                // Adiciona a conexao se ela nao estiver na lista inicial e nao for a própria
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
     * Encontrar o melhor caminho ate o alvo
     * 
     * @param origem  divisao atual
     * @param destino divisao destino
     * @return o percurso de divisoes
     */
    @Override
    public ArrayUnorderedList<IDivisao> calcularMelhorCaminho(IDivisao origem, IDivisao destino) {
        if (origem == null || destino == null) {
            throw new IllegalArgumentException("Origem ou destino invalido");
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
