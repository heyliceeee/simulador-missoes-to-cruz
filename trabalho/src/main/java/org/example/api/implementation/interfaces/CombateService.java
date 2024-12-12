package org.example.api.implementation.interfaces;

import org.example.api.implementation.models.ToCruz;
import org.example.api.exceptions.ElementNotFoundException;

/**
 * Interface para o serviço que gerencia o combate entre o agente Tó Cruz e os
 * inimigos.
 * 
 * <p>
 * O serviço de combate é responsável por lidar com interações entre o agente e
 * os
 * inimigos presentes em uma determinada divisão durante o progresso de uma
 * simulação
 * ou missão.
 * </p>
 */
public interface CombateService {

    /**
     * Resolve o combate entre o agente Tó Cruz e os inimigos presentes na divisão
     * atual.
     *
     * <p>
     * Este método gerencia toda a lógica de combate, incluindo o cálculo de danos,
     * ataques e a remoção de inimigos derrotados. O combate termina quando todos os
     * inimigos são derrotados ou quando Tó Cruz é eliminado.
     * </p>
     *
     * @param toCruz       O agente controlado pelo jogador.
     * @param divisaoAtual A divisão onde o combate ocorre.
     * @throws ElementNotFoundException Se um inimigo esperado na divisão não for
     *                                  encontrado.
     * @throws IllegalArgumentException Se `toCruz` ou `divisaoAtual` forem nulos.
     * @throws IllegalStateException    Se Tó Cruz for derrotado durante o combate.
     */
    void resolverCombate(ToCruz toCruz, Divisao divisaoAtual) throws ElementNotFoundException;
}
