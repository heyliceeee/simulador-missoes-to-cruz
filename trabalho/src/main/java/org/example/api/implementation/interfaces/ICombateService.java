package org.example.api.implementation.interfaces;

import org.example.api.implementation.models.ToCruz;
import org.example.api.exceptions.ElementNotFoundException;

/**
 * Interface para o servico que gere o combate entre o To Cruz e os inimigos.
 */
public interface ICombateService {
    /**
     * Resolve o combate na divisao atual.
     *
     * @param toCruz       O agente controlado pelo jogador.
     * @param divisaoAtual A divisao onde o combate ocorre.
     * @throws ElementNotFoundException Se um inimigo nao for encontrado.
     */
    void resolverCombate(ToCruz toCruz, IDivisao divisaoAtual) throws ElementNotFoundException;
}
