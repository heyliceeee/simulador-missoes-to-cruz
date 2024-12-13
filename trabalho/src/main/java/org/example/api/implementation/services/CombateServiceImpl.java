package org.example.api.implementation.services;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.ICombateService;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IInimigo;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.implementation.ArrayUnorderedList;

import static org.example.api.implementation.simulation.SimulacaoAutomaticaImpl.*;

/**
 * Implementação do serviço de combate cobrindo diferentes cenários de interação entre Tó Cruz e inimigos.
 * <p>
 * O combate segue as regras:
 * <ul>
 *     <li><b>Cenário 1 e 5:</b> Tó Cruz entra na sala com inimigos e ataca primeiro.</li>
 *     <li><b>Cenário 3:</b> Inimigos entram na sala de Tó Cruz e atacam primeiro.</li>
 *     <li><b>Cenário 2 e 6:</b> Sem inimigos na sala, nenhum combate ocorre.</li>
 * </ul>
 * Este serviço é ativado durante a simulação (manual ou automática) quando há movimentação de Tó Cruz ou inimigos.
 */
public class CombateServiceImpl implements ICombateService {

    /**
     * Construtor padrão da classe.
     */
    public CombateServiceImpl() {
    }

    /**
     * Resolve o combate considerando quem ataca primeiro, determinado pelo parâmetro {@code inimigoEntrouAgora}.
     * <p>
     * Se {@code inimigoEntrouAgora} for {@code true}, os inimigos atacam primeiro (Cenário 3).
     * Caso contrário, Tó Cruz ataca primeiro (Cenário 1 ou 5).
     * </p>
     *
     * @param toCruz             O agente Tó Cruz.
     * @param divisaoAtual       A divisão onde o combate ocorre.
     * @param inimigoEntrouAgora {@code true} se os inimigos entraram na sala de Tó Cruz nesta fase,
     *                           {@code false} se Tó Cruz entrou na sala dos inimigos.
     * @throws ElementNotFoundException se ocorrer erro ao acessar os inimigos.
     * @throws IllegalArgumentException se {@code toCruz} ou {@code divisaoAtual} forem nulos.
     */
    @Override
    public void resolverCombate(ToCruz toCruz, IDivisao divisaoAtual, boolean inimigoEntrouAgora) throws ElementNotFoundException {
        if (toCruz == null || divisaoAtual == null) {
            throw new IllegalArgumentException("toCruz ou divisaoAtual não podem ser nulos.");
        }

        ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
        if (inimigos == null || inimigos.isEmpty()) {
            System.out.println("Nenhum inimigo na divisão.");
            return; // Sem inimigos, nenhum combate ocorre.
        }

        if (inimigoEntrouAgora) {
            resolverCombateInimigoPrimeiro(toCruz, divisaoAtual);
        } else {
            resolverCombateToCruzPrimeiro(toCruz, divisaoAtual);
        }
    }

    /**
     * Realiza o combate onde Tó Cruz ataca primeiro.
     *
     * @param toCruz       O agente Tó Cruz.
     * @param divisaoAtual A divisão com inimigos.
     * @throws ElementNotFoundException se houver erro ao acessar os inimigos.
     */
    private void resolverCombateToCruzPrimeiro(ToCruz toCruz, IDivisao divisaoAtual) throws ElementNotFoundException {
        ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
        System.out.println(crossedSwords + " Combate iniciado (Tó Cruz primeiro) na divisão: " + divisaoAtual.getNomeDivisao());

        for (IInimigo inimigo : inimigos) {
            if (inimigo != null && inimigo.getPoder() > 0) {
                inimigo.sofrerDano(10); // Dano fixo de Tó Cruz.
                System.out.println("🟢 Tó Cruz atacou o inimigo '" + inimigo.getNome() + "'!");
            }
        }

        removerInimigosMortos(inimigos);

        if (inimigos.isEmpty()) {
            System.out.println(trophy + " Todos os inimigos na sala foram derrotados!");
            return;
        }

        combateCorpoACorpo(toCruz, inimigos);
    }

