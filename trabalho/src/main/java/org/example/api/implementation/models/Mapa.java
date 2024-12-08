package org.example.api.implementation.models;

import java.util.Iterator;
import java.util.Random;

import org.example.collections.implementation.Graph;
import org.example.collections.implementation.LinkedList;

/**
 * Representa o mapa do edifício como um grafo.
 */
public class Mapa {
    /**
     * Grafo que representa o edifício e suas conexões.
     */
    private Graph<Divisao> grafo;

    /**
     * Lista de divisões que são entradas ou saídas do edifício.
     */
    private LinkedList<Divisao> entradasSaidas;

    /**
     * Informações sobre o alvo da missão.
     */
    private Alvo alvo;

    /**
     * Construtor padrão do Mapa.
     */
    public Mapa() {
        this.grafo = new Graph<>();
        this.entradasSaidas = new LinkedList<>();
    }

    /**
     * Adiciona uma divisão ao mapa.
     *
     * @param nomeDivisao Nome da divisão.
     */
    public void adicionarDivisao(String nomeDivisao) {
        Divisao divisao = new Divisao(nomeDivisao);
        grafo.addVertex(divisao);
        // Log já está dentro do método addVertex através adicionarDivisao
    }

    /**
     * Adiciona uma ligação entre duas divisões.
     *
     * @param nomeDivisao1 Nome da primeira divisão.
     * @param nomeDivisao2 Nome da segunda divisão.
     */
    public void adicionarLigacao(String nomeDivisao1, String nomeDivisao2) {
        Divisao divisao1 = getDivisaoPorNome(nomeDivisao1);
        Divisao divisao2 = getDivisaoPorNome(nomeDivisao2);

        if (divisao1 == null) {
            System.err.println("Erro: Divisão de origem não encontrada: " + nomeDivisao1);
            return;
        }
        if (divisao2 == null) {
            System.err.println("Erro: Divisão de destino não encontrada: " + nomeDivisao2);
            return;
        }

        grafo.addEdge(divisao1, divisao2);
        // Log já está dentro do método addEdge
    }

