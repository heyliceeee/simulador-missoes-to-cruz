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

        for (int i = 0; i < divisaoAtual.getInimigosPresentes().getSize(); i++) {
            Inimigo inimigo = divisaoAtual.getInimigosPresentes().getElementAt(i);
            while (toCruz.getVida() > 0 && inimigo.getPoder() > 0) { // Corrigido para getPoder()
                inimigo.sofrerDano(10); // Tó Cruz ataca o inimigo com um dano fixo de 10
                if (inimigo.getPoder() > 0) { // Corrigido para getPoder()
                    toCruz.sofrerDano(5); // O inimigo ataca com dano fixo de 5
                }
            }

            if (toCruz.getVida() <= 0) {
                System.out.println("To Cruz foi derrotado!");
                return;
            }
        }

        divisaoAtual.getInimigosPresentes().clear();
        System.out.println("Todos os inimigos foram derrotados.");
    }
}
