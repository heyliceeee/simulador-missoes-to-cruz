package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.Agente;
import org.example.api.implementation.interfaces.Divisao;
import org.example.api.implementation.interfaces.Item;
import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.implementation.ArrayStack;

/**
 * Representa o agente Tó Cruz com atributos como vida, posição e inventário.
 */
public class ToCruz implements Agente {

    private String nome;
    private int vida;
    private Divisao posicaoAtual;
    private ArrayStack<Item> inventario;
    private boolean alvoConcluido;

    /**
     * Construtor do Tó Cruz.
     *
     * @param nome Nome do agente.
     * @param vida Vida inicial do agente.
     * @throws IllegalArgumentException se o nome for inválido ou a vida for
     *                                  negativa.
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
    public void moverPara(Divisao novaDivisao) {
        if (novaDivisao == null) {
            throw new IllegalArgumentException("Divisão para mover não pode ser nula.");
        }
        this.posicaoAtual = novaDivisao;
    }

    /**
     * Usa um kit de vida do inventário para recuperar pontos de vida.
     *
     * @throws IllegalStateException se o inventário estiver vazio ou se o item não
     *                               for um kit de vida.
     */
    @Override
    public void usarKitDeVida() {
        if (inventario.isEmpty()) {
            throw new IllegalStateException("Inventário vazio! Não há kits de vida para usar.");
        }

        try {
            Item kit = inventario.pop();
            if ("kit de vida".equalsIgnoreCase(kit.getTipo())) {
                vida += kit.getPontos();
                if (vida > 100) {
                    vida = 100; // Limitar vida ao máximo de 100.
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
     * Adiciona um item ao inventário ou aplica seus efeitos imediatamente,
     * dependendo do tipo.
     *
     * @param item O item a ser adicionado.
     * @throws IllegalArgumentException se o item for nulo.
     * @throws IllegalStateException    se o inventário estiver cheio ao tentar
     *                                  adicionar um kit de vida.
     */
    @Override
    public void adicionarAoInventario(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item a ser adicionado não pode ser nulo.");
        }

        switch (item.getTipo().toLowerCase()) {
            case "colete":
                vida += item.getPontos(); // Adiciona pontos de vida, ultrapassando o limite de 100, se permitido.
                break;
            case "kit de vida":
                if (inventario.size() >= 5) {
                    throw new IllegalStateException("Inventário cheio! Não é possível carregar mais kits de vida.");
                }
                inventario.push(item);
                break;
            default:
                inventario.push(item); // Outros itens são adicionados diretamente.
                break;
        }
    }

    /**
     * Aplica dano ao agente, reduzindo sua vida.
     *
     * @param dano Quantidade de dano a ser aplicada.
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
     * Obtém a vida atual do agente.
     *
     * @return Vida do agente.
     */
    @Override
    public int getVida() {
        return vida;
    }

    /**
     * Define a vida do agente.
     *
     * @param vida Nova quantidade de vida.
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
     * @return A divisão onde o agente está atualmente.
     */
    @Override
    public Divisao getPosicaoAtual() {
        return posicaoAtual;
    }

    /**
     * Define a posição atual do agente.
     *
     * @param posicaoAtual A nova divisão onde o agente estará.
     * @throws IllegalArgumentException se a divisão for nula.
     */
    @Override
    public void setPosicaoAtual(Divisao posicaoAtual) {
        if (posicaoAtual == null) {
            throw new IllegalArgumentException("Posição atual não pode ser nula.");
        }
        this.posicaoAtual = posicaoAtual;
    }

    /**
     * Obtém o nome do agente.
     *
     * @return Nome do agente.
     */
    @Override
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do agente.
     *
     * @param nome Novo nome do agente.
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
     * Obtém o inventário do agente.
     *
     * @return O inventário como uma pilha de itens.
     */
    public ArrayStack<Item> getInventario() {
        return inventario;
    }

    /**
     * Define o inventário do agente.
     *
     * @param inventario Nova pilha de itens.
     * @throws IllegalArgumentException se o inventário for nulo.
     */
    public void setInventario(ArrayStack<Item> inventario) {
        if (inventario == null) {
            throw new IllegalArgumentException("Inventário não pode ser nulo.");
        }
        this.inventario = inventario;
    }

    /**
     * Verifica se o objetivo do agente foi concluído.
     *
     * @return true se o objetivo foi concluído, false caso contrário.
     */
    public boolean isAlvoConcluido() {
        return alvoConcluido;
    }

    /**
     * Define o status do objetivo do agente.
     *
     * @param concluido true se o objetivo foi concluído, false caso contrário.
     */
    public void setAlvoConcluido(boolean concluido) {
        this.alvoConcluido = concluido;
    }
}
