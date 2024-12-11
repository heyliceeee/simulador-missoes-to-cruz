package org.example.api.implementation.models;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.Alvo;
import org.example.api.implementation.interfaces.Divisao;
import org.example.api.implementation.interfaces.Inimigo;
import org.example.api.implementation.interfaces.Item;
import org.example.api.implementation.interfaces.Mapa;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.Graph;

import java.util.HashMap;
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
        //System.out.println("Divisão adicionada: " + nomeDivisao);
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
        //System.out.println("Ligação adicionada entre " + nomeDivisao1 + " e " + nomeDivisao2);
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
            //System.out.println("Inimigo '" + inimigo.getNome() + "' adicionado à divisão: " + nomeDivisao);
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
            divisao.adicionarItem(item);
            //System.out.println("Item '" + item.getTipo() + "' adicionado à divisão: " + nomeDivisao);
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
    public void moverInimigos() throws ElementNotFoundException {
        Random random = new Random();
        ArrayUnorderedList<Divisao> divisoes = getDivisoes();
    
        if (divisoes == null || divisoes.isEmpty()) {
            throw new IllegalStateException("Nenhuma divisão disponível para mover inimigos.");
        }
    
        for (int i = 0; i < divisoes.size(); i++) {
            Divisao divisao = divisoes.getElementAt(i);
            if (divisao == null) continue;
    
            ArrayUnorderedList<Inimigo> inimigos = divisao.getInimigosPresentes();
            if (inimigos == null || inimigos.isEmpty()) continue;
    
            ArrayUnorderedList<Inimigo> inimigosCopy = new ArrayUnorderedList<>();
            for (int j = 0; j < inimigos.size(); j++) {
                inimigosCopy.addToRear(inimigos.getElementAt(j));
            }
    
            for (int j = 0; j < inimigosCopy.size(); j++) {
                Inimigo inimigo = inimigosCopy.getElementAt(j);
                if (inimigo == null) continue;
    
                ArrayUnorderedList<Divisao> conexoes = obterConexoes(divisao);
                if (conexoes == null || conexoes.isEmpty()) {
                    throw new IllegalStateException(
                        "Nenhuma conexão disponível para mover o inimigo '" + inimigo.getNome() + "'.");
                }
    
                Divisao novaDivisao = conexoes.getElementAt(random.nextInt(conexoes.size()));
                if (novaDivisao != null) {
                    novaDivisao.adicionarInimigo(inimigo);
                    divisao.removerInimigo(inimigo);
                    System.out.println("Inimigo '" + inimigo.getNome() + "' movido para " + novaDivisao.getNomeDivisao());
                } else {
                    throw new IllegalStateException("A conexão selecionada é inválida (nula).");
                }
            }
        }
    }
    

    @Override
    public void mostrarMapa() {
        System.out.println("===== MAPA DO EDIFÍCIO =====");
        ArrayUnorderedList<Divisao> divisoes = getDivisoes();
    
        for (int i = 0; i < divisoes.size(); i++) {
            Divisao divisao = divisoes.getElementAt(i);
            if (divisao == null) continue;
    
            // Exibir o nome da divisão principal
            System.out.println("📍 " + divisao.getNomeDivisao());
    
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
