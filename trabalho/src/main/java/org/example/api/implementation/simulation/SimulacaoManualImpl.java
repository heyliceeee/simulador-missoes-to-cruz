package org.example.api.implementation.simulation;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.IAlvo;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IInimigo;
import org.example.api.implementation.interfaces.IItem;
import org.example.api.implementation.interfaces.IMapa;
import org.example.api.implementation.interfaces.ISimulacaoManual;
import org.example.api.implementation.models.ToCruz;
import org.example.api.implementation.services.CombateServiceImpl;
import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.implementation.ArrayUnorderedList;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A classe SimulacaoManualImpl gerencia a simulação manual de navegação de To Cruz por um mapa.
 * Nesta simulação, o jogador toma as decisões de movimento, uso de itens, e ataques a inimigos.
 * O objetivo é alcançar uma divisão-alvo, resgatar o alvo e sair do edifício.
 * 
 * Funcionalidades principais:
 * - Permitir ao jogador escolher a entrada inicial no mapa.
 * - Fornecer comandos para mover-se entre divisões, usar itens de cura, atacar inimigos ou sair da simulação.
 * - Mostrar o estado atual de To Cruz (vida, inventário, posição).
 * - Exibir divisões adjacentes, itens encontrados e caminhos até o objetivo ou kits de vida.
 * - Interagir com o alvo, concluindo a missão se alcançado e retornado a uma saída.
 * 
 * Comandos Suportados:
 * - "mover": Desloca To Cruz para uma divisão adjacente.
 * - "usar": Utiliza um kit de vida do inventário (se disponível).
 * - "atacar": Ataca inimigos na divisão atual.
 * - "sair": Encerra a simulação.
 */
public class SimulacaoManualImpl implements ISimulacaoManual {

    /**
     * Mapa (edifício) no qual To Cruz se movimentará.
     */
    private final IMapa mapa;

    /**
     * Personagem principal controlado pelo jogador.
     */
    private final ToCruz toCruz;

    /**
     * Scanner para leitura de comandos do jogador via console.
     */
    private final Scanner scanner;

    /**
     * Serviço de combate para resolução de lutas entre To Cruz e inimigos.
     */
    private final CombateServiceImpl combateService;

    /**
     * Lista de itens coletados durante a simulação.
     */
    private final ArrayUnorderedList<IItem> itensColetados;

    /**
     * Lista de inimigos derrotados durante a simulação.
     */
    private final ArrayUnorderedList<IInimigo> inimigosDerrotados;

    /**
     * Lista de divisões percorridas durante a simulação.
     */
    private final ArrayUnorderedList<IDivisao> caminhoPercorrido;

    /**
     * Construtor da Simulacao Manual.
     *
     * @param mapa   O mapa do edifício.
     * @param toCruz O agente controlado pelo jogador.
     * @throws IllegalArgumentException se o mapa ou toCruz forem nulos.
     */
    public SimulacaoManualImpl(IMapa mapa, ToCruz toCruz) {
        if (mapa == null || toCruz == null) {
            throw new IllegalArgumentException("Mapa e To Cruz nao podem ser nulos.");
        }
        this.mapa = mapa;
        this.toCruz = toCruz;
        this.scanner = new Scanner(System.in);
        this.combateService = new CombateServiceImpl();
        this.itensColetados = new ArrayUnorderedList<>();
        this.inimigosDerrotados = new ArrayUnorderedList<>();
        this.caminhoPercorrido = new ArrayUnorderedList<>();
    }