    /**
     * Realiza o combate onde os inimigos atacam primeiro.
     *
     * @param toCruz       O agente Tó Cruz.
     * @param divisaoAtual A divisão com inimigos.
     * @throws ElementNotFoundException se houver erro ao acessar os inimigos.
     */
    private void resolverCombateInimigoPrimeiro(ToCruz toCruz, IDivisao divisaoAtual) throws ElementNotFoundException {
        ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
        System.out.println(crossedSwords + " Combate iniciado (Inimigo primeiro) na divisão: " + divisaoAtual.getNomeDivisao());

        for (IInimigo inimigo : inimigos) {
            if (inimigo.getPoder() > 0 && toCruz.getVida() > 0) {
                toCruz.sofrerDano(5); // Dano fixo dos inimigos.
                System.out.println(crossedSwords + " Inimigo '" + inimigo.getNome() + "' atacou Tó Cruz!");
            }
        }

        if (toCruz.getVida() <= 0) {
            System.err.println(skull + " Tó Cruz foi derrotado!");
            return;
        }

        combateCorpoACorpo(toCruz, inimigos);
    }

    /**
     * Realiza o combate corpo a corpo alternado entre Tó Cruz e os inimigos.
     *
     * @param toCruz   O agente Tó Cruz.
     * @param inimigos Lista de inimigos presentes na divisão.
     * @throws ElementNotFoundException se houver erro ao acessar os inimigos.
     */
    private void combateCorpoACorpo(ToCruz toCruz, ArrayUnorderedList<IInimigo> inimigos) throws ElementNotFoundException {
        while (toCruz.getVida() > 0 && existeInimigoVivo(inimigos)) {
            IInimigo alvo = getPrimeiroInimigoVivo(inimigos);
            if (alvo != null && alvo.getPoder() > 0) {
                alvo.sofrerDano(10);
                System.out.println("🟢 Tó Cruz atacou o inimigo '" + alvo.getNome() + "'!");
            }

            removerInimigosMortos(inimigos);

            if (toCruz.getVida() <= 0 || !existeInimigoVivo(inimigos)) {
                break;
            }

            alvo = getPrimeiroInimigoVivo(inimigos);
            if (alvo != null && alvo.getPoder() > 0) {
                toCruz.sofrerDano(5);
                System.out.println(crossedSwords + " Inimigo '" + alvo.getNome() + "' contra-atacou!");
            }

            removerInimigosMortos(inimigos);
        }

        if (toCruz.getVida() <= 0) {
            System.err.println(skull + " Tó Cruz foi derrotado!");
        } else if (!existeInimigoVivo(inimigos)) {
            System.out.println(trophy + " Todos os inimigos na sala foram derrotados!");
        }
    }

    /**
     * Remove inimigos mortos (poder <= 0) da lista.
     *
     * @param inimigos Lista de inimigos.
     * @throws ElementNotFoundException se ocorrer erro ao acessar elementos.
     */
    private void removerInimigosMortos(ArrayUnorderedList<IInimigo> inimigos) throws ElementNotFoundException {
        for (int i = 0; i < inimigos.size(); i++) {
            IInimigo inimigo = inimigos.getElementAt(i);
            if (inimigo != null && inimigo.getPoder() <= 0) {
                System.out.println(skull + " Inimigo '" + inimigo.getNome() + "' foi derrotado!");
                inimigos.remove(inimigo);
                i--;
            }
        }
    }

    /**
     * Verifica se existe algum inimigo vivo na lista.
     *
     * @param inimigos Lista de inimigos.
     * @return {@code true} se existir algum inimigo vivo, {@code false} caso contrário.
     */
    private boolean existeInimigoVivo(ArrayUnorderedList<IInimigo> inimigos) {
        for (IInimigo inimigo : inimigos) {
            if (inimigo != null && inimigo.getPoder() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtém o primeiro inimigo vivo (poder > 0) da lista.
     *
     * @param inimigos Lista de inimigos.
     * @return O primeiro inimigo vivo ou {@code null} se todos estiverem mortos.
     */
    private IInimigo getPrimeiroInimigoVivo(ArrayUnorderedList<IInimigo> inimigos) {
        for (IInimigo inimigo : inimigos) {
            if (inimigo != null && inimigo.getPoder() > 0) {
                return inimigo;
            }
        }
        return null;
    }
}
