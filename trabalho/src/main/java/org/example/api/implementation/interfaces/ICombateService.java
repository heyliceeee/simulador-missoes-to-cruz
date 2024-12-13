package org.example.api.implementation.interfaces;

import org.example.api.implementation.models.ToCruz;
import org.example.api.exceptions.ElementNotFoundException;

/**
 * Interface para o serviço que gerencia o combate entre o agente Tó Cruz e os inimigos.
 *
 * <p>O serviço de combate é responsável por lidar com as interações entre o agente e os
 * inimigos presentes em uma determinada divisão durante o progresso de uma simulação
 * ou missão.</p>
 */
public interface ICombateService {

    /**
     * Resolve o combate entre o agente Tó Cruz e os inimigos presentes na divisão atual,
     * determinando quem ataca primeiro com base no contexto.
     *
     * <p>
     * - Se {@code inimigoEntrouAgora} for {@code false}, significa que Tó Cruz entrou na sala
     *   (Cenário 1 ou 5), então Tó Cruz ataca primeiro simultaneamente todos os inimigos.
     * </p>
     *
     * <p>
     * - Se {@code inimigoEntrouAgora} for {@code true}, significa que os inimigos entraram
     *   na sala de Tó Cruz (Cenário 3), então os inimigos atacam primeiro.
     * </p>
     *
     * <p>
     * Se não houver inimigos (Cenário 2 e 6), o combate não ocorre.
     * </p>
     *
     * @param toCruz             O agente controlado pelo jogador.
     * @param divisaoAtual       A divisão onde o combate ocorre.
     * @param inimigoEntrouAgora true se os inimigos entraram na sala do Tó Cruz nesta fase,
     *                           false se Tó Cruz entrou na sala dos inimigos.
     * @throws ElementNotFoundException se um inimigo esperado não for encontrado.
     * @throws IllegalArgumentException se {@code toCruz} ou {@code divisaoAtual} forem nulos.
     * @throws IllegalStateException    se Tó Cruz for derrotado durante o combate.
     */
    void resolverCombate(ToCruz toCruz, IDivisao divisaoAtual, boolean inimigoEntrouAgora) throws ElementNotFoundException;
}
