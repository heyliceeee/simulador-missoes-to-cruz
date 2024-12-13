package org.example.api.implementation.interfaces;

import org.example.api.implementation.models.ToCruz;
import org.example.api.exceptions.ElementNotFoundException;

/**
 * Interface para o servico que gerencia o combate entre o agente To Cruz e os
 * inimigos.
 *
 * <p>
 * O servico de combate e responsavel por lidar com interacoes entre o agente e
 * os
 * inimigos presentes em uma determinada divisao durante o progresso de uma
 * simulacao
 * ou missao.
 * </p>
 */
public interface ICombateService {
    /**
     * Resolve o combate entre o agente To Cruz e os inimigos presentes na divisao
     * atual.
     *
     * <p>
     * Este metodo gerencia toda a logica de combate, incluindo o calculo de danos,
     * ataques e a remocao de inimigos derrotados. O combate termina quando todos os
     * inimigos sao derrotados ou quando To Cruz e eliminado.
     * </p>
     *
     * @param toCruz       O agente controlado pelo jogador.
     * @param divisaoAtual A divisao onde o combate ocorre.
     * @throws ElementNotFoundException Se um inimigo esperado na divisao nao for
     *                                  encontrado.
     * @throws IllegalArgumentException Se `toCruz` ou `divisaoAtual` forem nulos.
     * @throws IllegalStateException    Se To Cruz for derrotado durante o combate.
     */
    void resolverCombate(ToCruz toCruz, IDivisao divisaoAtual) throws ElementNotFoundException;
}
