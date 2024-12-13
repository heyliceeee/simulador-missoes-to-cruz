package org.example.api.implementation.services;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.ICombateService;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IInimigo;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.implementation.ArrayUnorderedList;

import static org.example.api.implementation.simulation.SimulacaoAutomaticaImpl.*;

/**
 * Implementação do serviço de combate cobrindo todos os cenários:
 * 
 * - Se Tó Cruz entra na sala com inimigos (Cenário 1 e 5): Tó Cruz ataca primeiro simultaneamente.
 * - Se inimigos entram na sala de Tó Cruz (Cenário 3): inimigos atacam primeiro.
 * - Caso não haja inimigos (Cenário 2, 6): nenhum combate ocorre.
 * 
 * Este serviço é chamado durante a simulação (automática ou manual) após Tó Cruz ou inimigos se moverem.
 */
public class CombateServiceImpl implements ICombateService {

    public CombateServiceImpl() {
    }

    /**
     * Resolve o combate levando em conta quem ataca primeiro, determinado pelo parâmetro inimigoEntrouAgora.
     *
     * @param toCruz             O agente Tó Cruz.
     * @param divisaoAtual       A divisão onde o combate ocorre.
     * @param inimigoEntrouAgora true se os inimigos entraram na sala de Tó Cruz nesta fase (cenário 3),
     *                           false se Tó Cruz entrou na sala dos inimigos (cenário 1 ou 5).
     * @throws ElementNotFoundException se ocorrer erro ao acessar os inimigos.
     */
    @Override
    public void resolverCombate(ToCruz toCruz, IDivisao divisaoAtual, boolean inimigoEntrouAgora) throws ElementNotFoundException {
        if (toCruz == null || divisaoAtual == null) {
            throw new IllegalArgumentException("toCruz ou divisaoAtual não podem ser nulos.");
        }

        ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
        if (inimigos == null || inimigos.isEmpty()) {
            System.out.println("Nenhum inimigo na divisão.");
            // Cenários 2 e 6: sem inimigos, nenhum combate.
            return;
        }

        if (inimigoEntrouAgora) {
            // Cenário 3: Inimigos atacam primeiro
            resolverCombateInimigoPrimeiro(toCruz, divisaoAtual);
        } else {
            // Cenário 1 e 5: Tó Cruz ataca primeiro
            resolverCombateToCruzPrimeiro(toCruz, divisaoAtual);
        }
    }

    /**
     * Combate no qual Tó Cruz ataca primeiro simultaneamente todos os inimigos.
     * 
     * Usado quando Tó Cruz entra em uma sala com inimigos (cenário 1) ou encontra o alvo com inimigos (cenário 5).
     *
     * @param toCruz       O agente Tó Cruz.
     * @param divisaoAtual A divisão com inimigos.
     * @throws ElementNotFoundException Se houver falha ao acessar inimigos.
     */
    private void resolverCombateToCruzPrimeiro(ToCruz toCruz, IDivisao divisaoAtual) throws ElementNotFoundException {
        ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
        System.out.println(crossedSwords + " Combate iniciado (Tó Cruz primeiro) na divisão: " + divisaoAtual.getNomeDivisao());

        // Tó Cruz ataca todos os inimigos simultaneamente
        for (IInimigo inimigo : inimigos) {
            if (inimigo != null && inimigo.getPoder() > 0) {
                inimigo.sofrerDano(10); // Dano fixo de Tó Cruz
                System.out.println("🟢 Tó Cruz atacou o inimigo '" + inimigo.getNome() + "'!");
            }
        }

        removerInimigosMortos(inimigos);

        // Se todos morreram, combate acaba
        if (inimigos.isEmpty()) {
            System.out.println(trophy + " Todos os inimigos na sala foram derrotados!");
            return;
        }

        // Caso restem inimigos, combate alternado
        combateCorpoACorpo(toCruz, inimigos);
    }

