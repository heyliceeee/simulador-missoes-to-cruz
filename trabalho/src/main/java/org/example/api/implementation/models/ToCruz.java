package org.example.api.implementation.models;

import org.example.collections.implementation.ArrayStack;

/**
 * Representa o agente Tó Cruz com atributos como vida, posição e inventário.
 */
public class ToCruz {

    /**
     * Nome do agente
     */
    private String nome;

    /**
     * Pontos de vida do agente
     */
    private int vida;

    /**
     * Posição atual no mapa
     */
    private Divisao posicaoAtual;

    /**
     * Inventário do Tó Cruz
     */
    private ArrayStack<Item> inventario;

    /**
     * Indica se o objetivo principal (alvo) foi concluído ou capturado.
     * 
     * Essa variável é usada para rastrear o estado da missão.
     * - true: O alvo foi capturado com sucesso.
     * - false: O alvo ainda não foi capturado.
     */
    private boolean alvoConcluido;

    /**
     * Construtor do Tó Cruz.
     *
     * @param nome Nome do agente.
     * @param vida Vida inicial do agente.
     */
    public ToCruz(String nome, int vida) {
        this.nome = nome;
        this.vida = vida;
        this.inventario = new ArrayStack<>(); // Inicializa o inventário
    }

    /**
     * Move o Tó Cruz para uma nova divisão.
     *
     * @param novaDivisao A nova divisão para onde o Tó Cruz vai se mover.
     */
    public void moverPara(Divisao novaDivisao) {
        this.posicaoAtual = novaDivisao;
        System.out.println("Tó Cruz moveu-se para a divisão: " + novaDivisao.getNomeDivisao());
    }

    /**
     * Usa um kit de vida do inventário para recuperar pontos de vida.
     */
    public void usarKitDeVida() {
        if (inventario.isEmpty()) {
            System.out.println("Inventário vazio! Não há kits de vida para usar.");
            return;
        }

        Item kit = inventario.pop(); // Retira o item do topo da pilha
        if (kit.getTipo().equals("kit de vida")) {
            vida += kit.getPontos(); // Recupera pontos de vida
            System.out.println("Usou um kit de vida! Vida atual: " + vida);
        } else {
            System.out.println("O item no topo do inventário não é um kit de vida.");
            inventario.push(kit); // Recoloca o item no topo da pilha
        }
    }

    /**
     * Adiciona um item ao inventário do Tó Cruz.
     *
     * @param item O item a ser adicionado.
     */
    public void adicionarAoInventario(Item item) {
        inventario.push(item);
        System.out.println("Item adicionado ao inventário: " + item.getTipo());
    }

    /**
     * Reduz os pontos de vida do Tó Cruz ao sofrer dano.
     *
     * @param dano Quantidade de dano recebido.
     */
    public void sofrerDano(int dano) {
        vida -= dano;
        if (vida <= 0) {
            System.out.println("Tó Cruz foi derrotado!");
        } else {
            System.out.println("Tó Cruz sofreu dano! Vida restante: " + vida);
        }
    }

    /**
     * Obtém a vida atual do Tó Cruz.
     *
     * @return Pontos de vida restantes.
     */
    public int getVida() {
        return vida;
    }

    /**
     * Define a vida do Tó Cruz.
     *
     * @param vida Pontos de vida a serem atribuídos.
     */
    public void setVida(int vida) {
        this.vida = vida;
    }

    /**
     * Obtém a posição atual do Tó Cruz.
     *
     * @return A divisão atual onde o Tó Cruz está localizado.
     */
    public Divisao getPosicaoAtual() {
        return posicaoAtual;
    }

    /**
     * Define a posição atual do Tó Cruz.
     *
     * @param posicaoAtual A nova divisão onde o Tó Cruz estará.
     */
    public void setPosicaoAtual(Divisao posicaoAtual) {
        this.posicaoAtual = posicaoAtual;
    }

    /**
     * Obtem o nome do agente
     * 
     * @return o nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do agente
     * 
     * @param nome o nome do agente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtem o que o agente tem no inventario
     * 
     * @return o inventario do agente
     */
    public ArrayStack<Item> getInventario() {
        return inventario;
    }

    /**
     * Define o que o agente tem no seu inventario
     * 
     * @param inventario a mochila do agente
     */
    public void setInventario(ArrayStack<Item> inventario) {
        this.inventario = inventario;
    }

    /**
     * Define se o alvo foi concluído.
     *
     * @param concluido true se o alvo foi capturado, false caso contrário.
     */
    public void setAlvoConcluido(boolean concluido) {
        this.alvoConcluido = concluido;
    }

    /**
     * Verifica se o alvo foi concluído.
     *
     * @return true se o alvo foi capturado, false caso contrário.
     */
    public boolean isAlvoConcluido() {
        return alvoConcluido;
    }

}
