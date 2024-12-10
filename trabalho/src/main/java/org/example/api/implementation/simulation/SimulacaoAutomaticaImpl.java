package org.example.api.implementation.simulation;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.CombateService;
import org.example.api.implementation.interfaces.Divisao;
import org.example.api.implementation.interfaces.Inimigo;
import org.example.api.implementation.interfaces.Item;
import org.example.api.implementation.interfaces.Mapa;
import org.example.api.implementation.interfaces.SimulacaoAutomatica;
import org.example.api.implementation.models.ToCruz;
import org.example.api.implementation.models.Predecessor;
import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.LinkedQueue;
import org.example.collections.implementation.LinkedStack;

/**
 * Gerencia a simulação automática para o Tó Cruz.
 */
public class SimulacaoAutomaticaImpl implements SimulacaoAutomatica {

    private final Mapa mapa;
    private final ToCruz toCruz;
    private final CombateService combateService;
    private final ArrayUnorderedList<Divisao> caminhoPercorrido;
    private final ArrayUnorderedList<Inimigo> inimigosDerrotados;
    private final ArrayUnorderedList<Item> itensColetados;

    public SimulacaoAutomaticaImpl(Mapa mapa, ToCruz toCruz, CombateService combateService) {
        if (mapa == null || toCruz == null || combateService == null) {
            throw new IllegalArgumentException("Mapa, Tó Cruz e CombateService não podem ser nulos.");
        }

        this.mapa = mapa;
        this.toCruz = toCruz;
        this.combateService = combateService;
        this.caminhoPercorrido = new ArrayUnorderedList<>();
        this.inimigosDerrotados = new ArrayUnorderedList<>();
        this.itensColetados = new ArrayUnorderedList<>();
    }

    @Override
    public void executar(Divisao divisaoObjetivo) throws ElementNotFoundException {
        System.out.println("Início da simulação automática!");

        if (divisaoObjetivo == null) {
            System.err.println("Erro: Divisão objetivo não encontrada.");
            return;
        }

        LinkedQueue<Divisao> fila = new LinkedQueue<>();
        ArrayUnorderedList<Divisao> visitados = new ArrayUnorderedList<>();
        ArrayUnorderedList<Predecessor> predecessores = new ArrayUnorderedList<>();

        Divisao posicaoInicial = toCruz.getPosicaoAtual();
        if (posicaoInicial == null) {
            System.err.println("Erro: Posição inicial de Tó Cruz é nula.");
            return;
        }

        fila.enqueue(posicaoInicial);
        visitados.addToRear(posicaoInicial);
        predecessores.addToRear(new Predecessor(posicaoInicial, null));

        boolean objetivoEncontrado = false;

        while (!fila.isEmpty()) {
            Divisao atual = fila.dequeue();

            if (atual.equals(divisaoObjetivo)) {
                System.out.println("Objetivo encontrado: " + atual.getNomeDivisao());
                reconstruirCaminho(predecessores, divisaoObjetivo);
                if (toCruz.getVida() > 0) {
                    verificarTrajetoDeVolta();
                }
                objetivoEncontrado = true;
                break;
            }

            ArrayUnorderedList<Divisao> conexoes = mapa.obterConexoes(atual);
            if (conexoes == null || conexoes.isEmpty()) {
                System.out.println("Aviso: Divisão " + atual.getNomeDivisao() + " não possui conexões.");
                continue;
            }

            for (int i = 0; i < conexoes.size(); i++) {
                Divisao vizinho = conexoes.getElementAt(i);
                if (vizinho == null) continue;

                if (!visitados.contains(vizinho) && mapa.podeMover(atual.getNomeDivisao(), vizinho.getNomeDivisao())) {
                    visitados.addToRear(vizinho);
                    fila.enqueue(vizinho);
                    predecessores.addToRear(new Predecessor(vizinho, atual));
                }
            }
        }

        if (!objetivoEncontrado) {
            System.err.println("Erro: Caminho não encontrado para o objetivo.");
        }
    }

