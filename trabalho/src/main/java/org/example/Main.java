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

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ElementNotFoundException {

        logger.info("Iniciando o programa...");
        String caminhoJson = "mapa_v6.json";

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
        ToCruz toCruz = new ToCruz("Tó Cruz", 100);
        Divisao divisaoInicial = mapa.getDivisoes().getElementAt(0);
        toCruz.moverPara(divisaoInicial);
        logger.info("Agente {} posicionado na divisão inicial: {}", toCruz.getNome(), divisaoInicial.getNomeDivisao());

        // Simulação Automática
        logger.info("Iniciando a simulação automática...");
        SimulacaoAutomatica simulacaoAuto = new SimulacaoAutomaticaImpl(mapa, toCruz, new CombateServiceImpl());
        simulacaoAuto.executar(mapa.getAlvo().getDivisao());

        ResultadoSimulacao resultadoAuto = new ResultadoSimulacaoImpl(
                "AUTO-001",
                divisaoInicial.getNomeDivisao(),
                simulacaoAuto.getDivisaoFinal().getNomeDivisao(),
                simulacaoAuto.getStatus(),
                simulacaoAuto.getVidaRestante(),
                filtrarLista(simulacaoAuto.getCaminhoPercorridoNomes()),
                filtrarLista(mapa.getEntradasSaidasNomes()),
                missao.getCodMissao(),
                missao.getVersao()
        );

        // Simulação Manual
        logger.info("Iniciando a simulação manual...");
        SimulacaoManual simulacaoManual = new SimulacaoManualImpl(mapa, toCruz);
        simulacaoManual.executar(mapa.getAlvo().getDivisao());

        ResultadoSimulacao resultadoManual = new ResultadoSimulacaoImpl(
                "MANUAL-001",
                divisaoInicial.getNomeDivisao(),
                simulacaoManual.getDivisaoFinal().getNomeDivisao(),
                simulacaoManual.getStatus(),
                simulacaoManual.getVidaRestante(),
                filtrarLista(simulacaoManual.getCaminhoPercorridoNomes()),
                filtrarLista(mapa.getEntradasSaidasNomes()),
                missao.getCodMissao(),
                missao.getVersao()
        );

        // Exportar o relatório combinado
        ExportarResultados exportador = new ExportarResultados();
        exportador.exportarRelatorioSimulacoes(resultadoAuto, resultadoManual, mapa, "relatorio_simulacoes.json");

        logger.info("Relatório de simulações exportado com sucesso.");

        logger.info("Programa finalizado com sucesso.");
    }

    /**
     * Filtra uma lista para remover valores nulos.
     *
     * @param lista Lista original a ser filtrada.
     * @return Nova lista sem valores nulos.
     */
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
