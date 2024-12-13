package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IAgente;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IItem;
import org.example.collections.exceptions.EmptyCollectionException;
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
     * @throws IllegalArgumentException se o nome for invalido ou a vida for
     *                                  negativa.
     */
    public ToCruz(String nome, int vida) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do agente nao pode ser nulo ou vazio.");
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
     * Move o agente para uma nova divisao.
     *
     * @param novaDivisao A divisao para a qual o agente deve se mover.
     * @throws IllegalArgumentException se a divisao for nula.
     */
    @Override
    public void moverPara(IDivisao novaDivisao) {
        if (novaDivisao == null) {
            throw new IllegalArgumentException("Divisao para mover nao pode ser nula.");
        }
        this.posicaoAtual = novaDivisao;
    }

    /**
     * Usa um kit de vida do inventario para recuperar pontos de vida.
     *
     * @throws IllegalStateException se o inventario estiver vazio ou se o item nao
     *                               for um kit de vida.
     */
    @Override
    public void usarKitDeVida() {
        if (inventario.isEmpty()) {
            throw new IllegalStateException("Inventario vazio! Nao ha kits de vida para usar.");
        }

        try {
            IItem kit = inventario.pop();
            if ("kit de vida".equalsIgnoreCase(kit.getTipo())) {
                vida += kit.getPontos();
                if (vida > this.vidaMaxima) {
                    vida = this.vidaMaxima; // Limitar vida ao maximo.
                }
            } else {
                inventario.push(kit); // Recoloca o item no inventario se nao for um kit de vida.
                throw new IllegalStateException("O item retirado do inventario nao e um kit de vida.");
            }
        } catch (EmptyCollectionException e) {
            throw new IllegalStateException("Erro ao usar kit de vida: " + e.getMessage());
        }
    }

    /**
     * Adiciona um item ao inventario ou aplica seus efeitos imediatamente,
     * dependendo do tipo.
     *
     * @param item O item a ser adicionado.
     * @throws IllegalArgumentException se o item for nulo.
     * @throws IllegalStateException    se o inventario estiver cheio ao tentar
     *                                  adicionar um kit de vida.
     */
    @Override
    public void adicionarAoInventario(IItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item a ser adicionado nao pode ser nulo.");
        }

        switch (item.getTipo().toLowerCase()) {
            case "colete":
                vida += item.getPontos(); // Adiciona pontos de vida, ultrapassando o limite de maximo, se permitido.
                break;
            case "kit de vida":
                if (inventario.size() >= 5) {
                    throw new IllegalStateException("Inventario cheio! Nao e possivel carregar mais kits de vida.");
                }
                inventario.push(item);
                break;
            default:
                inventario.push(item); // Outros itens sao adicionados diretamente.
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
            throw new IllegalArgumentException("Dano nao pode ser negativo.");
        }
        vida = Math.max(vida - dano, 0); // Garante que a vida nunca fique negativa.
    }

    /**
     * Obtem a vida atual do agente.
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
            throw new IllegalArgumentException("Vida nao pode ser negativa.");
        }
        this.vida = vida;
    }

    /**
     * Obtem a posicao atual do agente.
     *
     * @return A divisao onde o agente esta atualmente.
     */
    @Override
    public IDivisao getPosicaoAtual() {
        return posicaoAtual;
    }

    /**
     * Define a posicao atual do agente.
     *
     * @param posicaoAtual A nova divisao onde o agente estara.
     * @throws IllegalArgumentException se a divisao for nula.
     */
    @Override
    public void setPosicaoAtual(IDivisao posicaoAtual) {
        if (posicaoAtual == null) {
            throw new IllegalArgumentException("Posicao atual nao pode ser nula.");
        }
        this.posicaoAtual = posicaoAtual;
    }

    /**
     * Obtem o nome do agente.
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
            throw new IllegalArgumentException("Nome do agente nao pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }

    /**
     * Obtem o inventario do agente.
     *
     * @return O inventario como uma pilha de itens.
     */
    public ArrayStack<IItem> getInventario() {
        return inventario;
    }

    /**
     * Define o inventario do agente.
     *
     * @param inventario Nova pilha de itens.
     * @throws IllegalArgumentException se o inventario for nulo.
     */
    public void setInventario(ArrayStack<IItem> inventario) {
        if (inventario == null) {
            throw new IllegalArgumentException("Inventario nao pode ser nulo.");
        }
        this.inventario = inventario;
    }

    /**
     * Verifica se o objetivo do agente foi concluido.
     *
     * @return true se o objetivo foi concluido, false caso contrario.
     */
    public boolean isAlvoConcluido() {
        return alvoConcluido;
    }

    /**
     * Define o status do objetivo do agente.
     *
     * @param concluido true se o objetivo foi concluido, false caso contrario.
     */
    public void setAlvoConcluido(boolean concluido) {
        this.alvoConcluido = concluido;
    }
}
