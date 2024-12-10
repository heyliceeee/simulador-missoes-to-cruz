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
    private final String icone = "🤠";

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

    @Override
    public void moverPara(Divisao novaDivisao) {
        if (novaDivisao == null) {
            System.err.println("Erro: Divisão para mover é nula.");
            return;
        }
        this.posicaoAtual = novaDivisao;
        //System.out.println("Tó Cruz moveu-se para a divisão: " + novaDivisao.getNomeDivisao());
    }

    @Override
    public void usarKitDeVida() {
        if (inventario.isEmpty()) {
            System.out.println("Inventário vazio! Não há kits de vida para usar.");
            return;
        }

        try {
            Item kit = inventario.pop();
            if ("kit de vida".equalsIgnoreCase(kit.getTipo())) {
                vida += kit.getPontos();
                System.out.println("Usou um kit de vida! Vida atual: " + vida);
            } else {
                System.out.println("O item no topo do inventário não é um kit de vida.");
                inventario.push(kit);
            }
        } catch (EmptyCollectionException e) {
            System.err.println("Erro ao usar kit de vida: " + e.getMessage());
        }
    }

    @Override
    public void adicionarAoInventario(Item item) {
        if (item == null) {
            System.err.println("Erro: Item a ser adicionado é nulo.");
            return;
        }

        if ("colete".equalsIgnoreCase(item.getTipo())) {
            vida += item.getPontos();
            System.out.println("Consumiu um colete! Ganhou " + item.getPontos() + " pontos extras. Vida atual: " + vida);
        } else {
            inventario.push(item);
            System.out.println("Item adicionado ao inventário: " + item.getTipo());
        }
    }

    @Override
    public void sofrerDano(int dano) {
        if (dano < 0) {
            System.err.println("Erro: Dano não pode ser negativo.");
            return;
        }
        vida -= dano;
        if (vida <= 0) {
            vida = 0;
            System.out.println("Tó Cruz foi derrotado!");
        } else {
            System.out.println("Tó Cruz sofreu " + dano + " de dano! Vida restante: " + vida);
        }
    }

    @Override
    public int getVida() {
        return vida;
    }

    @Override
    public void setVida(int vida) {
        if (vida < 0) {
            throw new IllegalArgumentException("Vida não pode ser negativa.");
        }
        this.vida = vida;
    }

    @Override
    public Divisao getPosicaoAtual() {
        return posicaoAtual;
    }

    @Override
    public void setPosicaoAtual(Divisao posicaoAtual) {
        if (posicaoAtual == null) {
            throw new IllegalArgumentException("Posição atual não pode ser nula.");
        }
        this.posicaoAtual = posicaoAtual;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do agente inválido.");
        }
        this.nome = nome;
    }

    public ArrayStack<Item> getInventario() {
        return inventario;
    }

    public void setInventario(ArrayStack<Item> inventario) {
        if (inventario == null) {
            throw new IllegalArgumentException("Inventário não pode ser nulo.");
        }
        this.inventario = inventario;
    }

    public boolean isAlvoConcluido() {
        return alvoConcluido;
    }

    public void setAlvoConcluido(boolean concluido) {
        this.alvoConcluido = concluido;
    }

    public String getIcone() {
        return icone;
    }
}
