package org.example.api.implementation.interfaces;

public interface IInimigo {
    String getNome();

    void setNome(String nome);
    int getVida();

    void setVida(int vida);
    int getPoder();

    void setPoder(int poder);

    void sofrerDano(int dano);
}
