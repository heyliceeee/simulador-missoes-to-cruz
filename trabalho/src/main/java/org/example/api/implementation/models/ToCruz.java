package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IAgente;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IItem;
import org.example.collections.implementation.ArrayStack;

/**
 * Representa o agente Tó Cruz com atributos como vida, posição e inventário.
 */
public class ToCruz implements IAgente {

    /**
     * Nome do agente
     */
    private String nome;

    /**
     * Pontos de vida do agente
     */
    private int vida;

    /**
     * Pontos de vida maxima do agente
     */
    private int vidaMaxima = 100;

    /**
     * Posição atual no mapa
     */
    private IDivisao posicaoAtual;

    /**
     * Inventário do Tó Cruz
     */
    private ArrayStack<IItem> inventario;

    /**
     * Indica se o objetivo principal (alvo) foi concluído ou capturado.
     *
     * Essa variável é usada para rastrear o estado da missão.
     * - true: O alvo foi capturado com sucesso.
     * - false: O alvo ainda não foi capturado.
     */
    private boolean alvoConcluido;
    private final String icone = "\u1F920";

    /**
     * Construtor do Tó Cruz.
     *
     * @param nome Nome do agente.
     * @param vida Vida inicial do agente.
     */
    public ToCruz(String nome, int vida) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do agente inválido.");
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
     * Indica se o objetivo principal (alvo) foi concluído ou capturado.
     *
     * Essa variável é usada para rastrear o estado da missão.
     * - true: O alvo foi capturado com sucesso.
     * - false: O alvo ainda não foi capturado.
     */
    @Override
    public void moverPara(IDivisao novaDivisao) {
        if (novaDivisao == null) {
            System.err.println("Erro: Divisão para mover é nula.");
            return;
        }
        this.posicaoAtual = novaDivisao;
        // System.out.println("Tó Cruz moveu-se para a divisão: " +
        // novaDivisao.getNomeDivisao());
    }

    /**
     * Usa um kit de vida do inventário para recuperar pontos de vida.
     */
    @Override
    public void usarKitDeVida() {
        if (inventario.isEmpty()) {
            System.out.println("Inventário vazio! Não há kits de vida para usar.");
            return;
        }

        IItem item = inventario.pop(); // Retira o item do topo da pilha
        if (item.getTipo().equalsIgnoreCase("kit de vida")) {
            if (vida < vidaMaxima) {
                if (vida + item.getPontos() > vidaMaxima || vida + item.getPontos() == vidaMaxima) {
                    vida = vidaMaxima;
                    System.out.println("Usou um kit de vida! Vida atual: " + vida);
                } else if (vida + item.getPontos() < vidaMaxima) {
                    vida += item.getPontos();
                    System.out.println("Usou um kit de vida! Vida atual: " + vida);
                }
            }
            // }

            // if(item.getTipo().equalsIgnoreCase("colete")) {
            // vida += item.getPontos(); // Recupera pontos de vida
            // System.out.println("Usou um colete! Vida atual: " + vida);

        } else {
            inventario.push(item); // Recoloca o item no topo da pilha
        }
    }

    /**
     * Adiciona um item ao inventário do Tó Cruz.
     *
     * @param item O item a ser adicionado.
     */
    @Override
    public void adicionarAoInventario(IItem item) {
        if (item == null) {
            System.err.println("Erro: Item a ser adicionado é nulo.");
            return;
        }

        if ("colete".equalsIgnoreCase(item.getTipo())) {
            vida += item.getPontos();
            System.out
                    .println("Consumiu um colete! Ganhou " + item.getPontos() + " pontos extras. Vida atual: " + vida);
        } else {
            inventario.push(item);
            System.out.println("Item adicionado ao inventário: " + item.getTipo());
        }
    }

    /**
     * Reduz os pontos de vida do Tó Cruz ao sofrer dano.
     *
     * @param dano Quantidade de dano recebido.
     */
    @Override
    public void sofrerDano(int dano) {
        if (dano < 0) {
            System.err.println("Erro: Dano não pode ser negativo.");
            return;
        }
        vida -= dano;
        if (vida <= 0) {
            vida = 0;
            System.out.println("TO Cruz foi derrotado!");
        } else {
            System.out.println("TO Cruz sofreu " + dano + " de dano! Vida restante: " + vida);
        }
    }

    /**
     * Obtém a vida atual do Tó Cruz.
     *
     * @return Pontos de vida restantes.
     */
    @Override
    public int getVida() {
        return vida;
    }

    /**
     * Define a vida do Tó Cruz.
     *
     * @param vida Pontos de vida a serem atribuídos.
     */
    @Override
    public void setVida(int vida) {
        if (vida < 0) {
            throw new IllegalArgumentException("Vida não pode ser negativa.");
        }
        this.vida = vida;
    }

    /**
     * Obtém a posição atual do Tó Cruz.
     *
     * @return A divisão atual onde o Tó Cruz está localizado.
     */
    @Override
    public IDivisao getPosicaoAtual() {
        return posicaoAtual;
    }

    /**
     * Define a posição atual do Tó Cruz.
     *
     * @param posicaoAtual A nova divisão onde o Tó Cruz estará.
     */
    @Override
    public void setPosicaoAtual(IDivisao posicaoAtual) {
        if (posicaoAtual == null) {
            throw new IllegalArgumentException("Posição atual não pode ser nula.");
        }
        this.posicaoAtual = posicaoAtual;
    }

    /**
     * Obtem o nome do agente
     *
     * @return o nome
     */
    @Override
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do agente
     *
     * @param nome o nome do agente
     */
    @Override
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do agente inválido.");
        }
        this.nome = nome;
    }

    /**
     * Obtem o que o agente tem no inventario
     *
     * @return o inventario do agente
     */
    public ArrayStack<IItem> getInventario() {
        return inventario;
    }

    /**
     * Define o que o agente tem no seu inventario
     *
     * @param inventario a mochila do agente
     */
    public void setInventario(ArrayStack<IItem> inventario) {
        if (inventario == null) {
            throw new IllegalArgumentException("Inventario nao pode ser nulo.");
        }
        this.inventario = inventario;
    }

    /**
     * Verifica se o alvo foi concluído.
     *
     * @return true se o alvo foi capturado, false caso contrário.
     */
    public boolean isAlvoConcluido() {
        return alvoConcluido;
    }

    /**
     * Atualizar pontos de vida
     * 
     * @param pontos
     */
    public void recuperarVida(int pontos) {
        this.vida += pontos;
        if (this.vida > this.vidaMaxima) { // Garante que não ultrapassa o máximo
            this.vida = this.vidaMaxima;
        }
        System.out.println("Vida atual do Tó Cruz: " + this.vida);
    }

    /**
     * Define se o alvo foi concluído.
     *
     * @param concluido true se o alvo foi capturado, false caso contrário.
     */
    public void setAlvoConcluido(boolean concluido) {
        this.alvoConcluido = concluido;
    }

    public String getIcone() {
        return icone;
    }
}