    /**
     * Adiciona uma divisão como entrada ou saída.
     *
     * @param nomeDivisao Nome da divisão.
     */
    public void adicionarEntradaSaida(String nomeDivisao) {
        Divisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            entradasSaidas.add(divisao);
            System.out.println("Divisão adicionada como entrada/saída: " + nomeDivisao);
        } else {
            System.err.println("Erro: Divisão de entrada/saída não encontrada: " + nomeDivisao);
        }
    }

    /**
     * Adiciona um item a uma divisão.
     *
     * @param nomeDivisao Nome da divisão.
     * @param item        Item a ser adicionado.
     */
    public void adicionarItem(String nomeDivisao, Item item) {
        Divisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            divisao.adicionarItem(item);
            System.out.println("Item adicionado à divisão: " + nomeDivisao);
        } else {
            System.err.println("Erro: Divisão para adicionar o item não encontrada: " + nomeDivisao);
        }
    }

    /**
     * Adiciona um inimigo a uma divisão.
     *
     * @param nomeDivisao Nome da divisão.
     * @param inimigo     Inimigo a ser adicionado.
     */
    public void adicionarInimigo(String nomeDivisao, Inimigo inimigo) {
        Divisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            divisao.adicionarInimigo(inimigo);
            System.out.println("Inimigo adicionado à divisão: " + nomeDivisao);
        } else {
            System.err.println("Erro: Divisão para adicionar o inimigo não encontrada: " + nomeDivisao);
        }
    }

    /**
     * Define o alvo da missão.
     *
     * @param nomeDivisao Nome da divisão onde o alvo está localizado.
     * @param tipo        Tipo do alvo.
     */
    public void definirAlvo(String nomeDivisao, String tipo) {
        Divisao divisao = getDivisaoPorNome(nomeDivisao);
        if (divisao != null) {
            this.alvo = new Alvo(divisao, tipo);
            System.out.println("Alvo definido na divisão: " + nomeDivisao + ", Tipo: " + tipo);
        } else {
            System.err.println("Erro: Divisão para definir o alvo não encontrada: " + nomeDivisao);
        }
    }

    /**
     * Obtém uma divisão pelo nome.
     *
     * @param nomeDivisao Nome da divisão.
     * @return A divisão encontrada ou null.
     */
    public Divisao getDivisaoPorNome(String nomeDivisao) {
        if (grafo.isEmpty()) {
            return null;
        }

        Iterator<Divisao> iterator = grafo.iterator(); // Usar o iterador padrão
        while (iterator.hasNext()) {
            Divisao divisao = iterator.next();
            if (divisao.getNomeDivisao().equals(nomeDivisao)) {
                return divisao;
            }
        }
        return null;
    }

    /**
     * Verifica se é possível mover de uma divisão para outra.
     *
     * @param divisao1 Nome da divisão de origem.
     * @param divisao2 Nome da divisão de destino.
     * @return true se for possível mover, false caso contrário.
     */
    public boolean podeMover(String divisao1, String divisao2) {
        Divisao d1 = getDivisaoPorNome(divisao1);
        Divisao d2 = getDivisaoPorNome(divisao2);
        if (d1 == null || d2 == null) {
            return false;
        }
        Iterator<Divisao> conexoes = grafo.iteratorBFS(d1);
        while (conexoes.hasNext()) {
            if (conexoes.next().equals(d2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtém todas as conexões (divisões acessíveis) a partir de uma divisão
     * específica.
     *
     * @param divisao Divisão de origem.
     * @return Lista de divisões conectadas.
     */
    public LinkedList<Divisao> obterConexoes(Divisao divisao) {
        LinkedList<Divisao> conexoes = new LinkedList<>();
        Iterator<Divisao> iterator = grafo.iteratorBFS(divisao);
        while (iterator.hasNext()) {
            Divisao conexao = iterator.next();
            conexoes.add(conexao);
            // System.out.println("Conexão encontrada: " + conexao.getNomeDivisao());
        }
        return conexoes;
    }

    /**
     * Obtém todas as divisões presentes no grafo.
     *
     * @return Lista de todas as divisões.
     */
    public LinkedList<Divisao> getDivisoes() {
        LinkedList<Divisao> divisoes = new LinkedList<>();
        Iterator<Divisao> iterator = grafo.iterator(); // Usar o iterador padrão
        while (iterator.hasNext()) {
            Divisao divisao = iterator.next();
            divisoes.add(divisao);
        }
        return divisoes;
    }

    /**
     * Obtém os nomes das divisões que são entradas ou saídas.
     *
     * @return Lista de nomes das entradas e saídas.
     */
    public LinkedList<String> getEntradasSaidasNomes() {
        LinkedList<String> nomesEntradasSaidas = new LinkedList<>();
        for (int i = 0; i < entradasSaidas.getSize(); i++) {
            nomesEntradasSaidas.add(entradasSaidas.getElementAt(i).getNomeDivisao());
        }
        return nomesEntradasSaidas;
    }

    /**
     * Obtém o primeiro vértice (divisão) no grafo.
     *
     * @return A primeira divisão ou null se o grafo estiver vazio.
     */
    private Divisao getPrimeiroVertice() {
        Iterator<Divisao> iterator = grafo.iterator(); // Usar o iterador padrão
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * Mostra o mapa do edifício (matriz de adjacência e valores dos vértices).
     */
    /*
     * public void mostrarMapa() {
     * System.out.println(grafo.toString());
     * }
     */

    /**
     * Mostra o mapa do edifício, exibindo as divisões (vértices) e suas conexões
     * (arestas).
     */
    public void mostrarMapa() {
        System.out.println("===== MAPA DO EDIFÍCIO =====");

        for (Divisao divisao : getDivisoes()) {
            System.out.print("Divisão: " + divisao.getNomeDivisao() + " -> Conectada com: ");

            LinkedList<Divisao> conexoes = obterConexoes(divisao);
            if (conexoes.isEmpty()) {
                System.out.print("Nenhuma conexão");
            } else {
                for (int i = 0; i < conexoes.getSize(); i++) {
                    System.out.print(conexoes.getElementAt(i).getNomeDivisao());
                    if (i < conexoes.getSize() - 1) {
                        System.out.print(", ");
                    }
                }
            }
            System.out.println();
        }

        System.out.println("=============================");
    }

    /**
     * Obtém a lista de entradas e saídas.
     *
     * @return Lista de divisões que são entradas ou saídas.
     */
    public LinkedList<Divisao> getEntradasSaidas() {
        return entradasSaidas;
    }

    /**
     * Obtém o alvo da missão.
     *
     * @return Alvo da missão.
     */
    public Alvo getAlvo() {
        return alvo;
    }

    /**
     * Move os inimigos aleatoriamente para divisões conectadas até duas divisões de
     * distância.
     */
    public void moverInimigos() {
        for (Divisao divisaoAtual : getDivisoes()) {
            LinkedList<Inimigo> inimigos = divisaoAtual.getInimigosPresentes();
            LinkedList<Inimigo> inimigosMovidos = new LinkedList<>();

            // Processa cada inimigo presente na divisão atual
            for (int i = 0; i < inimigos.getSize(); i++) {
                Inimigo inimigo = inimigos.getElementAt(i);

                // Obtém divisões conectadas e expande até duas conexões de distância
                LinkedList<Divisao> possiveisMovimentos = expandirConexoes(divisaoAtual);

                // Escolhe aleatoriamente uma nova divisão
                if (!possiveisMovimentos.isEmpty()) {
                    Random random = new Random();
                    Divisao novaDivisao = possiveisMovimentos
                            .getElementAt(random.nextInt(possiveisMovimentos.getSize()));

                    // Move o inimigo para a nova divisão
                    novaDivisao.adicionarInimigo(inimigo);
                    inimigosMovidos.add(inimigo); // Marca o inimigo para remoção após a iteração
                    System.out.println("Inimigo " + inimigo.getNome() + " movido para " + novaDivisao.getNomeDivisao());
                }
            }

            // Remove os inimigos que foram movidos da divisão atual
            for (int i = 0; i < inimigosMovidos.getSize(); i++) {
                divisaoAtual.removerInimigo(inimigosMovidos.getElementAt(i));
            }
        }
    }

    /**
     * Expande as conexões de uma divisão para incluir divisões conectadas a até
     * duas conexões de distância.
     *
     * @param divisaoAtual A divisão atual de onde partirá a expansão.
     * @return Uma lista de divisões acessíveis a até duas conexões de distância.
     */
    private LinkedList<Divisao> expandirConexoes(Divisao divisaoAtual) {
        LinkedList<Divisao> conexoesDiretas = obterConexoes(divisaoAtual);
        LinkedList<Divisao> conexoesExpandida = new LinkedList<>();

        // Adiciona conexões de segunda distância
        for (int i = 0; i < conexoesDiretas.getSize(); i++) {
            Divisao conexao = conexoesDiretas.getElementAt(i);
            LinkedList<Divisao> conexoesSegundaDistancia = obterConexoes(conexao);

            for (int j = 0; j < conexoesSegundaDistancia.getSize(); j++) {
                Divisao segundaConexao = conexoesSegundaDistancia.getElementAt(j);

                // Adiciona a conexão se ela não estiver na lista inicial e não for a própria
                // divisão atual
                if (!conexoesDiretas.contains(segundaConexao) && !conexoesExpandida.contains(segundaConexao)
                        && !segundaConexao.equals(divisaoAtual)) {
                    conexoesExpandida.add(segundaConexao);
                }
            }
        }

        // Adiciona conexões diretas à lista expandida
        for (int i = 0; i < conexoesDiretas.getSize(); i++) {
            Divisao conexao = conexoesDiretas.getElementAt(i);
            if (!conexoesExpandida.contains(conexao)) {
                conexoesExpandida.add(conexao);
            }
        }

        return conexoesExpandida;
    }

    /**
     * Remove o alvo do mapa.
     */
    public void removerAlvo() {
        if (alvo != null) {
            System.out.println("Alvo removido do mapa.");
            this.alvo = null;
        } else {
            System.out.println("Nenhum alvo presente no mapa para remover.");
        }
    }

}
