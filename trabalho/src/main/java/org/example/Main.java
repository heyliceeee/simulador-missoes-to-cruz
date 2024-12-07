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

        // Inicialização do Mapa
        Mapa mapa = new Mapa();
        JsonUtils jsonUtils = new JsonUtils(mapa);

        String caminhoJson = "mapa.json";

        try {
            jsonUtils.carregarMapa(caminhoJson);
            logger.info("Mapa carregado com sucesso e pronto para uso!");
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

        // Verifique se o mapa foi carregado corretamente antes de prosseguir
        if (mapa.getDivisoes().isEmpty()) {
            logger.error("Mapa não possui divisões carregadas. Encerrando o programa.");
            return;
        }

        // Inicialização do Agente (ToCruz)
        ToCruz toCruz = new ToCruz("Tó Cruz", 100); // Nome e vida inicial

        // Definir a posição inicial do agente (assumindo que há pelo menos uma divisão)
        Divisao divisaoInicial = mapa.getDivisoes().getElementAt(0); // Escolhe a primeira divisão como inicial
        toCruz.moverPara(divisaoInicial);
        logger.info("Agente {} iniciado na divisão: {}", toCruz.getNome(), divisaoInicial.getNomeDivisao());

        // Definir o alvo da missão (assumindo que há pelo menos uma divisão no mapa)
        Divisao divisaoObjetivo = mapa.getDivisoes().getElementAt(mapa.getDivisoes().getSize() - 1); // Última divisão como objetivo
        mapa.definirAlvo(divisaoObjetivo.getNomeDivisao(), "ObjetoValioso");
        logger.info("Alvo definido na divisão: {}", divisaoObjetivo.getNomeDivisao());

       // Executar Simulação Automática
        SimulacaoAutomatica simulacaoAuto = new SimulacaoAutomatica(mapa, toCruz);
        simulacaoAuto.executar(divisaoObjetivo);

        // Coletar resultados da Simulação Automática
        LinkedList<ResultadoSimulacao> resultadosSimulacao = new LinkedList<>();

        // Criar ResultadoSimulacao
        ResultadoSimulacao resultadoAuto = new ResultadoSimulacao(
                "AUTO-001",
                divisaoInicial.getNomeDivisao(),
                simulacaoAuto.getDivisaoFinal().getNomeDivisao(),
                simulacaoAuto.getStatus(),
                simulacaoAuto.getVidaRestante(),
                coletarTrajeto(simulacaoAuto.getCaminhoPercorrido()),
                mapa.getEntradasSaidasNomes()
        );

        // Adicionar o resultado à lista
        resultadosSimulacao.add(resultadoAuto);

        // Exportar os resultados para um arquivo JSON
        ExportarResultados.exportarParaJson(resultadosSimulacao, "resultado_simulacao.json");

        logger.info("Resultado da Simulação Automática exportado para 'resultado_simulacao.json'.");
        //logger.info("Resultado da Simulação Automática: {}", resultadoAuto);

        // Executar Simulação Manual
        SimulacaoManual simulacaoManual = new SimulacaoManual(mapa, toCruz);
        simulacaoManual.executar();

        // Coletar resultados da Simulação Manual
        // Nota: Dependendo da implementação, você pode precisar ajustar como os dados são coletados
        // Aqui, assumimos que SimulacaoManual também atualiza o caminhoPercorrido ou similar
        // Por simplicidade, deixamos o trajeto como vazio
        ResultadoSimulacao resultadoManual = new ResultadoSimulacao(
                "MANUAL-001",
                divisaoInicial.getNomeDivisao(),
                toCruz.getPosicaoAtual().getNomeDivisao(),
                toCruz.getVida() > 0 ? "SUCESSO" : "FALHA",
                toCruz.getVida(),
                new LinkedList<>(), // Implementar coleta de trajeto se necessário
                mapa.getEntradasSaidasNomes()
        );

        logger.info("Resultado da Simulação Manual: {}", resultadoManual.toString());

        // Finalizar o Programa
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