    /**
     * Reconstrói o caminho percorrido a partir dos predecessores.
     *
     * @param predecessores Lista de predecessores para cada divisão.
     * @param objetivo      Divisão objetivo que foi encontrada.
          * @throws ElementNotFoundException 
          */
         private void reconstruirCaminho(ArrayUnorderedList<Predecessor> predecessores, Divisao objetivo) throws ElementNotFoundException {
        LinkedStack<Divisao> caminhoReverso = new LinkedStack<>();
        Divisao atual = objetivo;

        while (atual != null) {
            caminhoReverso.push(atual);
            atual = getPredecessor(predecessores, atual.getNomeDivisao());
        }

        while (!caminhoReverso.isEmpty()) {
            try {
                Divisao divisao = caminhoReverso.pop();
                if (divisao != null) {
                    caminhoPercorrido.addToRear(divisao);
                    moverParaDivisao(divisao);
                    if (toCruz.getVida() <= 0) {
                        System.err.println("Tó Cruz foi derrotado durante a simulação!");
                        return;
                    }
                }
            } catch (EmptyCollectionException e) {
                System.err.println("Erro ao reconstruir o caminho: " + e.getMessage());
                return;
            }
        }
        System.out.println("Tó Cruz alcançou o objetivo com sucesso!");
    }

    /**
     * Move Tó Cruz para a divisão especificada, resolve combates e coleta itens.
     *
     * @param divisao Divisão para onde Tó Cruz deve se mover.
     * @throws ElementNotFoundException 
     */
    private void moverParaDivisao(Divisao divisao) throws ElementNotFoundException {
        if (divisao == null) {
            System.err.println("Erro: Tentativa de mover para uma divisão nula.");
            return;
        }
    
        // Atualiza a posição de Tó Cruz
        toCruz.moverPara(divisao);
    
        // Exibe a posição atual sem duplicação
        System.out.println("🤠 Tó Cruz moveu-se para a divisão: " + divisao.getNomeDivisao());
    
        // Verifica e processa inimigos
        ArrayUnorderedList<Inimigo> inimigos = divisao.getInimigosPresentes();
        if (inimigos != null && !inimigos.isEmpty()) {
            System.out.println("⚔️ Combate iniciado na divisão: " + divisao.getNomeDivisao());
            while (!inimigos.isEmpty()) {
                try {
                    Inimigo inimigo = inimigos.removeFirst();
                    toCruz.sofrerDano(5); // Simular dano
                    inimigosDerrotados.addToRear(inimigo);
                    System.out.println("💀 Inimigo derrotado: " + inimigo.getNome());
                } catch (EmptyCollectionException e) {
                    System.err.println("Erro ao processar inimigo: " + e.getMessage());
                    break;
                }
            }
        }
    
        // Verifica e processa itens
        ArrayUnorderedList<Item> itens = divisao.getItensPresentes();
        if (itens != null && !itens.isEmpty()) {
            System.out.println("🎒 Itens encontrados na divisão: " + divisao.getNomeDivisao());
            while (!itens.isEmpty()) {
                try {
                    Item item = itens.removeFirst();
                    toCruz.adicionarAoInventario(item);
                    itensColetados.addToRear(item);
                    System.out.println("✅ Item coletado: " + item.getTipo());
                } catch (EmptyCollectionException e) {
                    System.err.println("Erro ao coletar item: " + e.getMessage());
                    break;
                }
            }
        }
    }
    
    
    

    /**
     * Encontra o caminho para a divisão de saída mais próxima usando BFS.
     *
     * @return Lista de divisões representando o caminho mais curto para a saída.
     */
    private ArrayUnorderedList<Divisao> encontrarCaminhoParaSaidaMaisProxima() {
        ArrayUnorderedList<String> entradasSaidas = mapa.getEntradasSaidasNomes();
        if (entradasSaidas.isEmpty()) {
            System.err.println("Erro: Nenhuma divisão de saída definida no mapa.");
            return null;
        }

        LinkedQueue<Divisao> fila = new LinkedQueue<>();
        ArrayUnorderedList<Divisao> visitados = new ArrayUnorderedList<>();
        ArrayUnorderedList<Predecessor> predecessores = new ArrayUnorderedList<>();

        Divisao posicaoInicial = toCruz.getPosicaoAtual();
        if (posicaoInicial == null) {
            System.err.println("Erro: Posição inicial de Tó Cruz é nula.");
            return null;
        }

        fila.enqueue(posicaoInicial);
        visitados.addToRear(posicaoInicial);
        predecessores.addToRear(new Predecessor(posicaoInicial, null));

        while (!fila.isEmpty()) {
            Divisao atual = fila.dequeue();

            // Verifica se a divisão atual é uma das saídas
            for (int i = 0; i < entradasSaidas.size(); i++) {
                String nomeSaida = entradasSaidas.getElementAt(i);
                if (atual.getNomeDivisao().equalsIgnoreCase(nomeSaida)) {
                    System.out.println("Divisão de saída encontrada: " + atual.getNomeDivisao());
                    // Reconstruir o caminho até esta divisão
                    ArrayUnorderedList<Divisao> caminho = new ArrayUnorderedList<>();
                    Divisao passo = atual;
                    while (passo != null) {
                        caminho.addToRear(passo);
                        passo = getPredecessor(predecessores, passo.getNomeDivisao());
                    }
                    return inverterLista(caminho);
                }
            }

            ArrayUnorderedList<Divisao> conexoes = mapa.obterConexoes(atual);
            if (conexoes == null || conexoes.isEmpty()) {
                System.out.println("Aviso: Divisão " + atual.getNomeDivisao() + " não possui conexões.");
                continue;
            }

            for (int i = 0; i < conexoes.size(); i++) {
                Divisao vizinho = conexoes.getElementAt(i);
                if (vizinho == null) continue;

                if (!visitados.contains(vizinho) && mapa.podeMover(atual.getNomeDivisao(), vizinho.getNomeDivisao())) {
                    visitados.addToRear(vizinho);
                    fila.enqueue(vizinho);
                    predecessores.addToRear(new Predecessor(vizinho, atual));
                }
            }
        }

        System.err.println("Erro: Nenhuma divisão de saída acessível foi encontrada.");
        return null;
    }

