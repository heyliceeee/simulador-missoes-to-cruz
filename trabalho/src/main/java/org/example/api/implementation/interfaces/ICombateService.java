package org.example.api.implementation.interfaces;

import org.example.api.implementation.models.ToCruz;
import org.example.api.exceptions.ElementNotFoundException;

/**
 * Interface para o servico que gerencia o combate entre o agente To Cruz e os
 * inimigos.
 *
 * <p>
 * O servico de combate e responsavel por lidar com as interacoes entre o agente
 * e os
 * inimigos presentes em uma determinada divisao durante o progresso de uma
 * simulacao
 * ou missao.
 * </p>
 */
public interface ICombateService {

    /**
     * Resolve o combate entre o agente To Cruz e os inimigos presentes na divisao
     * atual,
     * determinando quem ataca primeiro com base no contexto.
     *
     * <p>
     * - Se {@code inimigoEntrouAgora} for {@code false}, significa que To Cruz
     * entrou na sala
     * (Cenario 1 ou 5), entao To Cruz ataca primeiro simultaneamente todos os
     * inimigos.
     * </p>
     *
     * <p>
     * - Se {@code inimigoEntrouAgora} for {@code true}, significa que os inimigos
     * entraram
     * na sala de To Cruz (Cenario 3), entao os inimigos atacam primeiro.
     * </p>
     *
     * <p>
     * Se nao houver inimigos (Cenario 2 e 6), o combate nao ocorre.
     * </p>
     *
     * @param toCruz             O agente controlado pelo jogador.
     * @param divisaoAtual       A divisao onde o combate ocorre.
     * @param inimigoEntrouAgora true se os inimigos entraram na sala do To Cruz
     *                           nesta fase,
     *                           false se To Cruz entrou na sala dos inimigos.
     * @throws ElementNotFoundException se um inimigo esperado nao for encontrado.
     * @throws IllegalArgumentException se {@code toCruz} ou {@code divisaoAtual}
     *                                  forem nulos.
     * @throws IllegalStateException    se To Cruz for derrotado durante o combate.
     */
    void resolverCombate(ToCruz toCruz, IDivisao divisaoAtual, boolean inimigoEntrouAgora)
            throws ElementNotFoundException;
}
