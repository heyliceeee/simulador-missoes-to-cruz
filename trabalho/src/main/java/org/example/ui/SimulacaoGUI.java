package org.example.ui;

import org.example.Main;
import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;
import org.example.api.implementation.models.*;
import org.example.api.implementation.utils.JsonUtils;
import org.example.collections.implementation.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class SimulacaoGUI extends JFrame {

    private LinkedList<Divisao> divisoes;
    private LinkedList<Ligacao> ligacoes;
    private ToCruz toCruz;
    private LinkedList<Point> posicoesDivisoes;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public SimulacaoGUI(LinkedList<Divisao> divisoes, LinkedList<Ligacao> ligacoes, ToCruz toCruz) {
        this.divisoes = divisoes;
        this.ligacoes = ligacoes;
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
            JOptionPane.showMessageDialog(this, "Movimento invalido (nao existe essa divisao ou nao ha ligacao direta)!");
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
        for (Ligacao ligacao : ligacoes) {
            if (ligacao.conecta(origem, destino)) {
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
            for (Ligacao ligacao : ligacoes) {
                Point pos1 = posicoesDivisoes.getElementAt(divisoes.indexOf(ligacao.getDivisao1()));
                Point pos2 = posicoesDivisoes.getElementAt(divisoes.indexOf(ligacao.getDivisao2()));
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

    public static void main(String[] args) {
        Mapa mapa = new Mapa();
        JsonUtils jsonUtils = new JsonUtils(mapa);
        String caminhoJson = "mapa.json";

        // mapa carregado apartir do JSON
        try {
            jsonUtils.carregarMapa(caminhoJson);
            logger.info("Mapa carregado com sucesso e pronto para uso!");

            // Verificar se o alvo foi carregado corretamente
            Alvo alvo = mapa.getAlvo();
            if (alvo != null) {
                logger.info("Alvo carregado do JSON: Divisão - {}, Tipo - {}", alvo.getDivisao().getNomeDivisao(), alvo.getTipo());
            } else {
                logger.error("Nenhum alvo definido no JSON ou erro ao carregar.");
                return;
            }
        } catch (InvalidJsonStructureException e) {
            logger.error("Erro na estrutura do JSON: {}", e.getMessage());
            return;
        } catch (InvalidFieldException e) {
            logger.error("Erro em um campo do JSON: {}", e.getMessage());
            return;
        } catch (DivisionNotFoundException e) {
            logger.error("Erro de referência de divisão: {}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Erro inesperado: {}", e.getMessage());
            e.printStackTrace();
            return;
        }

        if (mapa.getDivisoes().isEmpty()) {
            logger.error("Mapa não possui divisões carregadas. Encerrando o programa.");
            return;
        }

        //criar o to cruz e definir a sua posicao inicial
        ToCruz toCruz = new ToCruz("To Cruz", 100); // Nome e vida inicial
        Divisao divisaoInicial = mapa.getDivisoes().getElementAt(0); // Primeira divisão
        toCruz.moverPara(divisaoInicial);


        SwingUtilities.invokeLater(() -> {
            SimulacaoGUI gui = new SimulacaoGUI(mapa.getDivisoes(), mapa.getLigacoes(), toCruz);
            gui.setVisible(true);
        });
    }
}

