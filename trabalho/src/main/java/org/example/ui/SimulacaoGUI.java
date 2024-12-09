package org.example.ui;

import org.example.api.implementation.models.Divisao;
import org.example.api.implementation.models.Mapa;
import org.example.api.implementation.models.ToCruz;
import org.example.api.implementation.utils.JsonUtils;
import org.example.collections.implementation.LinkedList;
import javax.swing.*;
import java.awt.*;

public class SimulacaoGUI extends JFrame {

    private LinkedList<Divisao> divisoes;
    private LinkedList<Conexao> conexoes;
    private ToCruz toCruz;
    private LinkedList<Point> posicoesDivisoes;

    public SimulacaoGUI(LinkedList<Divisao> divisoes, LinkedList<Conexao> conexoes, ToCruz toCruz) {
        this.divisoes = divisoes;
        this.conexoes = conexoes;
        this.toCruz = toCruz;
        this.posicoesDivisoes = new LinkedList<>();

        setTitle("Simulacao - To Cruz");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Adicionar o painel de mapa
        MapaPanel mapaPanel = new MapaPanel();
        add(mapaPanel, BorderLayout.CENTER);

        // Adicionar controles
        JPanel controlePanel = new JPanel();
        JButton moverButton = new JButton("Mover");
        moverButton.addActionListener(e -> moverToCruz(mapaPanel));
        controlePanel.add(moverButton);

        add(controlePanel, BorderLayout.SOUTH);

        gerarPosicoesDivisoes();
    }

    private void gerarPosicoesDivisoes() {
        // Gerar posições fixas para as divisões no mapa
        int xBase = 100, yBase = 100, offset = 100;

        for (int i = 0; i < divisoes.getSize(); i++) {
            int x = xBase + (i % 5) * offset;
            int y = yBase + (i / 5) * offset;
            posicoesDivisoes.add(new Point(x, y));
        }
    }

    private void moverToCruz(MapaPanel mapaPanel) {
        String destino = JOptionPane.showInputDialog(this, "Digite o nome da divisao:");
        Divisao novaDivisao = getDivisaoPorNome(destino);

        if (novaDivisao != null && podeMover(toCruz.getPosicaoAtual(), novaDivisao)) {
            toCruz.moverPara(novaDivisao);
            mapaPanel.repaint(); // Atualizar o desenho
        } else {
            JOptionPane.showMessageDialog(this, "Movimento invalido!");
        }
    }

    private Divisao getDivisaoPorNome(String nome) {
        for (Divisao divisao : divisoes) {
            if (divisao.getNomeDivisao().equals(nome)) {
                return divisao;
            }
        }
        return null;
    }

    private boolean podeMover(Divisao origem, Divisao destino) {
        for (Conexao conexao : conexoes) {
            if (conexao.conecta(origem, destino)) {
                return true;
            }
        }
        return false;
    }

    private class MapaPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Desenhar conexões
            g.setColor(Color.LIGHT_GRAY);
            for (Conexao conexao : conexoes) {
                Point pos1 = posicoesDivisoes.getElementAt(divisoes.indexOf(conexao.getDivisao1()));
                Point pos2 = posicoesDivisoes.getElementAt(divisoes.indexOf(conexao.getDivisao2()));
                g.drawLine(pos1.x, pos1.y, pos2.x, pos2.y);
            }

            // Desenhar divisões
            g.setColor(Color.BLUE);
            for (int i = 0; i < divisoes.getSize(); i++) {
                Point pos = posicoesDivisoes.getElementAt(i);
                g.fillOval(pos.x - 10, pos.y - 10, 20, 20);
                g.setColor(Color.BLACK);
                g.drawString(divisoes.getElementAt(i).getNomeDivisao(), pos.x - 20, pos.y - 15);
            }

            // Desenhar Tó Cruz
            if (toCruz.getPosicaoAtual() != null) {
                Point toCruzPos = posicoesDivisoes.getElementAt(divisoes.indexOf(toCruz.getPosicaoAtual()));
                g.setColor(Color.RED);
                g.fillOval(toCruzPos.x - 15, toCruzPos.y - 15, 30, 30);
                g.setColor(Color.BLACK);
                g.drawString("To Cruz", toCruzPos.x - 20, toCruzPos.y - 20);
            }
        }
    }

    // Classe Conexao
    private static class Conexao {
        private Divisao divisao1;
        private Divisao divisao2;

        public Conexao(Divisao divisao1, Divisao divisao2) {
            this.divisao1 = divisao1;
            this.divisao2 = divisao2;
        }

        public Divisao getDivisao1() {
            return divisao1;
        }

        public Divisao getDivisao2() {
            return divisao2;
        }

        public boolean conecta(Divisao d1, Divisao d2) {
            return (divisao1.equals(d1) && divisao2.equals(d2)) ||
                    (divisao1.equals(d2) && divisao2.equals(d1));
        }
    }

    public static void main(String[] args) {
        Mapa mapa = new Mapa();
        JsonUtils jsonUtils = new JsonUtils(mapa);
        String caminhoJson = "mapa.json";

        LinkedList<Divisao> divisoes = new LinkedList<>();
        divisoes.add(new Divisao("Sala A"));
        divisoes.add(new Divisao("Sala B"));
        divisoes.add(new Divisao("Corredor"));

        LinkedList<Conexao> conexoes = new LinkedList<>();
        conexoes.add(new Conexao(divisoes.getElementAt(0), divisoes.getElementAt(1)));
        conexoes.add(new Conexao(divisoes.getElementAt(1), divisoes.getElementAt(2)));

        ToCruz toCruz = new ToCruz("To Cruz", 100);
        toCruz.setPosicaoAtual(divisoes.getElementAt(0));

        SwingUtilities.invokeLater(() -> {
            SimulacaoGUI gui = new SimulacaoGUI(divisoes, conexoes, toCruz);
            gui.setVisible(true);
        });
    }
}

