package org.example.api.implementation.services;

import org.example.api.implementation.models.Divisao;
import org.example.api.implementation.models.Inimigo;
import org.example.api.implementation.models.ToCruz;

/**
 * Serviço que gere o combate entre o Tó Cruz e os inimigos.
 */
public class CombateService {

    /**
     * Resolve o combate na divisão atual.
     *
     * @param toCruz       O agente controlado pelo jogador.
     * @param divisaoAtual A divisão onde o combate ocorre.
     */
    public void resolverCombate(ToCruz toCruz, Divisao divisaoAtual) {
        if (divisaoAtual.getInimigosPresentes().getSize() == 0) {
            System.out.println("Nenhum inimigo na divisão.");
            return;
        }

        for (Inimigo inimigo : divisaoAtual.getInimigosPresentes()) {
            while (toCruz.getVida() > 0 && inimigo.getVida() > 0) {
                inimigo.sofrerDano(10);
                if (inimigo.getVida() > 0) {
                    toCruz.sofrerDano(inimigo.atacar());
                }
            }

            if (toCruz.getVida() <= 0) {
                System.out.println("Tó Cruz foi derrotado!");
                return;
            }
        }

        divisaoAtual.getInimigosPresentes().clear();
        System.out.println("Todos os inimigos foram derrotados.");
    }
}