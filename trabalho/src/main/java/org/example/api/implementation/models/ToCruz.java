package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IAgente;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IItem;
import org.example.collections.implementation.ArrayStack;

/**
 * Representa o agente To Cruz com atributos como vida, posicao e inventario.
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
     * Posicao atual no mapa
     */
    private IDivisao posicaoAtual;

    /**
     * Inventario do To Cruz
     */
    private ArrayStack<IItem> inventario;

    /**
     * Indica se o objetivo principal (alvo) foi concluido ou capturado.
     *
     * Essa variavel e usada para rastrear o estado da missao.
     * - true: O alvo foi capturado com sucesso.
     * - false: O alvo ainda nao foi capturado.
     */
    private boolean alvoConcluido;

    /**
     * Construtor do To Cruz.
     *
     * @param nome Nome do agente.
     * @param vida Vida inicial do agente.
     */
    public ToCruz(String nome, int vida) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do agente invalido.");
        }
        if (vida < 0) {
            throw new IllegalArgumentException("Vida nao pode ser negativa.");
        }
        this.nome = nome;
        this.vida = vida;
        this.inventario = new ArrayStack<>();
        this.alvoConcluido = false;
    }

    /**
     * Indica se o objetivo principal (alvo) foi concluido ou capturado.
     *
     * Essa variavel e usada para rastrear o estado da missao.
     * - true: O alvo foi capturado com sucesso.
     * - false: O alvo ainda nao foi capturado.
     */
    @Override
    public void moverPara(IDivisao novaDivisao) {
        if (novaDivisao == null) {
            System.err.println("Erro: Divisao para mover e nula.");
            return;
        }
        this.posicaoAtual = novaDivisao;
        // System.out.println("To Cruz moveu-se para a divisao: " +
        // novaDivisao.getNomeDivisao());
    }

    /**
     * Usa um kit de vida do inventario para recuperar pontos de vida.
     */
    @Override
    public void usarKitDeVida() {
        if (inventario.isEmpty()) {
            System.out.println("Inventario vazio! Nao ha kits de vida para usar.");
            return;
        }

        IItem item = inventario.pop(); // Retira o item do topo da pilha
        if (item.getTipo().equals("kit de vida")) {
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

            // if(item.getTipo().equalsI("colete")) {
            // vida += item.getPontos(); // Recupera pontos de vida
            // System.out.println("Usou um colete! Vida atual: " + vida);

        } else {
            inventario.push(item); // Recoloca o item no topo da pilha
        }
    }

    /**
     * Adiciona um item ao inventario do To Cruz.
     *
     * @param item O item a ser adicionado.
     */
    @Override
    public void adicionarAoInventario(IItem item) {
        if (item == null) {
            System.err.println("Erro: Item a ser adicionado e nulo.");
            return;
        }

        switch (item.getTipo().toLowerCase()) {
            case "colete":
                // Adiciona os pontos de vida do colete, permitindo ultrapassar o limite de 100
                vida += item.getPontos();
                System.out.println(
                        "Consumiu um colete! Ganhou " + item.getPontos() + " pontos extras. Vida atual: " + vida);
                break;

            case "kit de vida":
                // Verifica o limite maximo de kits na mochila
                if (inventario.size() >= 5) { // Considerando 5 como limite configurado
                    System.out.println("Mochila cheia! Nao e possivel carregar mais kits de vida.");
                } else {
                    inventario.push(item);
                    System.out.println("Kit de vida adicionado ao inventario.");
                }
                break;

            default:
                // Outros tipos de itens
                inventario.push(item);
                System.out.println("Item adicionado ao inventario: " + item.getTipo());
                break;
        }
    }

    /**
     * Reduz os pontos de vida do To Cruz ao sofrer dano.
     *
     * @param dano Quantidade de dano recebido.
     */
    @Override
    public void sofrerDano(int dano) {
        if (dano < 0) {
            System.err.println("Erro: Dano nao pode ser negativo.");
            return;
        }
        vida -= dano;
        if (vida <= 0) {
            vida = 0;
            System.out.println("To Cruz foi derrotado!");
        } else {
            System.out.println("To Cruz sofreu " + dano + " de dano! Vida restante: " + vida);
        }
    }

    /**
     * Obtem a vida atual do To Cruz.
     *
     * @return Pontos de vida restantes.
     */
    @Override
    public int getVida() {
        return vida;
    }

    /**
     * Define a vida do To Cruz.
     *
     * @param vida Pontos de vida a serem atribuidos.
     */
    @Override
    public void setVida(int vida) {
        if (vida < 0) {
            throw new IllegalArgumentException("Vida nao pode ser negativa.");
        }
        this.vida = vida;
    }

    /**
     * Obtem a posicao atual do To Cruz.
     *
     * @return A divisao atual onde o To Cruz esta localizado.
     */
    @Override
    public IDivisao getPosicaoAtual() {
        return posicaoAtual;
    }

    /**
     * Define a posicao atual do To Cruz.
     *
     * @param posicaoAtual A nova divisao onde o To Cruz estara.
     */
    @Override
    public void setPosicaoAtual(IDivisao posicaoAtual) {
        if (posicaoAtual == null) {
            throw new IllegalArgumentException("Posicao atual nao pode ser nula.");
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
            throw new IllegalArgumentException("Nome do agente invalido.");
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
     * Verifica se o alvo foi concluido.
     *
     * @return true se o alvo foi capturado, false caso contrario.
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
        if (this.vida > this.vidaMaxima) { // Garante que nao ultrapassa o maximo
            this.vida = this.vidaMaxima;
        }
        System.out.println("Vida atual do To Cruz: " + this.vida);
    }

    /**
     * Define se o alvo foi concluido.
     *
     * @param concluido true se o alvo foi capturado, false caso contrario.
     */
    public void setAlvoConcluido(boolean concluido) {
        this.alvoConcluido = concluido;
    }
}
