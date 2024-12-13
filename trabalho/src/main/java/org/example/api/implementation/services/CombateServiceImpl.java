package org.example.api.implementation.services;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.ICombateService;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IInimigo;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.implementation.ArrayUnorderedList;

import static org.example.api.implementation.simulation.SimulacaoAutomaticaImpl.*;

/**
 * Implementacao do servico de combate cobrindo todos os cenarios:
 * 
 * - Se To Cruz entra na sala com inimigos (Cenario 1 e 5): To Cruz ataca
 * primeiro simultaneamente.
 * - Se inimigos entram na sala de To Cruz (Cenario 3): inimigos atacam
 * primeiro.
 * - Caso nao haja inimigos (Cenario 2, 6): nenhum combate ocorre.
 * 
 * Este servico e chamado durante a simulacao (automatica ou manual) apos To
 * Cruz ou inimigos se moverem.
 */
public class CombateServiceImpl implements ICombateService {

    public CombateServiceImpl() {
    }

    /**
     * Resolve o combate levando em conta quem ataca primeiro, determinado pelo
     * parametro inimigoEntrouAgora.
     *
     * @param toCruz             O agente To Cruz.
     * @param divisaoAtual       A divisao onde o combate ocorre.
     * @param inimigoEntrouAgora true se os inimigos entraram na sala de To Cruz
     *                           nesta fase (cenario 3),
     *                           false se To Cruz entrou na sala dos inimigos
     *                           (cenario 1 ou 5).
     * @throws ElementNotFoundException se ocorrer erro ao acessar os inimigos.
     */
    @Override
    public void resolverCombate(ToCruz toCruz, IDivisao divisaoAtual, boolean inimigoEntrouAgora)
            throws ElementNotFoundException {
        if (toCruz == null || divisaoAtual == null) {
            throw new IllegalArgumentException("toCruz ou divisaoAtual nao podem ser nulos.");
        }

        ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
        if (inimigos == null || inimigos.isEmpty()) {
            System.out.println("Nenhum inimigo na divisao.");
            // Cenarios 2 e 6: sem inimigos, nenhum combate.
            return;
        }

        if (inimigoEntrouAgora) {
            // Cenario 3: Inimigos atacam primeiro
            resolverCombateInimigoPrimeiro(toCruz, divisaoAtual);
        } else {
            // Cenario 1 e 5: To Cruz ataca primeiro
            resolverCombateToCruzPrimeiro(toCruz, divisaoAtual);
        }
    }

    /**
     * Combate no qual To Cruz ataca primeiro simultaneamente todos os inimigos.
     * 
     * Usado quando To Cruz entra em uma sala com inimigos (cenario 1) ou encontra o
     * alvo com inimigos (cenario 5).
     *
     * @param toCruz       O agente To Cruz.
     * @param divisaoAtual A divisao com inimigos.
     * @throws ElementNotFoundException Se houver falha ao acessar inimigos.
     */
    private void resolverCombateToCruzPrimeiro(ToCruz toCruz, IDivisao divisaoAtual) throws ElementNotFoundException {
        ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
        System.out.println(
                crossedSwords + " Combate iniciado (To Cruz primeiro) na divisao: " + divisaoAtual.getNomeDivisao());

        // To Cruz ataca todos os inimigos simultaneamente
        for (IInimigo inimigo : inimigos) {
            if (inimigo != null && inimigo.getPoder() > 0) {
                inimigo.sofrerDano(10); // Dano fixo de To Cruz
                System.out.println("🟢 To Cruz atacou o inimigo '" + inimigo.getNome() + "'!");
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
     * Usado quando os inimigos entram na sala de To Cruz durante a fase dos
     * inimigos (cenario 3).
     *
     * @param toCruz       O agente To Cruz.
     * @param divisaoAtual A divisao com inimigos.
     * @throws ElementNotFoundException Se houver falha ao acessar inimigos.
     */
    private void resolverCombateInimigoPrimeiro(ToCruz toCruz, IDivisao divisaoAtual) throws ElementNotFoundException {
        ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
        System.out.println(
                crossedSwords + " Combate iniciado (Inimigo primeiro) na divisao: " + divisaoAtual.getNomeDivisao());

        // Inimigos atacam primeiro
        for (IInimigo inimigo : inimigos) {
            if (inimigo.getPoder() > 0 && toCruz.getVida() > 0) {
                toCruz.sofrerDano(5); // Dano fixo dos inimigos
                System.out.println(crossedSwords + " Inimigo '" + inimigo.getNome() + "' atacou To Cruz!");
            }
        }

        if (toCruz.getVida() <= 0) {
            System.err.println(skull + " To Cruz foi derrotado!");
            return;
        }

        // Agora combate alternado
        combateCorpoACorpo(toCruz, inimigos);
    }

    /**
     * Combate corpo a corpo alternado: enquanto To Cruz e inimigos estiverem vivos,
     * To Cruz ataca um inimigo e o inimigo contra-ataca, ate um dos lados ser
     * derrotado.
     * 
     * @throws ElementNotFoundException
     */
    private void combateCorpoACorpo(ToCruz toCruz, ArrayUnorderedList<IInimigo> inimigos)
            throws ElementNotFoundException {
        while (toCruz.getVida() > 0 && existeInimigoVivo(inimigos)) {
            IInimigo alvo = getPrimeiroInimigoVivo(inimigos);
            if (alvo != null && alvo.getPoder() > 0) {
                alvo.sofrerDano(10); // To Cruz ataca
                System.out.println("🟢 To Cruz atacou o inimigo '" + alvo.getNome() + "'!");
            }

            removerInimigosMortos(inimigos);
            if (toCruz.getVida() <= 0 || !existeInimigoVivo(inimigos))
                break;

            alvo = getPrimeiroInimigoVivo(inimigos);
            if (alvo != null && alvo.getPoder() > 0) {
                toCruz.sofrerDano(5); // Inimigo contra-ataca
                System.out.println(crossedSwords + " Inimigo '" + alvo.getNome() + "' contra-atacou!");
            }

            removerInimigosMortos(inimigos);
        }

        if (toCruz.getVida() <= 0) {
            System.err.println(skull + " To Cruz foi derrotado!");
            return;
        }

        if (!existeInimigoVivo(inimigos)) {
            System.out.println(trophy + " Todos os inimigos na sala foram derrotados!");
        }
    }

    /**
     * Remove inimigos mortos (poder <= 0) da lista.
     * 
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
     * Obtem o primeiro inimigo vivo (poder > 0).
     */
    private IInimigo getPrimeiroInimigoVivo(ArrayUnorderedList<IInimigo> inimigos) {
        for (int i = 0; i < inimigos.size(); i++) {
            IInimigo in = inimigos.getElementAt(i);
            if (in != null && in.getPoder() > 0)
                return in;
        }
        return null;
    }
}
