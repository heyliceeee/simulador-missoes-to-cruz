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

        // Inicializacao do mapa e carregamento da missao
        IMapa mapa = new MapaImpl();
        IImportJson importJson = new ImportJsonImpl(mapa);
        IMissao missao;

        try {
            System.out.println("--------------------------------------------------------------------------------");
            missao = importJson.carregarMissao(caminhoJson);
            logger.info("Missao carregada: {} - Versao {}", missao.getCodMissao(), missao.getVersao());
        } catch (InvalidJsonStructureException | InvalidFieldException | DivisionNotFoundException e) {
            logger.error("Erro ao carregar a missao: {}", e.getMessage());
            return;
        }

        if (mapa.getDivisoes().isEmpty()) {
            logger.error("Erro: Nenhuma divisao carregada no mapa. Encerrando o programa.");
            return;
        }

        System.out.println("--------------------------------------------------------------------------------");
        mapa.mostrarMapa();

        // Inicializacao do agente Tó Cruz
        logger.info("Inicializando o agente To Cruz...");
        ToCruz toCruz = new ToCruz("To Cruz", 100); // Nome e vida inicial
        IDivisao divisaoInicial = mapa.getDivisoes().getElementAt(0); // Primeira divisao
        toCruz.moverPara(divisaoInicial);
        logger.info("Agente {} posicionado na divisao inicial: {}", toCruz.getNome(), divisaoInicial.getNomeDivisao());

        // ============ SIMULAcaO AUTOMATICA ============
        logger.info("Iniciando a simulacao automatica...");
        ISimulacaoAutomatica simulacaoAuto = new SimulacaoAutomaticaImpl(mapa, toCruz);
        simulacaoAuto.executar(mapa.getAlvo().getDivisao());

        IResultadoSimulacao resultadoAuto = new ResultadoSimulacaoImpl(
                "AUTO-001",
                divisaoInicial.getNomeDivisao(),
                simulacaoAuto.getDivisaoFinal().getNomeDivisao(),
                simulacaoAuto.getStatus(),
                simulacaoAuto.getVidaRestante(),
                filtrarLista(simulacaoAuto.getCaminhoPercorridoNomes()),
                filtrarLista(mapa.getEntradasSaidasNomes()),
                missao.getCodMissao(),
                missao.getVersao());

        ArrayUnorderedList<IResultadoSimulacao> resultados = new ArrayUnorderedList<>();
        resultados.addToRear(resultadoAuto);

        ExportarResultados exportador = new ExportarResultados();
        exportador.exportarParaJson(resultados, "resultado_simulacao_automatica.json");
        logger.info("Resultado da Simulacao Automatica exportado com sucesso.");

        // Simulacao Manual
        logger.info("Iniciando a simulacao manual...");
        ISimulacaoManual simulacaoManual = new SimulacaoManualImpl(mapa, toCruz);
        simulacaoManual.executar(mapa.getAlvo().getDivisao());

        ArrayUnorderedList<String> trajetoManual = filtrarLista(simulacaoManual.getCaminhoPercorridoNomes());
        IResultadoSimulacao resultadoManual = new ResultadoSimulacaoImpl(
                "MANUAL-001",
                divisaoInicial.getNomeDivisao(),
                simulacaoManual.getDivisaoFinal().getNomeDivisao(),
                simulacaoManual.getStatus(),
                simulacaoManual.getVidaRestante(),
                filtrarLista(simulacaoManual.getCaminhoPercorridoNomes()),
                filtrarLista(mapa.getEntradasSaidasNomes()),
                missao.getCodMissao(),
                missao.getVersao());

        resultados.addToRear(resultadoManual);
        exportador.exportarParaJson(resultados, "resultado_simulacao_manual.json");
        logger.info("Resultado da Simulacao Manual exportado com sucesso.");

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
