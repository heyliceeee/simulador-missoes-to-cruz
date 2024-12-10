package org.example.api.implementation.services;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.CombateService;
import org.example.api.implementation.interfaces.Divisao;
import org.example.api.implementation.interfaces.Inimigo;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Implementação do serviço de combate.
 */
public class CombateServiceImpl implements CombateService {

    @Override
    public void resolverCombate(ToCruz toCruz, Divisao divisaoAtual) throws ElementNotFoundException {
        if (toCruz == null || divisaoAtual == null) {
            System.err.println("Erro: Agente ou divisão atual é nula.");
            return;
        }

        ArrayUnorderedList<Inimigo> inimigos = divisaoAtual.getInimigosPresentes();
        if (inimigos == null || inimigos.isEmpty()) {
            System.out.println("Nenhum inimigo na divisão.");
            return;
        }

        for (int i = 0; i < inimigos.size(); i++) {
            Inimigo inimigo = inimigos.getElementAt(i);
            if (inimigo == null) continue;

            while (toCruz.getVida() > 0 && inimigo.getPoder() > 0) {
                inimigo.sofrerDano(10); // Tó Cruz ataca o inimigo com um dano fixo de 10
                if (inimigo.getPoder() > 0) {
                    toCruz.sofrerDano(5); // O inimigo ataca com dano fixo de 5
                }
            }

            if (inimigo.getPoder() <= 0) {
                System.out.println("Inimigo '" + inimigo.getNome() + "' derrotado.");
                inimigos.remove(inimigo);
                i--; // Ajustar índice após remoção
            }

            if (toCruz.getVida() <= 0) {
                System.out.println("Tó Cruz foi derrotado!");
                return;
            }
        }

        System.out.println("Todos os inimigos foram derrotados.");
    }

}
