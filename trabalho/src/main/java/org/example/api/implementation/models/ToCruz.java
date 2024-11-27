package org.example.api.implementation.models;

import org.example.collections.implementation.ArrayStack;
import org.example.collections.implementation.Stack;


/**
 * Representa o agente, com vida, posicao e inventario
 */
public class ToCruz {
    int vida;
    Divisao posicaoAtual;
    ArrayStack<Item> inventario;

    /**
     * Tó Cruz ataca todos os inimigos na divisão
     * Reduz os pontos de vida dos inimigos e do Tó Cruz conforme o poder de ataque
     */
    public void atacar() {
        for (Inimigo inimigo : inimigosNaDivisao) {
            inimigo.vida -= poderAtaque;
            this.vida -= inimigo.poder;
        }
        inimigosNaDivisao.removeIf(inimigo -> inimigo.vida <= 0);
    }


    /**
     * atualiza a posicao
     * @param novaDivisao
     */
    public void moverPara(Divisao novaDivisao) {
        if (podeMover(posicaoAtual, novaDivisao)) {
            posicaoAtual = novaDivisao;
        } else {
            System.out.println("Movimento inválido!");
        }
    }



    /**
     * retira um kit da queue e aumenta os pontos de vida
     */
    public void usarKit() {
        if (!inventario.isEmpty()) {
            Item kit = inventario.pop();
            vida = Math.min(vida + kit.pontosRecuperados, 100);
        }
    }
}
