package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IAgente;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IItem;
import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.implementation.ArrayStack;

/**
 * Representa o agente To Cruz com atributos como vida, posição e inventário.
 * <p>
 * O agente pode se mover entre divisões, interagir com itens, sofrer dano e
 * utilizar kits de vida. Ele possui um inventário limitado e uma vida máxima.
 * </p>
 */
public class ToCruz implements IAgente {

    /**
     * Nome do agente.
     */
    private String nome;

    /**
     * Pontos de vida do agente.
     */
    private int vida;

    /**
     * Pontos de vida máxima do agente.
     */
    private int vidaMaxima = 100;

    /**
     * Posição atual do agente no mapa.
     */
    private IDivisao posicaoAtual;

    /**
     * Inventário do agente, armazenado como uma pilha de itens.
     */
    private ArrayStack<IItem> inventario;

    /**
     * Indica se o objetivo principal (alvo) foi concluído ou capturado.
     */
    private boolean alvoConcluido;

    /**
     * Construtor do To Cruz.
     *
     * @param nome Nome do agente. Não pode ser nulo ou vazio.
     * @param vida Vida inicial do agente. Deve ser um valor não negativo.
     * @throws IllegalArgumentException se o nome for inválido ou a vida for negativa.
     */
    public ToCruz(String nome, int vida) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do agente não pode ser nulo ou vazio.");
        }
        if (vida < 0) {
            throw new IllegalArgumentException("Vida não pode ser negativa.");
        }
        this.nome = nome;
        this.vida = vida;
        this.inventario = new ArrayStack<>();
        this.alvoConcluido = false;
    }

    /**
     * Move o agente para uma nova divisão.
     *
     * @param novaDivisao A divisão para a qual o agente deve se mover.
     * @throws IllegalArgumentException se a divisão for nula.
     */
    @Override
    public void moverPara(IDivisao novaDivisao) {
        if (novaDivisao == null) {
            throw new IllegalArgumentException("Divisão para mover não pode ser nula.");
        }
        this.posicaoAtual = novaDivisao;
    }

    /**
     * Usa um kit de vida do inventário para recuperar pontos de vida.
     * <p>
     * Se o item retirado do inventário não for um kit de vida, ele será devolvido.
     * </p>
     *
     * @throws IllegalStateException se o inventário estiver vazio ou o item retirado não for um kit de vida.
     */
    @Override
    public void usarKitDeVida() {
        if (inventario.isEmpty()) {
            throw new IllegalStateException("Inventário vazio! Não há kits de vida para usar.");
        }

        try {
            IItem kit = inventario.pop();
            if ("kit de vida".equalsIgnoreCase(kit.getTipo())) {
                vida += kit.getPontos();
                if (vida > vidaMaxima) {
                    vida = vidaMaxima; // Limitar vida ao máximo.
                }
            } else {
                inventario.push(kit); // Recoloca o item no inventário se não for um kit de vida.
                throw new IllegalStateException("O item retirado do inventário não é um kit de vida.");
            }
        } catch (EmptyCollectionException e) {
            throw new IllegalStateException("Erro ao usar kit de vida: " + e.getMessage());
        }
    }

    /**
     * Adiciona um item ao inventário ou aplica seus efeitos imediatamente, dependendo do tipo.
     *
     * @param item O item a ser adicionado. Não pode ser nulo.
     * @throws IllegalArgumentException se o item for nulo.
     * @throws IllegalStateException    se o inventário estiver cheio ao tentar adicionar um kit de vida.
     */
    @Override
    public void adicionarAoInventario(IItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item a ser adicionado não pode ser nulo.");
        }

        switch (item.getTipo().toLowerCase()) {
            case "colete":
                vida += item.getPontos();
                break;
            case "kit de vida":
                if (inventario.size() >= 5) {
                    throw new IllegalStateException("Inventário cheio! Não é possível carregar mais kits de vida.");
                }
                inventario.push(item);
                break;
            default:
                inventario.push(item);
                break;
        }
    }

    /**
     * Aplica dano ao agente, reduzindo sua vida.
     *
     * @param dano Quantidade de dano a ser aplicada. Deve ser um valor não negativo.
     * @throws IllegalArgumentException se o dano for negativo.
     */
    @Override
    public void sofrerDano(int dano) {
        if (dano < 0) {
            throw new IllegalArgumentException("Dano não pode ser negativo.");
        }
        vida = Math.max(vida - dano, 0); // Garante que a vida nunca fique negativa.
    }

    /**
     * Obtém o nome do agente.
     *
     * @return O nome do agente.
     */
    @Override
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do agente.
     *
     * @param nome Novo nome do agente. Não pode ser nulo ou vazio.
     * @throws IllegalArgumentException se o nome for nulo ou vazio.
     */
    @Override
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do agente não pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }

    /**
     * Obtém a vida atual do agente.
     *
     * @return A vida atual do agente.
     */
    @Override
    public int getVida() {
        return vida;
    }

    /**
     * Define a vida do agente.
     *
     * @param vida Nova quantidade de vida. Deve ser um valor não negativo.
     * @throws IllegalArgumentException se a vida for negativa.
     */
    @Override
    public void setVida(int vida) {
        if (vida < 0) {
            throw new IllegalArgumentException("Vida não pode ser negativa.");
        }
        this.vida = vida;
    }

    /**
     * Obtém a posição atual do agente.
     *
     * @return A posição atual do agente no mapa.
     */
    @Override
    public IDivisao getPosicaoAtual() {
        return posicaoAtual;
    }

    /**
     * Define a posição atual do agente.
     *
     * @param posicaoAtual Nova posição do agente. Não pode ser nula.
     * @throws IllegalArgumentException se a divisão for nula.
     */
    @Override
    public void setPosicaoAtual(IDivisao posicaoAtual) {
        if (posicaoAtual == null) {
            throw new IllegalArgumentException("Posição atual não pode ser nula.");
        }
        this.posicaoAtual = posicaoAtual;
    }

    /**
     * Obtém o inventário do agente.
     *
     * @return O inventário como uma pilha de itens.
     */
    public ArrayStack<IItem> getInventario() {
        return inventario;
    }

    /**
     * Define o inventário do agente.
     *
     * @param inventario Nova pilha de itens. Não pode ser nula.
     * @throws IllegalArgumentException se o inventário for nulo.
     */
    public void setInventario(ArrayStack<IItem> inventario) {
        if (inventario == null) {
            throw new IllegalArgumentException("Inventário não pode ser nulo.");
        }
        this.inventario = inventario;
    }

    /**
     * Verifica se o objetivo do agente foi concluído.
     *
     * @return {@code true} se o objetivo foi concluído, {@code false} caso contrário.
     */
    public boolean isAlvoConcluido() {
        return alvoConcluido;
    }

    /**
     * Define o status do objetivo do agente.
     *
     * @param concluido {@code true} se o objetivo foi concluído, {@code false} caso contrário.
     */
    public void setAlvoConcluido(boolean concluido) {
        this.alvoConcluido = concluido;
    }
}