    /**
     * Executa o loop principal da simulação manual, permitindo que o jogador 
     * controle os movimentos de To Cruz, ataque inimigos e colete itens.
     * 
     * O objetivo é alcançar a divisão onde o alvo se encontra, resgatá-lo 
     * e então sair do edifício.
     * 
     * @param divisaoObjetivo A divisão onde o objetivo (alvo) se encontra.
     * @throws ElementNotFoundException se ocorrer erro ao acessar divisões ou itens.
     */
    @Override
    public void executar(IDivisao divisaoObjetivo) throws ElementNotFoundException {
        System.out.println("Inicio da simulacao manual!");

        ArrayUnorderedList<String> entradasSaidas = mapa.getEntradasSaidasNomes();
        if (entradasSaidas.isEmpty()) {
            System.err.println("Erro: Nao ha divisoes marcadas como entrada/saida no mapa.");
            return;
        }

        // Escolha da entrada inicial pelo jogador
        IDivisao posicaoInicial = null;
        while (posicaoInicial == null) {
            System.out.println("Escolha uma das entradas disponiveis:");
            for (int i = 0; i < entradasSaidas.size(); i++) {
                System.out.println("- " + entradasSaidas.getElementAt(i));
            }
            System.out.print("Digite o nome da entrada: ");
            String escolha = scanner.nextLine().trim();

            try {
                posicaoInicial = mapa.getDivisaoPorNome(escolha);
                if (!entradasSaidas.contains(escolha)) {
                    System.out.println("Escolha invalida. Tente novamente.");
                    posicaoInicial = null;
                }
            } catch (RuntimeException e) {
                System.out.println("Divisao nao encontrada. Tente novamente.");
            }
        }

        toCruz.moverPara(posicaoInicial);
        caminhoPercorrido.addToRear(posicaoInicial);

        if (divisaoObjetivo == null) {
            System.err.println("Erro: Divisao objetivo invalida.");
            return;
        }

        // Loop principal de interação jogador x simulação
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
                    System.out.println("Simulacao terminada.");
                    return;
                }
                default -> {
                    System.out.println("Comando invalido. Tente novamente.");
                    continue;
                }
            }

            // Mover inimigos apos a ação do jogador
            try {
                mapa.moverInimigos(toCruz, combateService);
            } catch (ElementNotFoundException e) {
                System.err.println("Erro ao mover inimigos: " + e.getMessage());
            }

            // Mostrar o melhor caminho para o alvo
            ArrayUnorderedList<IDivisao> caminhoParaAlvo = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(),
                    divisaoObjetivo);
            System.out.print("Melhor caminho para o alvo: ");
            mostrarCaminho(caminhoParaAlvo);

            // Mostrar o melhor caminho para o kit de vida mais próximo
            ArrayUnorderedList<IItem> kitsDisponiveis = mapa.getItensPorTipo("kit de vida");
            if (!kitsDisponiveis.isEmpty()) {
                IItem kitMaisProximo = encontrarKitMaisProximo(kitsDisponiveis);
                ArrayUnorderedList<IDivisao> caminhoParaKit = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(),
                        kitMaisProximo.getDivisao());
                System.out.print("Melhor caminho para o kit de recuperacao: ");
                mostrarCaminho(caminhoParaKit);
            } else {
                System.out.println("Nenhum kit de recuperacao disponivel no mapa.");
            }

            // Interagir com alvo se presente
            interagirComAlvo(toCruz.getPosicaoAtual());

            if (toCruz.isAlvoConcluido()) {
                System.out.println("Missao concluida com sucesso! To Cruz capturou o alvo.");
                return;
            }
        }

        System.out.println("To Cruz foi derrotado! Simulacao encerrada.");
    }

    /**
     * Mostra as conexões (divisões adjacentes) à divisão atual, incluindo 
     * o número de inimigos e itens em cada uma.
     * 
     * @param divisaoAtual A divisão onde To Cruz se encontra atualmente.
     * @throws ElementNotFoundException se não for possível obter conexões.
     */
    private void mostrarConexoesAdjacentes(IDivisao divisaoAtual) throws ElementNotFoundException {
        ArrayUnorderedList<IDivisao> conexoes = mapa.obterConexoes(divisaoAtual);
        System.out.println("\n--- Divisoes Adjacentes ---");
        for (int i = 0; i < conexoes.size(); i++) {
            IDivisao conexao = conexoes.getElementAt(i);
            if (conexao != null) {
                int numInimigos = conexao.getInimigosPresentes().size();
                int numItens = conexao.getItensPresentes().size();
                System.out.println(conexao.getNomeDivisao() + " -> Inimigos: " + numInimigos + ", Itens: " + numItens);
            }
        }
        System.out.println("---------------------------");
    }

    /**
     * Mostra o caminho fornecido, ignorando entradas/saídas, para apresentar
     * apenas as divisões intermediárias.
     * 
     * @param caminho Lista de divisões representando um caminho.
     */
    private void mostrarCaminho(ArrayUnorderedList<IDivisao> caminho) {
        if (caminho == null || caminho.isEmpty()) {
            System.out.println("Nenhum caminho disponivel.");
            return;
        }

        ArrayUnorderedList<IDivisao> caminhoFiltrado = new ArrayUnorderedList<>();
        for (int i = 0; i < caminho.size(); i++) {
            IDivisao divisao = caminho.getElementAt(i);
            if (!mapa.getEntradasSaidasNomes().contains(divisao.getNomeDivisao())) {
                caminhoFiltrado.addToRear(divisao);
            }
        }

        if (caminhoFiltrado.isEmpty()) {
            System.out.println("Nenhum caminho disponivel.");
            return;
        }

        for (int i = 0; i < caminhoFiltrado.size(); i++) {
            System.out.print(caminhoFiltrado.getElementAt(i).getNomeDivisao());
            if (i < caminhoFiltrado.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    /**
     * Encontra o kit de vida mais próximo com base no caminho mais curto até o mesmo.
     * 
     * @param kits Lista de kits de vida disponíveis no mapa.
     * @return O kit de vida mais próximo da posição atual de To Cruz.
     * @throws ElementNotFoundException se não for possível acessar as divisões envolvidas.
     */
    private IItem encontrarKitMaisProximo(ArrayUnorderedList<IItem> kits) throws ElementNotFoundException {
        IItem kitMaisProximo = null;
        int menorDistancia = Integer.MAX_VALUE;

        for (int i = 0; i < kits.size(); i++) {
            IItem kit = kits.getElementAt(i);
            int distancia = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(), kit.getDivisao()).size();
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                kitMaisProximo = kit;
            }
        }
        return kitMaisProximo;
    }

    /**
     * Obtém a vida restante de To Cruz.
     * 
     * @return Vida restante do personagem.
     */
    @Override
    public int getVidaRestante() {
        return toCruz.getVida();
    }

    /**
     * Retorna o status da missão.
     * 
     * @return "SUCESSO" se To Cruz estiver vivo, tiver concluído o objetivo (resgatado o alvo)
     *         e estiver em uma divisão de saída. Caso contrário, "FALHA".
     */
    @Override
    public String getStatus() {
        if (toCruz.getVida() > 0 && toCruz.isAlvoConcluido()) {
            return "SUCESSO";
        }
        return "FALHA";
    }

    /**
     * Obtém a divisão atual onde To Cruz se encontra.
     * 
     * @return Divisão atual de To Cruz.
     */
    @Override
    public IDivisao getDivisaoFinal() {
        return toCruz.getPosicaoAtual();
    }

    /**
     * @return Lista de itens coletados durante a simulação.
     */
    public ArrayUnorderedList<IItem> getItensColetados() {
        return itensColetados;
    }

    /**
     * @return Lista de inimigos derrotados durante a simulação.
     */
    public ArrayUnorderedList<IInimigo> getInimigosDerrotados() {
        return inimigosDerrotados;
    }

    /**
     * @return Lista de divisões percorridas durante a simulação.
     */
    public ArrayUnorderedList<IDivisao> getCaminhoPercorrido() {
        return caminhoPercorrido;
    }

    /**
     * Mostra o estado atual do jogo, incluindo posição de To Cruz, vida e inventário.
     */
    private void mostrarEstado() {
        System.out.println("\n--- Estado Atual ---");
        IDivisao posicao = toCruz.getPosicaoAtual();
        String nomeDivisao = (posicao != null) ? posicao.getNomeDivisao() : "Nenhuma";
        System.out.println("Posicao: " + nomeDivisao);
        System.out.println("Vida: " + toCruz.getVida());
        System.out.println("Inventario: " + toCruz.getInventario().size() + " itens");
        System.out.println("---------------------");
    }

    /**
     * Obtém um comando do jogador a partir da entrada padrão.
     * 
     * @return O comando digitado pelo jogador, ou string vazia em caso de erro.
     */
    private String obterComando() {
        try {
            System.out.println("Comandos disponiveis: mover, usar, atacar, sair");
            System.out.print("Digite seu comando: ");
            return scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.err.println("Erro ao receber comando: " + e.getMessage());
            return "";
        }
    }

    /**
     * Move To Cruz para uma divisão adjacente, conforme escolha do jogador.
     * 
     * @throws ElementNotFoundException se a divisão especificada não for encontrada.
     */
    private void mover() throws ElementNotFoundException {
        System.out.print("Digite o nome da divisao para onde deseja mover: ");
        String novaDivisao = scanner.nextLine().trim();

        if (novaDivisao.isEmpty()) {
            System.out.println("Divisao invalida.");
            return;
        }

        if (mapa.podeMover(toCruz.getPosicaoAtual().getNomeDivisao(), novaDivisao)) {
            IDivisao proximaDivisao = mapa.getDivisaoPorNome(novaDivisao);
            if (proximaDivisao != null) {
                toCruz.moverPara(proximaDivisao);
                caminhoPercorrido.addToRear(proximaDivisao);
                verificarItens(proximaDivisao);

                // Se houver inimigos, combate é resolvido imediatamente
                ArrayUnorderedList<IInimigo> inimigos = proximaDivisao.getInimigosPresentes();
                if (inimigos != null && !inimigos.isEmpty()) {
                    combateService.resolverCombate(toCruz, proximaDivisao, false);
                }

            } else {
                System.err.println("Erro: Divisao '" + novaDivisao + "' nao encontrada.");
            }
        } else {
            System.out.println("Movimento invalido! Divisoes nao conectadas.");
        }
    }

    /**
     * Realiza um ataque contra os inimigos na divisão atual.
     * 
     * @throws ElementNotFoundException se houver problemas para acessar a divisão ou inimigos.
     */
    private void atacar() throws ElementNotFoundException {
        IDivisao divisaoAtual = toCruz.getPosicaoAtual();
        if (divisaoAtual == null) {
            System.err.println("Erro: Divisao atual e nula.");
            return;
        }
        combater(divisaoAtual);
    }

    /**
     * Executa o combate na divisão indicada. Tó Cruz ataca primeiro.
     * 
     * @param divisao Divisão onde o combate ocorre.
     * @throws ElementNotFoundException se ocorrer erro ao acessar dados.
     */
    private void combater(IDivisao divisao) throws ElementNotFoundException {
        combateService.resolverCombate(toCruz, divisao, false);

        ArrayUnorderedList<IInimigo> inimigos = divisao.getInimigosPresentes();
        while (inimigos != null && !inimigos.isEmpty()) {
            try {
                IInimigo inimigo = inimigos.removeFirst();
                inimigosDerrotados.addToRear(inimigo);
                System.out.println("To Cruz derrotou: " + inimigo.getNome());
            } catch (EmptyCollectionException e) {
                System.err.println("Erro ao remover inimigo: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * Verifica se há itens na divisão atual e pergunta ao jogador se deseja coletá-los.
     * 
     * @param divisao Divisão onde To Cruz está.
     * @throws ElementNotFoundException se itens não puderem ser acessados.
     */
    private void verificarItens(IDivisao divisao) throws ElementNotFoundException {
        ArrayUnorderedList<IItem> itens = divisao.getItensPresentes();
        if (itens != null && !itens.isEmpty()) {
            System.out.println("Itens encontrados na divisao:");
            for (int i = 0; i < itens.size(); i++) {
                IItem item = itens.getElementAt(i);
                if (item != null) {
                    System.out.println("- " + item.getTipo());
                }
            }

            System.out.print("Deseja pegar todos os itens? (sim/nao): ");
            String resposta = scanner.nextLine().trim();
            if ("sim".equalsIgnoreCase(resposta)) {
                while (!itens.isEmpty()) {
                    try {
                        IItem item = itens.removeFirst();
                        itensColetados.addToRear(item);
                        toCruz.adicionarAoInventario(item);
                        System.out.println("To Cruz coletou: " + item.getTipo());
                    } catch (EmptyCollectionException e) {
                        System.err.println("Erro ao coletar item: " + e.getMessage());
                        break;
                    }
                }
            }
        }
    }

    /**
     * Interage com o alvo se estiver na mesma divisão de To Cruz.
     * Caso haja inimigos, informa que primeiro devem ser eliminados.
     * Caso o alvo seja resgatado e se chegue a uma saída, a missão é concluída.
     * Caso saia sem o alvo, a missão falha.
     * 
     * @param divisao Divisão atual de To Cruz.
     */
    private void interagirComAlvo(IDivisao divisao) {
        IAlvo alvo = mapa.getAlvo();
        if (alvo != null && divisao.equals(alvo.getDivisao())) {
            if (divisao.getInimigosPresentes() != null && !divisao.getInimigosPresentes().isEmpty()) {
                System.out.println("O alvo esta nesta sala, mas ha inimigos! Elimine-os primeiro.");
            } else {
                System.out.println("O alvo foi resgatado com sucesso!");
                mapa.removerAlvo();
                toCruz.setAlvoConcluido(true);
            }
        } else if (mapa.getEntradasSaidasNomes().contains(divisao.getNomeDivisao()) && toCruz.isAlvoConcluido()) {
            System.out.println("🏆 Missao concluida com sucesso! To Cruz saiu do edificio com o alvo.");
            System.exit(0);
        } else if (mapa.getEntradasSaidasNomes().contains(divisao.getNomeDivisao()) && !toCruz.isAlvoConcluido()) {
            System.out.println("❌ Missao falhou! To Cruz saiu do edificio sem capturar o alvo.");
            System.exit(0);
        }
    }

    /**
     * Retorna uma lista com os nomes das divisões percorridas.
     * 
     * @return Lista de nomes de divisões percorridas.
     */
    @Override
    public ArrayUnorderedList<String> getCaminhoPercorridoNomes() {
        ArrayUnorderedList<String> nomes = new ArrayUnorderedList<>();
        for (int i = 0; i < caminhoPercorrido.size(); i++) {
            IDivisao divisao = caminhoPercorrido.getElementAt(i);
            if (divisao != null) {
                nomes.addToRear(divisao.getNomeDivisao());
            }
        }
        return nomes;
    }
}
