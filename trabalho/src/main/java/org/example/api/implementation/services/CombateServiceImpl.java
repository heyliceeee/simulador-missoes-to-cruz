package org.example.api.implementation.services;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.ICombateService;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IInimigo;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.implementation.ArrayUnorderedList;

import static org.example.api.implementation.simulation.SimulacaoAutomaticaImpl.*;

/**
 * Implementacao do servico de combate.
 */
public class CombateServiceImpl implements ICombateService {

    public CombateServiceImpl() {
    }

    /**
     * Resolve o combate na divisao atual.
     *
     * @param toCruz       O agente controlado pelo jogador.
     * @param divisaoAtual A divisao onde o combate ocorre.
     * @throws ElementNotFoundException Se um inimigo nao for encontrado.
     */
    @Override
    public void resolverCombate(ToCruz toCruz, IDivisao divisaoAtual) throws ElementNotFoundException {
        if (toCruz == null || divisaoAtual == null) {
            System.err.println("Erro: Agente ou divisao atual e nula.");
            return;
        }

        ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
        if (inimigos == null || inimigos.isEmpty()) {
            System.out.println("Nenhum inimigo na divisao.");
            return;
        }

        System.out.println(crossedSwords + " Combate iniciado na divisao: " + divisaoAtual.getNomeDivisao());
        for (int i = 0; i < inimigos.size(); i++) {
            IInimigo inimigo = inimigos.getElementAt(i);
            if (inimigo == null)
                continue;

            System.out.println("\uD83D\uDD34 To Cruz enfrenta o inimigo: " + inimigo.getNome());

            // Prioridade para o ataque do inimigo
            if (toCruz.getVida() > 0 && inimigo.getPoder() > 0) {
                toCruz.sofrerDano(5); // Inimigo ataca com dano fixo de 5
                System.out.println(crossedSwords + " Inimigo '" + inimigo.getNome() + "' atacou To Cruz!");
            }

            // Combate direto
            while (toCruz.getVida() > 0 && inimigo.getPoder() > 0) {
                inimigo.sofrerDano(10); // To Cruz ataca o inimigo com um dano fixo de 10
                System.out.println("\uD83D\uDFE2 To Cruz atacou o inimigo '" + inimigo.getNome() + "'!");

                if (inimigo.getPoder() > 0) {
                    toCruz.sofrerDano(5); // O inimigo ataca novamente
                    System.out.println(crossedSwords + " Inimigo '" + inimigo.getNome() + "' contra-atacou!");
                }
            }

            // Verificar o estado do inimigo
            if (inimigo.getPoder() <= 0) {
                System.out.println(skull + " Inimigo '" + inimigo.getNome() + "' foi derrotado!");
                inimigos.remove(inimigo);
                i--; // Ajustar indice apos remocao
            }

            // Verificar o estado de To Cruz
            if (toCruz.getVida() <= 0) {
                System.err.println(skull + " To Cruz foi derrotado!");
                return;
            }
        }

        System.out.println(trophy + " Todos os inimigos na sala foram derrotados!");
    }
}
