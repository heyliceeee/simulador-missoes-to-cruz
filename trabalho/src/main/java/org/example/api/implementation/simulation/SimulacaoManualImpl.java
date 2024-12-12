package org.example.api.implementation.simulation;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.Alvo;
import org.example.api.implementation.interfaces.Divisao;
import org.example.api.implementation.interfaces.Inimigo;
import org.example.api.implementation.interfaces.Item;
import org.example.api.implementation.interfaces.Mapa;
import org.example.api.implementation.interfaces.SimulacaoManual;
import org.example.api.implementation.services.CombateServiceImpl;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.implementation.ArrayUnorderedList;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Gerencia a simulação manual, onde o jogador toma as decisões.
 */
public class SimulacaoManualImpl implements SimulacaoManual {

    private final Mapa mapa;
    private final ToCruz toCruz;
    private final Scanner scanner;
    private final CombateServiceImpl combateService;
    private final ArrayUnorderedList<Item> itensColetados;
    private final ArrayUnorderedList<Inimigo> inimigosDerrotados;
    private final ArrayUnorderedList<Divisao> caminhoPercorrido;

    public SimulacaoManualImpl(Mapa mapa, ToCruz toCruz) {
        if (mapa == null || toCruz == null) {
            throw new IllegalArgumentException("Mapa e Tó Cruz não podem ser nulos.");
        }
        this.mapa = mapa;
        this.toCruz = toCruz;
        this.scanner = new Scanner(System.in);
        this.combateService = new CombateServiceImpl();
        this.itensColetados = new ArrayUnorderedList<>();
        this.inimigosDerrotados = new ArrayUnorderedList<>();
        this.caminhoPercorrido = new ArrayUnorderedList<>();
    }

    @Override
    public void executar(Divisao divisaoObjetivo) throws ElementNotFoundException {
        System.out.println("Início da simulação manual!");
    
        // Obter entradas/saídas disponíveis
        ArrayUnorderedList<String> entradasSaidas = mapa.getEntradasSaidasNomes();
        if (entradasSaidas.isEmpty()) {
            System.err.println("Erro: Não há divisões marcadas como entrada/saída no mapa.");
            return;
        }
    
        // Pedir ao jogador para escolher uma entrada
        Divisao posicaoInicial = null;
        while (posicaoInicial == null) {
            System.out.println("Escolha uma das entradas disponíveis:");
            for (int i = 0; i < entradasSaidas.size(); i++) {
                System.out.println("- " + entradasSaidas.getElementAt(i));
            }
            System.out.print("Digite o nome da entrada: ");
            String escolha = scanner.nextLine().trim();
    
            try {
                posicaoInicial = mapa.getDivisaoPorNome(escolha);
                if (!entradasSaidas.contains(escolha)) {
                    System.out.println("Escolha inválida. Tente novamente.");
                    posicaoInicial = null;
                }
            } catch (RuntimeException e) {
                System.out.println("Divisão não encontrada. Tente novamente.");
            }
        }
    
        toCruz.moverPara(posicaoInicial);
        caminhoPercorrido.addToRear(posicaoInicial);
    
        // Verificar se o objetivo foi definido
        if (divisaoObjetivo == null) {
            System.err.println("Erro: Divisão objetivo inválida.");
            return;
        }
    
        // Iniciar loop principal da simulação
        while (toCruz.getVida() > 0) {
            mostrarEstado();
            mostrarConexoesAdjacentes(toCruz.getPosicaoAtual());
    
            String comando = obterComando();
    
            if (comando.isEmpty()) {
                System.out.println("Nenhum comando fornecido. Tente novamente.");
                continue;
            }
    
            switch (comando.toLowerCase()) {
                case "mover" -> mover();
                case "usar" -> toCruz.usarKitDeVida();
                case "atacar" -> atacar();
                case "sair" -> {
                    System.out.println("Simulação terminada.");
                    return;
                }
                default -> {
                    System.out.println("Comando inválido. Tente novamente.");
                    continue; // Reinicia o loop ao detectar comando inválido
                }
            }
    
            // Mover inimigos após a ação do jogador
            try {
                mapa.moverInimigos(toCruz, combateService);
            } catch (ElementNotFoundException e) {
                System.err.println("Erro ao mover inimigos: " + e.getMessage());
            }
    
            // Mostrar o melhor caminho para o alvo
            ArrayUnorderedList<Divisao> caminhoParaAlvo = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(), divisaoObjetivo);
            System.out.print("Melhor caminho para o alvo: ");
            mostrarCaminho(caminhoParaAlvo);
    
            // Mostrar o melhor caminho para o kit de recuperação mais próximo
            ArrayUnorderedList<Item> kitsDisponiveis = mapa.getItensPorTipo("kit de vida");
            if (!kitsDisponiveis.isEmpty()) {
                Item kitMaisProximo = encontrarKitMaisProximo(kitsDisponiveis);
                ArrayUnorderedList<Divisao> caminhoParaKit = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(), kitMaisProximo.getDivisao());
                System.out.print("Melhor caminho para o kit de recuperação: ");
                mostrarCaminho(caminhoParaKit);
            } else {
                System.out.println("Nenhum kit de recuperação disponível no mapa.");
            }
    
