package org.example.api.implementation.interfaces;

public interface IAgente {
    String getNome();

    void setNome(String nome);

    int getVida();

    void setVida(int vida);

    IDivisao getPosicaoAtual();

    void setPosicaoAtual(IDivisao divisao);

    void moverPara(IDivisao novaDivisao);

    void sofrerDano(int dano);

    void adicionarAoInventario(IItem item);

    void usarKitDeVida();
}