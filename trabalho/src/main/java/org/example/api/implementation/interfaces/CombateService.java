package org.example.api.implementation.interfaces;

import org.example.api.implementation.models.ToCruz;
import org.example.api.exceptions.ElementNotFoundException;

/**
 * Interface para o serviço que gere o combate entre o Tó Cruz e os inimigos.
 */
public interface CombateService {
    /**
     * Resolve o combate na divisão atual.
     *
     * @param toCruz       O agente controlado pelo jogador.
     * @param divisaoAtual A divisão onde o combate ocorre.
     * @throws ElementNotFoundException Se um inimigo não for encontrado.
     */
    void resolverCombate(ToCruz toCruz, Divisao divisaoAtual) throws ElementNotFoundException;
}
