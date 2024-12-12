package org.example.api.implementation.interfaces;

public interface Agente {
    String getNome();

    void setNome(String nome);

    int getVida();

    void setVida(int vida);

    Divisao getPosicaoAtual();

    void setPosicaoAtual(Divisao divisao);

    void moverPara(Divisao novaDivisao);

    void sofrerDano(int dano);

    void adicionarAoInventario(Item item);

    void usarKitDeVida();
    
}