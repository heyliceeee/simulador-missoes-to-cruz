package org.example;

import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;
import org.example.api.implementation.models.MapaImpl;
import org.example.api.implementation.models.ToCruz;
import org.example.api.implementation.services.CombateServiceImpl;
import org.example.api.implementation.simulation.SimulacaoAutomaticaImpl;
import org.example.api.implementation.simulation.SimulacaoManualImpl;
import org.example.api.implementation.utils.ExportarResultados;
import org.example.api.implementation.utils.ImportJsonImpl;
import org.example.api.implementation.interfaces.*;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.api.implementation.models.ResultadoSimulacaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ElementNotFoundException {
        logger.info("Iniciando o programa...");
        String caminhoJson = "mapa_v5.json";

        // Inicialização do mapa e carregamento da missão
        Mapa mapa = new MapaImpl();
        ImportJson importJson = new ImportJsonImpl(mapa);
        Missao missao;

        try {
            System.out.println("--------------------------------------------------------------------------------");
            missao = importJson.carregarMissao(caminhoJson);
            logger.info("Missão carregada: {} - Versão {}", missao.getCodMissao(), missao.getVersao());
        } catch (InvalidJsonStructureException | InvalidFieldException | DivisionNotFoundException e) {
            logger.error("Erro ao carregar a missão: {}", e.getMessage());
            return;
        }

        if (mapa.getDivisoes().isEmpty()) {
            logger.error("Erro: Nenhuma divisão carregada no mapa. Encerrando o programa.");
            return;
        }

        System.out.println("--------------------------------------------------------------------------------");
        mapa.mostrarMapa();

        // Inicialização do agente Tó Cruz
        logger.info("Inicializando o agente Tó Cruz...");
        ToCruz toCruzOriginal = new ToCruz("Tó Cruz", 100);

        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("\nEscolha o tipo de simulação:");
            System.out.println("1. Simulação Automática");
            System.out.println("2. Simulação Manual");
            System.out.println("3. Sair");
            System.out.print("Digite sua escolha: ");
            String escolha = scanner.nextLine().trim();

            switch (escolha) {
                case "1" -> {
                    logger.info("Iniciando a simulação automática...");
                    ToCruz toCruz = clonarToCruz(toCruzOriginal);

                    Divisao divisaoInicial = mapa.getDivisoes().getElementAt(0);
                    if (divisaoInicial == null) {
                        System.out.println("Erro: Nenhuma divisão inicial encontrada.");
                        break;
                    }

                    toCruz.moverPara(divisaoInicial);
                    logger.info("Tó Cruz posicionado na divisão inicial: {}", divisaoInicial.getNomeDivisao());

                    SimulacaoAutomatica simulacaoAuto = new SimulacaoAutomaticaImpl(mapa, toCruz,
                            new CombateServiceImpl());
                    try {
                        simulacaoAuto.executar(mapa.getAlvo().getDivisao());
                    } catch (Exception e) {
                        logger.error("Erro durante a simulação automática: {}", e.getMessage());
                        System.err.println("Erro na simulação automática. Verifique os logs para mais detalhes.");
                        break;
                    }

                    Divisao divisaoFinal = simulacaoAuto.getDivisaoFinal();
                    if (divisaoFinal == null) {
                        System.out.println("Erro: Simulação automática falhou.");
                    } else {
                        ResultadoSimulacao resultadoAuto = new ResultadoSimulacaoImpl(
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
                        logger.info("Simulação automática concluída.");
                    }
                }
                case "2" -> {
                    logger.info("Iniciando a simulação manual...");
                    ToCruz toCruz = clonarToCruz(toCruzOriginal);

                    SimulacaoManual simulacaoManual = new SimulacaoManualImpl(mapa, toCruz);
                    simulacaoManual.executar(mapa.getAlvo().getDivisao());

                    ResultadoSimulacao resultadoManual = new ResultadoSimulacaoImpl(
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
                    logger.info("Simulação manual concluída.");

                }

                case "3" -> {
                    continuar = false;
                    logger.info("Encerrando o programa...");
                }
                default -> System.out.println("Escolha inválida. Tente novamente.");
            }
        }

        scanner.close();
    }

    private static ToCruz clonarToCruz(ToCruz original) {
        return new ToCruz(original.getNome(), original.getVida());
    }

    private static ArrayUnorderedList<String> filtrarLista(ArrayUnorderedList<String> lista) {
        ArrayUnorderedList<String> filtrada = new ArrayUnorderedList<>();
        for (int i = 0; i < lista.size(); i++) {
            String elemento = lista.getElementAt(i);
            if (elemento != null) {
                filtrada.addToRear(elemento);
            }
        }
        return filtrada;
    }
}
