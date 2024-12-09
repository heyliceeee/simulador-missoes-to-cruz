package org.example;

import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;
import org.example.api.implementation.models.*;
import org.example.api.implementation.simulation.SimulacaoAutomatica;
import org.example.api.implementation.simulation.SimulacaoManual;
import org.example.api.implementation.services.CombateService;
import org.example.api.implementation.utils.ExportarResultados;
import org.example.api.implementation.utils.JsonUtils;
import org.example.collections.implementation.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // ============ INICIALIZAÇÃO DO MAPA ============
        logger.info("Iniciando o carregamento do mapa...");
        Mapa mapa = new Mapa();
        JsonUtils jsonUtils = new JsonUtils(mapa);
        String caminhoJson = "mapa.json";

        try {
            jsonUtils.carregarMapa(caminhoJson);
            logger.info("Mapa carregado com sucesso e pronto para uso!");

            // Exibir o mapa na consola
            System.out.println("Mapa do edifício:");
            mapa.mostrarMapa();

            // Verificar se o alvo foi carregado corretamente
            Alvo alvo = mapa.getAlvo();
            if (alvo != null) {
                logger.info("Alvo carregado do JSON: Divisão - {}, Tipo - {}", alvo.getDivisao().getNomeDivisao(), alvo.getTipo());
            } else {
                logger.error("Nenhum alvo definido no JSON ou erro ao carregar.");
                return;
            }

        } catch (InvalidJsonStructureException e) {
            logger.error("Erro na estrutura do JSON: {}", e.getMessage());
            return;
        } catch (InvalidFieldException e) {
            logger.error("Erro em um campo do JSON: {}", e.getMessage());
            return;
        } catch (DivisionNotFoundException e) {
            logger.error("Erro de referência de divisão: {}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Erro inesperado: {}", e.getMessage());
            e.printStackTrace();
            return;
        }

        if (mapa.getDivisoes().isEmpty()) {
            logger.error("Mapa não possui divisões carregadas. Encerrando o programa.");
            return;
        }

        // ============ INICIALIZAÇÃO DO AGENTE ============
        logger.info("Inicializando o agente Tó Cruz...");
        ToCruz toCruz = new ToCruz("Tó Cruz", 100); // Nome e vida inicial
        Divisao divisaoInicial = mapa.getDivisoes().getElementAt(0); // Primeira divisão
        toCruz.moverPara(divisaoInicial);
        logger.info("Agente {} posicionado na divisão inicial: {}", toCruz.getNome(), divisaoInicial.getNomeDivisao());

        // ============ SIMULAÇÃO AUTOMATICA ============
        logger.info("Iniciando a simulação automática...");
        SimulacaoAutomatica simulacaoAuto = new SimulacaoAutomatica(mapa, toCruz);
        simulacaoAuto.executar(mapa.getAlvo().getDivisao());

        ResultadoSimulacao resultadoAuto = new ResultadoSimulacao(
                "AUTO-001",
                divisaoInicial.getNomeDivisao(),
                simulacaoAuto.getDivisaoFinal().getNomeDivisao(),
                simulacaoAuto.getStatus(),
                simulacaoAuto.getVidaRestante(),
                coletarTrajeto(simulacaoAuto.getCaminhoPercorrido()),
                mapa.getEntradasSaidasNomes()
        );
        logger.info("Resultado da Simulação Automática: {}", resultadoAuto.toString());

        // Exportar resultado para JSON
        ExportarResultados.exportarParaJson(new LinkedList<ResultadoSimulacao>() {{
            add(resultadoAuto);
        }}, "resultado_simulacao.json");
        logger.info("Resultado da Simulação Automática exportado para 'resultado_simulacao.json'.");

        // ============ SIMULAÇÃO MANUAL ============
        logger.info("Iniciando a simulação manual...");
        SimulacaoManual simulacaoManual = new SimulacaoManual(mapa, toCruz);
        simulacaoManual.executar();

        ResultadoSimulacao resultadoManual = new ResultadoSimulacao(
                "MANUAL-001",
                divisaoInicial.getNomeDivisao(),
                toCruz.getPosicaoAtual().getNomeDivisao(),
                toCruz.getVida() > 0 ? "SUCESSO" : "FALHA",
                toCruz.getVida(),
                new LinkedList<>(), // Atualizar com o trajeto percorrido, se disponível
                mapa.getEntradasSaidasNomes()
        );
        logger.info("Resultado da Simulação Manual: {}", resultadoManual.toString());

        // ============ FINALIZAÇÃO ============
        logger.info("Programa finalizado com sucesso.");
    }

    /**
     * Método auxiliar para coletar o trajeto percorrido e convertê-lo para uma lista de nomes.
     *
     * @param caminhoPercorrido Lista de divisões percorridas.
     * @return Lista de nomes das divisões.
     */
    private static LinkedList<String> coletarTrajeto(LinkedList<Divisao> caminhoPercorrido) {
        LinkedList<String> trajetoNomes = new LinkedList<>();
        for (int i = 0; i < caminhoPercorrido.getSize(); i++) {
            trajetoNomes.add(caminhoPercorrido.getElementAt(i).getNomeDivisao());
        }
        return trajetoNomes;
    }
}
