package org.example.api.implementation.services;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.ICombateService;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IInimigo;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Implementação do serviço de combate.
 */
public class CombateServiceImpl implements ICombateService {

    public CombateServiceImpl() {
    }

    /**
     * Resolve o combate na divisão atual.
     *
     * @param toCruz       O agente controlado pelo jogador.
     * @param divisaoAtual A divisão onde o combate ocorre.
     * @throws ElementNotFoundException Se um inimigo não for encontrado.
     */
    @Override
    public void resolverCombate(ToCruz toCruz, IDivisao divisaoAtual) throws ElementNotFoundException {
        if (toCruz == null || divisaoAtual == null) {
            System.err.println("Erro: Agente ou divisao atual é nula.");
            return;
        }

        ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
        if (inimigos == null || inimigos.isEmpty()) {
            System.out.println("Nenhum inimigo na divisão.");
            return;
        }

        for (int i = 0; i < inimigos.size(); i++) {
            IInimigo inimigo = inimigos.getElementAt(i);
            if (inimigo == null)
                continue;

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