            interagirComAlvo(toCruz.getPosicaoAtual());
    
            if (toCruz.isAlvoConcluido()) {
                System.out.println("Missão concluída com sucesso! Tó Cruz capturou o alvo.");
                return;
            }
        }
    
        System.out.println("Tó Cruz foi derrotado! Simulação encerrada.");
    }
    



    private void mostrarConexoesAdjacentes(Divisao divisaoAtual) throws ElementNotFoundException {
        ArrayUnorderedList<Divisao> conexoes = mapa.obterConexoes(divisaoAtual);
        System.out.println("\n--- Divisões Adjacentes ---");
        for (int i = 0; i < conexoes.size(); i++) {
            Divisao conexao = conexoes.getElementAt(i);
            if (conexao != null) {
                int numInimigos = conexao.getInimigosPresentes().size();
                int numItens = conexao.getItensPresentes().size();
                System.out.println(conexao.getNomeDivisao() + " -> Inimigos: " + numInimigos + ", Itens: " + numItens);
            }
        }
        System.out.println("---------------------------");
    }

    private void mostrarCaminho(ArrayUnorderedList<Divisao> caminho) {
        if (caminho == null || caminho.isEmpty()) {
            System.out.println("Nenhum caminho disponível.");
            return;
        }
    
        ArrayUnorderedList<Divisao> caminhoFiltrado = new ArrayUnorderedList<>();
        for (int i = 0; i < caminho.size(); i++) {
            Divisao divisao = caminho.getElementAt(i);
            if (!mapa.getEntradasSaidasNomes().contains(divisao.getNomeDivisao())) {
                caminhoFiltrado.addToRear(divisao);
            }
        }
    
        if (caminhoFiltrado.isEmpty()) {
            System.out.println("Nenhum caminho disponível.");
            return;
        }
    
        for (int i = 0; i < caminhoFiltrado.size(); i++) {
            System.out.print(caminhoFiltrado.getElementAt(i).getNomeDivisao());
            if (i < caminhoFiltrado.size() - 1){
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    

    private Item encontrarKitMaisProximo(ArrayUnorderedList<Item> kits) throws ElementNotFoundException {
        // Assume que o mapa pode retornar uma lista de itens e as divisões onde estão localizados
        Item kitMaisProximo = null;
        int menorDistancia = Integer.MAX_VALUE;
    
        for (int i = 0; i < kits.size(); i++) {
            Item kit = kits.getElementAt(i);
            int distancia = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(), kit.getDivisao()).size();
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                kitMaisProximo = kit;
            }
        }
        return kitMaisProximo;
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

    public ArrayUnorderedList<Item> getItensColetados() {
        return itensColetados;
    }

    public ArrayUnorderedList<Inimigo> getInimigosDerrotados() {
        return inimigosDerrotados;
    }

    public ArrayUnorderedList<Divisao> getCaminhoPercorrido() {
        return caminhoPercorrido;
    }

    private void mostrarEstado() {
        System.out.println("\n--- Estado Atual ---");
        Divisao posicao = toCruz.getPosicaoAtual();
        String nomeDivisao = (posicao != null) ? posicao.getNomeDivisao() : "Nenhuma";
        System.out.println("Posição: " + nomeDivisao);
        System.out.println("Vida: " + toCruz.getVida());
        System.out.println("Inventário: " + toCruz.getInventario().size() + " itens");
        System.out.println("---------------------");
    }

    private String obterComando() {
        try {
            System.out.println("Comandos disponíveis: mover, usar, atacar, sair");
            System.out.print("Digite seu comando: ");
            return scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.err.println("Erro ao receber comando: " + e.getMessage());
            return "";
        }
    }

    private void mover() throws ElementNotFoundException {
        System.out.print("Digite o nome da divisão para onde deseja mover: ");
        String novaDivisao = scanner.nextLine().trim();
    
        if (novaDivisao.isEmpty()) {
            System.out.println("Divisão inválida.");
            return;
        }
    
        if (mapa.podeMover(toCruz.getPosicaoAtual().getNomeDivisao(), novaDivisao)) {
            Divisao proximaDivisao = mapa.getDivisaoPorNome(novaDivisao);
            if (proximaDivisao != null) {
                toCruz.moverPara(proximaDivisao);
                caminhoPercorrido.addToRear(proximaDivisao);
                verificarItens(proximaDivisao);
                combater(proximaDivisao);
            } else {
                System.err.println("Erro: Divisão '" + novaDivisao + "' não encontrada.");
            }
        } else {
            System.out.println("Movimento inválido! Divisões não conectadas.");
        }
    }
    

    private void atacar() throws ElementNotFoundException {
        Divisao divisaoAtual = toCruz.getPosicaoAtual();
        if (divisaoAtual == null) {
            System.err.println("Erro: Divisão atual é nula.");
            return;
        }
        combater(divisaoAtual);
    }

    private void combater(Divisao divisao) throws ElementNotFoundException {
        combateService.resolverCombate(toCruz, divisao);
        ArrayUnorderedList<Inimigo> inimigos = divisao.getInimigosPresentes();
        while (inimigos != null && !inimigos.isEmpty()) {
            try {
                Inimigo inimigo = inimigos.removeFirst();
                inimigosDerrotados.addToRear(inimigo);
                System.out.println("Tó Cruz derrotou: " + inimigo.getNome());
            } catch (EmptyCollectionException e) {
                System.err.println("Erro ao remover inimigo: " + e.getMessage());
                break;
            }
        }
    }

    private void verificarItens(Divisao divisao) throws ElementNotFoundException {
        ArrayUnorderedList<Item> itens = divisao.getItensPresentes();
        if (itens != null && !itens.isEmpty()) {
            System.out.println("Itens encontrados na divisão:");
            for (int i = 0; i < itens.size(); i++) {
                Item item = itens.getElementAt(i);
                if (item != null) {
                    System.out.println("- " + item.getTipo());
                }
            }

            System.out.print("Deseja pegar todos os itens? (sim/não): ");
            String resposta = scanner.nextLine().trim();
            if ("sim".equalsIgnoreCase(resposta)) {
                while (!itens.isEmpty()) {
                    try {
                        Item item = itens.removeFirst();
                        itensColetados.addToRear(item);
                        toCruz.adicionarAoInventario(item);
                        System.out.println("Tó Cruz coletou: " + item.getTipo());
                    } catch (EmptyCollectionException e) {
                        System.err.println("Erro ao coletar item: " + e.getMessage());
                        break;
                    }
                }
            }
        }
    }

    private void interagirComAlvo(Divisao divisao) {
        Alvo alvo = mapa.getAlvo();
        if (alvo != null && divisao.equals(alvo.getDivisao())) {
            if (divisao.getInimigosPresentes() != null && !divisao.getInimigosPresentes().isEmpty()) {
                System.out.println("O alvo está nesta sala, mas há inimigos! Elimine-os primeiro.");
            } else {
                System.out.println("O alvo foi resgatado com sucesso!");
                mapa.removerAlvo();
                toCruz.setAlvoConcluido(true);
            }
        } else if (mapa.getEntradasSaidasNomes().contains(divisao.getNomeDivisao()) && toCruz.isAlvoConcluido()) {
            System.out.println("🏆 Missão concluída com sucesso! Tó Cruz saiu do edifício com o alvo.");
            System.exit(0);
        } else if (mapa.getEntradasSaidasNomes().contains(divisao.getNomeDivisao()) && !toCruz.isAlvoConcluido()) {
            System.out.println("❌ Missão falhou! Tó Cruz saiu do edifício sem capturar o alvo.");
            System.exit(0);
        }
    }
    

    @Override
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

}
