package org.example;

import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;
import org.example.api.implementation.interfaces.*;
import org.example.api.implementation.models.MapaImpl;
import org.example.api.implementation.models.ResultadoSimulacaoImpl;
import org.example.api.implementation.models.ToCruz;
import org.example.api.implementation.simulation.SimulacaoAutomaticaImpl;
import org.example.api.implementation.simulation.SimulacaoManualImpl;
import org.example.api.implementation.utils.ExportarResultados;
import org.example.api.implementation.utils.ImportJsonImpl;
import org.example.collections.implementation.ArrayUnorderedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Classe principal que gerencia a execucao do programa.
 * 
 * O programa permite que o usuario escolha entre simular uma missao
 * automaticamente ou manualmente.
 * Tambem gerencia o carregamento do mapa, inicializacao do agente To Cruz e
 * exportacao dos resultados da simulacao.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Metodo principal para executar o programa.
     *
     * @param args Argumentos da linha de comando.
     * @throws ElementNotFoundException Excecao lancada se algum elemento nao for
     *                                  encontrado no mapa ou durante a simulacao.
     */
    public static void main(String[] args) throws ElementNotFoundException {
        logger.info("Iniciando o programa...");
        String caminhoJson = "map5_v.json";

        // Inicializacao do mapa e carregamento da missao
        IMapa mapa = new MapaImpl();
        IImportJson importJson = new ImportJsonImpl(mapa);
        IMissao missao;

        System.out.println("================================================================================");
        System.out.println("                   BEM-VINDO AO SIMULADOR DE MISSOES DE TÓ CRUZ                  ");
        System.out.println("================================================================================\n");

        try {
            System.out.println("Carregando missão a partir do arquivo: " + caminhoJson + " ...");
            missao = importJson.carregarMissao(caminhoJson);
            System.out.println("\nMissão carregada com sucesso!");
            System.out.println("Código da Missão: " + missao.getCodMissao());
            System.out.println("Versão da Missão: " + missao.getVersao());
            logger.info("Missao carregada: {} - Versao {}", missao.getCodMissao(), missao.getVersao());
        } catch (InvalidJsonStructureException | InvalidFieldException | DivisionNotFoundException e) {
            logger.error("Erro ao carregar a missao: {}", e.getMessage());
            System.err.println("Erro ao carregar a missão: " + e.getMessage());
            return;
        }

        // Verifica se o mapa contem divisoes validas.
        if (mapa.getDivisoes().isEmpty()) {
            logger.error("Erro: Nenhuma divisao carregada no mapa. Encerrando o programa.");
            System.err.println("Erro: Nenhuma divisão carregada no mapa. Encerrando o programa.");
            return;
        }

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("                 MAPA DO EDIFÍCIO (DIVISÕES E CONEXÕES) ");
        System.out.println("--------------------------------------------------------------------------------");
        mapa.mostrarMapa();

        // Inicializa o agente To Cruz com valores padrao.
        logger.info("Inicializando o agente Tó Cruz...");
        ToCruz toCruzOriginal = new ToCruz("To Cruz", 100);

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("Agente Tó Cruz inicializado com vida: " + toCruzOriginal.getVida());
        System.out.println("Inventário inicial vazio.");
        System.out.println("--------------------------------------------------------------------------------\n");

        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        // Menu principal para o usuario escolher o tipo de simulacao.
        while (continuar) {
            System.out.println("\n====================================");
            System.out.println("          MENU PRINCIPAL            ");
            System.out.println("====================================");
            System.out.println("1. Simulação Automática");
            System.out.println("2. Simulação Manual");
            System.out.println("3. Sair");
            System.out.print("\nDigite sua escolha: ");
            String escolha = scanner.nextLine().trim();

            switch (escolha) {
                case "1" -> {
                    logger.info("Iniciando a simulacao automatica...");
                    System.out.println("\n====================================");
                    System.out.println("      SIMULAÇÃO AUTOMÁTICA          ");
                    System.out.println("====================================");

                    ToCruz toCruz = clonarToCruz(toCruzOriginal);

                    // Define a divisao inicial como a primeira no mapa
                    IDivisao divisaoInicial = mapa.getDivisoes().isEmpty() ? null : mapa.getDivisoes().getElementAt(0);
                    if (divisaoInicial == null) {
                        System.err.println("Erro: Nenhuma divisão inicial encontrada.");
                        break;
                    }

                    toCruz.moverPara(divisaoInicial);
                    logger.info("To Cruz posicionado na divisao inicial: {}", divisaoInicial.getNomeDivisao());
                    System.out.println("Tó Cruz posicionado na divisão inicial: " + divisaoInicial.getNomeDivisao());

                    // Executa a simulacao automatica
                    ISimulacaoAutomatica simulacaoAuto = new SimulacaoAutomaticaImpl(mapa, toCruz);
                    try {
                        simulacaoAuto.executar(mapa.getAlvo().getDivisao());
                    } catch (Exception e) {
                        logger.error("Erro durante a simulacao automatica: {}", e.getMessage());
                        System.err.println("Erro na simulação automática: " + e.getMessage());
                        System.err.println("Verifique os logs para mais detalhes.");
                        break;
                    }

                    // Exporta os resultados da simulacao automatica.
                    IDivisao divisaoFinal = simulacaoAuto.getDivisaoFinal();
                    if (divisaoFinal == null) {
                        System.err.println("Erro: Simulação automática falhou.");
                    } else {
                        IResultadoSimulacao resultadoAuto = new ResultadoSimulacaoImpl(
                                "AUTO-001",
                                divisaoInicial.getNomeDivisao(),
                                divisaoFinal.getNomeDivisao(),
                                simulacaoAuto.getStatus(),
                                simulacaoAuto.getVidaRestante(),
                                filtrarLista(simulacaoAuto.getCaminhoPercorridoNomes()),
                                filtrarLista(mapa.getEntradasSaidasNomes()),
                                missao.getCodMissao(),
                                missao.getVersao());

                        ExportarResultados exportador = new ExportarResultados();
                        exportador.exportarParaJson(resultadoAuto, "relatorio_simulacao_automatica.json", mapa);
                        logger.info("Simulacao automatica concluida.");

                        System.out.println(
                                "\n--------------------------------------------------------------------------------");
                        System.out.println("Simulação automática concluída!");
                        System.out.println("Relatório exportado para relatorio_simulacao_automatica.json\n");
                    }
                }
                case "2" -> {
                    logger.info("Iniciando a simulacao manual...");
                    System.out.println("\n====================================");
                    System.out.println("       SIMULAÇÃO MANUAL             ");
                    System.out.println("====================================");

                    ToCruz toCruz = clonarToCruz(toCruzOriginal);

                    // Executa a simulação manual.
                    ISimulacaoManual simulacaoManual = new SimulacaoManualImpl(mapa, toCruz);
                    simulacaoManual.executar(mapa.getAlvo().getDivisao());

                    // Exporta os resultados da simulação manual.
                    IResultadoSimulacao resultadoManual = new ResultadoSimulacaoImpl(
                            "MANUAL-001",
                            mapa.getDivisoes().getElementAt(0).getNomeDivisao(),
                            simulacaoManual.getDivisaoFinal().getNomeDivisao(),
                            simulacaoManual.getStatus(),
                            simulacaoManual.getVidaRestante(),
                            filtrarLista(simulacaoManual.getCaminhoPercorridoNomes()),
                            filtrarLista(mapa.getEntradasSaidasNomes()),
                            missao.getCodMissao(),
                            missao.getVersao());

                    ExportarResultados exportador = new ExportarResultados();
                    exportador.exportarParaJson(resultadoManual, "relatorio_simulacao_manual.json", mapa);
                    logger.info("Simulacao manual concluida.");

                    System.out.println(
                            "\n--------------------------------------------------------------------------------");
                    System.out.println("Simulação manual concluída!");
                    System.out.println("Relatório exportado para relatorio_simulacao_manual.json\n");
                }

                case "3" -> {
                    continuar = false;
                    logger.info("Encerrando o programa...");
                    System.out.println("\nEncerrando o programa. Obrigado por utilizar o simulador!");
                }

                default -> {
                    System.out.println("Escolha inválida. Tente novamente.\n");
                }
            }
        }

        scanner.close();
    }

    /**
     * Cria uma copia do agente To Cruz com os mesmos atributos.
     *
     * @param original Objeto original de To Cruz.
     * @return Nova instancia de To Cruz.
     */
    private static ToCruz clonarToCruz(ToCruz original) {
        return new ToCruz(original.getNome(), original.getVida());
    }

    /**
     * Filtra uma lista de strings, removendo elementos nulos ou vazios.
     *
     * @param lista Lista de strings a ser filtrada.
     * @return Nova lista contendo apenas elementos validos.
     */
    public static ArrayUnorderedList<String> filtrarLista(ArrayUnorderedList<String> lista) {
        ArrayUnorderedList<String> filtrada = new ArrayUnorderedList<>();
        if (lista != null) {
            for (int i = 0; i < lista.size(); i++) {
                String elemento = lista.getElementAt(i);
                if (elemento != null && !elemento.isEmpty()) {
                    filtrada.addToRear(elemento);
                }
            }
        }
        return filtrada;
    }

    /**
     * Filtra uma lista de divisões para remover valores nulos e retornar apenas os
     * nomes.
     *
     * @param lista Lista original de divisoes.
     * @return Nova lista de strings com nomes das divisoes.
     */
    public static ArrayUnorderedList<String> filtrarListaDivisao(ArrayUnorderedList<IDivisao> lista) {
        ArrayUnorderedList<String> filtrada = new ArrayUnorderedList<>();
        for (int i = 0; i < lista.size(); i++) {
            IDivisao div = lista.getElementAt(i);
            if (div != null && div.getNomeDivisao() != null) {
                filtrada.addToRear(div.getNomeDivisao());
            }
        }
        return filtrada;
    }
}