    /**
     * Reconstrói o caminho percorrido a partir dos predecessores.
     *
     * @param predecessores Lista de predecessores para cada divisão.
     * @param objetivo      Divisão objetivo que foi encontrada.
     */
    /**private void reconstructPath(ArrayUnorderedList<Predecessor> predecessores, Divisao objetivo, ArrayUnorderedList<Divisao> caminho) {
        Divisao passo = objetivo;
        while (passo != null) {
            caminho.addToRear(passo);
            passo = getPredecessor(predecessores, passo.getNomeDivisao());
        }
    }*/

    /**
     * Recupera o predecessor de uma divisão a partir da lista de predecessores.
     *
     * @param predecessores Lista de predecessores.
     * @param nomeDivisao   Nome da divisão atual.
     * @return Divisão predecessora ou null se não encontrada.
     */
    private Divisao getPredecessor(ArrayUnorderedList<Predecessor> predecessores, String nomeDivisao) {
        for (int i = 0; i < predecessores.size(); i++) {
            Predecessor p = predecessores.getElementAt(i);
            if (p != null && p.getAtual().getNomeDivisao().equalsIgnoreCase(nomeDivisao.trim())) {
                return p.getPredecessor();
            }
        }
        return null;
    }

    /**
     * Inverte uma lista de divisões para obter a ordem correta do caminho.
     *
     * @param lista Lista original.
     * @return Lista invertida.
     */
    private ArrayUnorderedList<Divisao> inverterLista(ArrayUnorderedList<Divisao> lista) {
        LinkedStack<Divisao> pilha = new LinkedStack<>();
        ArrayUnorderedList<Divisao> invertida = new ArrayUnorderedList<>();
        for (int i = 0; i < lista.size(); i++) {
            Divisao divisao = lista.getElementAt(i);
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
     * Verifica e executa o trajeto de volta para a divisão de saída mais próxima.
          * @throws ElementNotFoundException 
          */
          private void verificarTrajetoDeVolta() throws ElementNotFoundException {
            ArrayUnorderedList<Divisao> caminhoDeVolta = encontrarCaminhoParaSaidaMaisProxima();
        
            if (caminhoDeVolta == null || caminhoDeVolta.isEmpty()) {
                System.err.println("Erro: Caminho de volta não encontrado.");
                return;
            }
        
            System.out.println("🎯 Divisão de saída encontrada: " + caminhoDeVolta.getElementAt(caminhoDeVolta.size() - 1).getNomeDivisao());
        
            for (int i = 0; i < caminhoDeVolta.size(); i++) {
                Divisao divisao = caminhoDeVolta.getElementAt(i);
                if (divisao == null) continue;
        
                // Combate com inimigos na divisão de volta
                if (divisao.getInimigosPresentes() != null && !divisao.getInimigosPresentes().isEmpty()) {
                    System.out.println("⚔️ Combate iniciado na divisão: " + divisao.getNomeDivisao());
                    combateService.resolverCombate(toCruz, divisao);
        
                    // Verificar se Tó Cruz foi derrotado
                    if (toCruz.getVida() <= 0) {
                        System.err.println("💀 Tó Cruz foi derrotado durante o retorno!");
                        mostrarMapaInterativo(toCruz, divisao, false);
                        return;
                    }
                }
        
                // Mover para a divisão
                toCruz.moverPara(divisao);
                caminhoPercorrido.addToRear(divisao);
                mostrarMapaInterativo(toCruz, divisao, true);
            }
        
            System.out.println("🏆 Missão concluída com sucesso! Tó Cruz retornou com o alvo.");
        }
        
    @Override
    public int getVidaRestante() {
        return toCruz.getVida();
    }

    @Override
    public String getStatus() {
        return toCruz.getVida() > 0 ? "SUCESSO" : "FALHA";
    }

    @Override
    public Divisao getDivisaoFinal() {
        return toCruz.getPosicaoAtual();
    }

    @Override
    public ArrayUnorderedList<Divisao> getCaminhoPercorrido() {
        return caminhoPercorrido;
    }

    public ArrayUnorderedList<Inimigo> getInimigosDerrotados() {
        return inimigosDerrotados;
    }

    public ArrayUnorderedList<Item> getItensColetados() {
        return itensColetados;
    }

    public ArrayUnorderedList<String> getCaminhoPercorridoNomes() {
        ArrayUnorderedList<String> nomes = new ArrayUnorderedList<>();
        for (int i = 0; i < caminhoPercorrido.size(); i++) {
            Divisao divisao = caminhoPercorrido.getElementAt(i);
            if (divisao != null) {
                nomes.addToRear(divisao.getNomeDivisao());
            }
        }
        return nomes;
    }

    /**
     * Calcula o melhor caminho entre duas divisões usando BFS.
     *
     * @param origem  Divisão de origem.
     * @param destino Divisão de destino.
     * @return Lista de divisões representando o caminho mais curto.
          * @throws ElementNotFoundException 
          */
         public ArrayUnorderedList<Divisao> calcularMelhorCaminho(Divisao origem, Divisao destino) throws ElementNotFoundException {
        if (origem == null || destino == null) {
            System.err.println("Erro: Origem ou destino inválidos.");
            return new ArrayUnorderedList<>();
        }

        LinkedQueue<Divisao> fila = new LinkedQueue<>();
        ArrayUnorderedList<Divisao> visitados = new ArrayUnorderedList<>();
        ArrayUnorderedList<Predecessor> predecessores = new ArrayUnorderedList<>();
        ArrayUnorderedList<Divisao> caminho = new ArrayUnorderedList<>();

        fila.enqueue(origem);
        visitados.addToRear(origem);
        predecessores.addToRear(new Predecessor(origem, null));

        while (!fila.isEmpty()) {
            Divisao atual = fila.dequeue();
            if (atual.equals(destino)) {
                reconstruirCaminho(predecessores, destino);
                break;
            }

            ArrayUnorderedList<Divisao> conexoes = mapa.obterConexoes(atual);
            if (conexoes == null || conexoes.isEmpty()) {
                continue;
            }

            for (int i = 0; i < conexoes.size(); i++) {
                Divisao vizinho = conexoes.getElementAt(i);
                if (vizinho == null) continue;

                if (!visitados.contains(vizinho) && mapa.podeMover(atual.getNomeDivisao(), vizinho.getNomeDivisao())) {
                    visitados.addToRear(vizinho);
                    fila.enqueue(vizinho);
                    predecessores.addToRear(new Predecessor(vizinho, atual));
                }
            }
        }

        return inverterLista(caminho);
    }

    public void mostrarMapaInterativo(ToCruz toCruz, Divisao divisaoAtual, boolean sucesso) {
        System.out.println("===== MAPA ATUAL =====");
        ArrayUnorderedList<Divisao> divisoes = mapa.getDivisoes();
    
        for (int i = 0; i < divisoes.size(); i++) {
            Divisao divisao = divisoes.getElementAt(i);
            if (divisao == null) continue;
    
            // Mostrar o ícone de Tó Cruz na posição atual
            if (divisao.equals(divisaoAtual)) {
                String icone = sucesso ? "🤠" : "💀";
                System.out.print(icone + " " + divisao.getNomeDivisao() + " <- Tó Cruz está aqui");
            } else {
                System.out.print("   " + divisao.getNomeDivisao());
            }
            System.out.println();
        }
        System.out.println("=======================");
    }
    
    
    
    
    
}
