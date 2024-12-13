package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IDivisao;

/**
 * Classe auxiliar para representar uma conexão bidirecional entre duas divisões.
 * <p>
 * A ligação estabelece uma relação entre duas divisões, permitindo verificar
 * conectividade, acessar a outra divisão conectada e comparar objetos de ligação.
 * </p>
 */
public class Ligacao {

    private IDivisao divisao1;
    private IDivisao divisao2;

    /**
     * Construtor para criar uma ligação entre duas divisões.
     *
     * @param divisao1 A primeira divisão.
     * @param divisao2 A segunda divisão.
     */
    public Ligacao(IDivisao divisao1, IDivisao divisao2) {
        this.divisao1 = divisao1;
        this.divisao2 = divisao2;
    }

    /**
     * Retorna a primeira divisão desta ligação.
     *
     * @return A primeira divisão.
     */
    public IDivisao getDivisao1() {
        return divisao1;
    }

    /**
     * Retorna a segunda divisão desta ligação.
     *
     * @return A segunda divisão.
     */
    public IDivisao getDivisao2() {
        return divisao2;
    }

    /**
     * Verifica se esta ligação é igual a outra.
     * <p>
     * Duas ligações são consideradas iguais se conectarem as mesmas divisões,
     * independentemente da ordem.
     * </p>
     *
     * @param obj O objeto a ser comparado.
     * @return {@code true} se as ligações forem iguais, {@code false} caso contrário.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Ligacao ligacao = (Ligacao) obj;
        return (divisao1.equals(ligacao.divisao1) && divisao2.equals(ligacao.divisao2)) ||
               (divisao1.equals(ligacao.divisao2) && divisao2.equals(ligacao.divisao1));
    }

    /**
     * Verifica se as duas divisões estão conectadas por esta ligação.
     *
     * @param d1 Uma divisão.
     * @param d2 Outra divisão.
     * @return {@code true} se as divisões estão conectadas, {@code false} caso contrário.
     */
    public boolean conecta(IDivisao d1, IDivisao d2) {
        return (divisao1.equals(d1) && divisao2.equals(d2)) ||
               (divisao1.equals(d2) && divisao2.equals(d1));
    }

    /**
     * Retorna a outra divisão conectada a uma divisão fornecida.
     * <p>
     * Se a divisão fornecida não fizer parte desta ligação, retorna {@code null}.
     * </p>
     *
     * @param divisao A divisão para a qual deseja obter a outra conectada.
     * @return A outra divisão conectada ou {@code null} se a divisão não fizer parte desta ligação.
     */
    public IDivisao getOutraDivisao(IDivisao divisao) {
        if (divisao.equals(divisao1)) {
            return divisao2;
        } else if (divisao.equals(divisao2)) {
            return divisao1;
        }
        return null;
    }

    /**
     * Retorna uma representação textual da ligação.
     *
     * @return Uma string no formato {@code Ligacao{divisao1=..., divisao2=...}}.
     */
    @Override
    public String toString() {
        return "Ligacao{" +
               "divisao1=" + divisao1 +
               ", divisao2=" + divisao2 +
               '}';
    }

    /**
     * Calcula o código hash da ligação.
     * <p>
     * O código hash é baseado nos códigos hash das duas divisões conectadas.
     * </p>
     *
     * @return O código hash da ligação.
     */
    @Override
    public int hashCode() {
        return divisao1.hashCode() + divisao2.hashCode();
    }
}