    /**
     * Combate no qual os inimigos atacam primeiro.
     * 
     * Usado quando os inimigos entram na sala de Tó Cruz durante a fase dos inimigos (cenário 3).
     *
     * @param toCruz       O agente Tó Cruz.
     * @param divisaoAtual A divisão com inimigos.
     * @throws ElementNotFoundException Se houver falha ao acessar inimigos.
     */
    private void resolverCombateInimigoPrimeiro(ToCruz toCruz, IDivisao divisaoAtual) throws ElementNotFoundException {
        ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
        System.out.println(crossedSwords + " Combate iniciado (Inimigo primeiro) na divisão: " + divisaoAtual.getNomeDivisao());

        // Inimigos atacam primeiro
        for (IInimigo inimigo : inimigos) {
            if (inimigo.getPoder() > 0 && toCruz.getVida() > 0) {
                toCruz.sofrerDano(5); // Dano fixo dos inimigos
                System.out.println(crossedSwords + " Inimigo '" + inimigo.getNome() + "' atacou Tó Cruz!");
            }
        }

        if (toCruz.getVida() <= 0) {
            System.err.println(skull + " Tó Cruz foi derrotado!");
            return;
        }

        // Agora combate alternado
        combateCorpoACorpo(toCruz, inimigos);
    }

    /**
     * Combate corpo a corpo alternado: enquanto Tó Cruz e inimigos estiverem vivos, 
     * Tó Cruz ataca um inimigo e o inimigo contra-ataca, até um dos lados ser derrotado.
          * @throws ElementNotFoundException 
          */
         private void combateCorpoACorpo(ToCruz toCruz, ArrayUnorderedList<IInimigo> inimigos) throws ElementNotFoundException {
        while (toCruz.getVida() > 0 && existeInimigoVivo(inimigos)) {
            IInimigo alvo = getPrimeiroInimigoVivo(inimigos);
            if (alvo != null && alvo.getPoder() > 0) {
                alvo.sofrerDano(10); // Tó Cruz ataca
                System.out.println("🟢 Tó Cruz atacou o inimigo '" + alvo.getNome() + "'!");
            }

            removerInimigosMortos(inimigos);
            if (toCruz.getVida() <= 0 || !existeInimigoVivo(inimigos)) break;

            alvo = getPrimeiroInimigoVivo(inimigos);
            if (alvo != null && alvo.getPoder() > 0) {
                toCruz.sofrerDano(5); // Inimigo contra-ataca
                System.out.println(crossedSwords + " Inimigo '" + alvo.getNome() + "' contra-atacou!");
            }

            removerInimigosMortos(inimigos);
        }

        if (toCruz.getVida() <= 0) {
            System.err.println(skull + " Tó Cruz foi derrotado!");
            return;
        }

        if (!existeInimigoVivo(inimigos)) {
            System.out.println(trophy + " Todos os inimigos na sala foram derrotados!");
        }
    }

    /**
     * Remove inimigos mortos (poder <= 0) da lista.
          * @throws ElementNotFoundException 
          */
         private void removerInimigosMortos(ArrayUnorderedList<IInimigo> inimigos) throws ElementNotFoundException {
        for (int i = 0; i < inimigos.size(); i++) {
            IInimigo in = inimigos.getElementAt(i);
            if (in != null && in.getPoder() <= 0) {
                System.out.println(skull + " Inimigo '" + in.getNome() + "' foi derrotado!");
                inimigos.remove(in);
                i--;
            }
        }
    }

    /**
     * Verifica se existe algum inimigo vivo na lista.
     */
    private boolean existeInimigoVivo(ArrayUnorderedList<IInimigo> inimigos) {
        for (int i = 0; i < inimigos.size(); i++) {
            IInimigo in = inimigos.getElementAt(i);
            if (in != null && in.getPoder() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtém o primeiro inimigo vivo (poder > 0).
     */
    private IInimigo getPrimeiroInimigoVivo(ArrayUnorderedList<IInimigo> inimigos) {
        for (int i = 0; i < inimigos.size(); i++) {
            IInimigo in = inimigos.getElementAt(i);
            if (in != null && in.getPoder() > 0) return in;
        }
        return null;
    }
}
