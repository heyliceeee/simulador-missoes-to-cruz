package org.example.ui;

import org.example.Main;
import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;
import org.example.api.implementation.models.*;
import org.example.api.implementation.services.CombateService;
import org.example.api.implementation.utils.JsonUtils;
import org.example.collections.implementation.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;

public class SimulacaoManualGUI extends JFrame {

    private LinkedList<Divisao> divisoes;
    private LinkedList<Divisao> entradasSaidas;
    private LinkedList<Ligacao> ligacoes;
    private Mapa mapa;
    private ToCruz toCruz;
    private LinkedList<Point> posicoesDivisoes;
    private Image toCruzImage;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private CombateService combateService;



    public SimulacaoManualGUI(LinkedList<Divisao> divisoes, LinkedList<Divisao> entradasSaidas, LinkedList<Ligacao> ligacoes, Mapa mapa, ToCruz toCruz) {
        this.divisoes = divisoes;
        this.mapa = mapa;
        this.ligacoes = ligacoes;
        this.entradasSaidas = entradasSaidas;
        this.toCruz = toCruz;
        this.posicoesDivisoes = new LinkedList<>();
        this.combateService = new CombateService();


        setTitle("Simulacao - To Cruz");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Adicionar o painel de mapa
        MapaPanel mapaPanel = new MapaPanel();
        add(mapaPanel, BorderLayout.CENTER);

        // Adicionar controlos
        JPanel controlePanel = new JPanel();
        inicializarControlos(controlePanel, mapaPanel);

        add(controlePanel, BorderLayout.SOUTH);
        carregarImagens();
        gerarPosicoesDivisoes();
        interagirComAlvo(toCruz.getPosicaoAtual());
    }

    /**
     * Interage com o alvo se estiver na mesma divisão.
     * Se houver inimigos, avisa o jogador que deve eliminá-los antes.
     *
     * @param divisao A divisão onde o Tó Cruz está.
     */
    private void interagirComAlvo(Divisao divisao) {
        if (mapa.getAlvo() != null && mapa.getAlvo().getDivisao().equals(divisao)) {
            if (divisao.getInimigosPresentes().getSize() > 0) {
                System.out.println("O alvo está nesta sala, mas há inimigos! Elimine-os primeiro.");
            } else {
                System.out.println("O alvo foi resgatado com sucesso!");
                mapa.removerAlvo();
                toCruz.setAlvoConcluido(true);
            }
        }
    }

    /**
     * criar botoes de acao
     * @param controlePanel
     * @param mapaPanel mapa do edificio
     */
    private void inicializarControlos(JPanel controlePanel, MapaPanel mapaPanel) {
        JButton moverButton = new JButton("Mover");
        moverButton.addActionListener(e -> moverToCruz(mapaPanel));

        JButton usarButton = new JButton("Usar");
        usarButton.addActionListener(e -> {
            if (!toCruz.getInventario().isEmpty()) {
                toCruz.usarKitDeVida();
                mapaPanel.repaint();
                atualizarEstadoBotoes();
            } else {
                JOptionPane.showMessageDialog(this, "Inventario vazio! Nao ha kits para usar.");
            }
        });

        JButton atacarButton = new JButton("Atacar");
        atacarButton.addActionListener(e -> {
            Divisao divisaoAtual = toCruz.getPosicaoAtual();

            if (divisaoAtual != null && divisaoAtual.getInimigosPresentes().getSize() > 0) {
                combateService.resolverCombate(toCruz, divisaoAtual);
                mapaPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum inimigo para atacar nesta divisao.");
            }
        });

        JButton sairButton = new JButton("Sair");
        sairButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Simulacao encerrada. Obrigado por jogar!");
            System.exit(0);
        });

        controlePanel.add(moverButton);
        controlePanel.add(usarButton);
        controlePanel.add(atacarButton);
        controlePanel.add(sairButton);
    }

    /**
     * carregar a foto do to cruz
     */
    private void carregarImagens() {
        try {
            // Caminho para a imagem do Tó Cruz
            toCruzImage = new ImageIcon(getClass().getResource("/images/to_cruz.jpg")).getImage();
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem do Tó Cruz: " + e.getMessage());
        }
    }

    /**
     * criar divisoes no mapa
     */
    private void gerarPosicoesDivisoes() {
        // Gerar posições com maior espaçamento
        int xBase = 100, yBase = 100, offsetX = 150, offsetY = 100;

        for (int i = 0; i < divisoes.getSize(); i++) {
            int x = xBase + (i % 5) * offsetX; // Maior espaçamento horizontal
            int y = yBase + (i / 5) * offsetY; // Maior espaçamento vertical
            posicoesDivisoes.add(new Point(x, y));
        }
    }

    /**
     * Alterar a posicao atual, tanto na logica como no UI, do To Cruz
     * @param mapaPanel
     */
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

    /**
     * obter a Divisao pelo o seu nome
     * @param nome nome da divisao
     * @return a Divisao
     */
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
            Graphics2D g2 = (Graphics2D) g;

            // Configura suavização para melhorar o visual
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Desenhar conexões apenas para a divisão atual
            g2.setColor(Color.LIGHT_GRAY);
            if (toCruz.getPosicaoAtual() != null && toCruzImage != null) {
                Divisao posicaoAtual = toCruz.getPosicaoAtual();
                for (Ligacao ligacao : ligacoes) {
                    if (ligacao.conecta(posicaoAtual, ligacao.getOutraDivisao(posicaoAtual))) {
                        Point pos1 = posicoesDivisoes.getElementAt(divisoes.indexOf(ligacao.getDivisao1()));
                        Point pos2 = posicoesDivisoes.getElementAt(divisoes.indexOf(ligacao.getDivisao2()));

                        boolean intersecta = verificaInterseccao(pos1, pos2);

                        if (intersecta) {
                            desenharLigacaoCurva(g2, pos1, pos2);
                        } else {
                            g2.drawLine(pos1.x, pos1.y, pos2.x, pos2.y);
                        }
                    }
                }
            }

            // Desenhar divisões
            for (int i = 0; i < divisoes.getSize(); i++) {
                Divisao divisao = divisoes.getElementAt(i);
                Point pos = posicoesDivisoes.getElementAt(i);

                // Verificar se a divisão é entrada/saída
                if (isEntradaSaida(divisao)) {
                    g2.setColor(Color.GREEN); // Entrada/saída: verde
                } else {
                    g2.setColor(Color.BLUE); // Normal: azul
                }

                g2.fillOval(pos.x - 10, pos.y - 10, 20, 20);
                g2.setColor(Color.BLACK);
                g2.drawString(divisao.getNomeDivisao(), pos.x - 30, pos.y - 15);
            }

            // Desenhar Tó Cruz
            if (toCruz.getPosicaoAtual() != null && toCruzImage != null) {
                Point toCruzPos = posicoesDivisoes.getElementAt(divisoes.indexOf(toCruz.getPosicaoAtual()));
                g2.drawImage(toCruzImage, toCruzPos.x - 15, toCruzPos.y - 5, 30, 30, this);

                // Assinalar inimigos na divisão atual
                Divisao divisaoAtual = toCruz.getPosicaoAtual();
                if (divisaoAtual != null) {
                    LinkedList<Inimigo> inimigos = divisaoAtual.getInimigosPresentes();
                    int offsetY = 40; // Deslocamento vertical para evitar sobreposição
                    for (int i = 0; i < inimigos.getSize(); i++) {
                        Inimigo inimigo = inimigos.getElementAt(i);
                        g2.setColor(Color.RED);
                        g2.drawString("Inimigo: " + inimigo.getPoder() + " (poder)", toCruzPos.x - 30, toCruzPos.y + offsetY);
                        offsetY += 15; // Incrementa para cada inimigo
                    }

                    // Assinalar itens na divisão atual
                    LinkedList<Item> itens = divisaoAtual.getItensPresentes();
                    offsetY += 10; // Espaço entre inimigos e itens
                    for (int i = 0; i < itens.getSize(); i++) {
                        Item item = itens.getElementAt(i);
                        g2.setColor(Color.ORANGE);
                        g2.drawString("Item: " + item.getTipo() + " " + item.getPontos() + "(pontos)", toCruzPos.x - 30, toCruzPos.y + offsetY);
                        offsetY += 15; // Incrementa para cada item
                    }

                    // Assinalar alvos na divisao atual
                    if (mapa.getAlvo() != null && mapa.getAlvo().getDivisao().equals(divisaoAtual)) {
                        offsetY += 10; // Espaço entre itens e alvos
                        g2.setColor(Color.MAGENTA);
                        g2.drawString("Alvo: " + mapa.getAlvo().getTipo(), toCruzPos.x - 30, toCruzPos.y + offsetY);
                    }
                }
            }

            // Adicionar legenda
            desenharLegenda(g2);

            // Exibir informações no canto superior esquerdo
            g2.setColor(Color.BLACK);
            g2.drawString("vida: " + toCruz.getVida(), 10, 20);

            int qtdKits = 0;
            int qtdColetes = 0;

            // Itera sobre os elementos da stack
            for (int i = 0; i < toCruz.getInventario().size(); i++) {
                Item item = toCruz.getInventario().peek(); // Obtem o item no topo sem remover
                toCruz.getInventario().pop(); // Remove o item temporariamente
                if (item.getTipo().equalsIgnoreCase("kit de vida")) {
                    qtdKits++;
                } else if (item.getTipo().equalsIgnoreCase("colete")) {
                    qtdColetes++;
                }
                toCruz.getInventario().push(item); // Reinsere o item no inventário
            }

            g2.drawString("kits: " + qtdKits + "x", 10, 40);
            g2.drawString("coletes: " + qtdColetes + "x", 10, 60);
        }

        /**
         * Verifica se uma divisão é do tipo entrada/saída.
         *
         * @param divisao A divisão a verificar.
         * @return true se for entrada/saída, false caso contrário.
         */
        private boolean isEntradaSaida(Divisao divisao) {
            for (Divisao entradaSaida : entradasSaidas) {
                if (divisao.equals(entradaSaida)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Desenha uma legenda na interface gráfica.
         *
         * @param g2 Graphics2D para desenhar.
         */
        private void desenharLegenda(Graphics2D g2) {
            int x = getWidth() - 150; // Posição X da legenda
            int y = getHeight() - 50; // Posição Y da legenda

            g2.setColor(Color.BLUE);
            g2.fillOval(x, y + 20, 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawString("Divisao Normal", x + 15, y + 30);

            g2.setColor(Color.GREEN);
            g2.fillOval(x, y + 40, 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawString("Entrada/Saida", x + 15, y + 50);
        }

        /**
         * Verifica se a linha entre dois pontos intersecta outras divisões.
         *
         * @param pos1 Ponto inicial da linha.
         * @param pos2 Ponto final da linha.
         * @return true se intersecta, false caso contrário.
         */
        private boolean verificaInterseccao(Point pos1, Point pos2) {
            for (int i = 0; i < posicoesDivisoes.getSize(); i++) {
                Point divisao = posicoesDivisoes.getElementAt(i);
                Rectangle divisaoArea = new Rectangle(divisao.x - 10, divisao.y - 10, 20, 20);
                if (divisaoArea.intersectsLine(pos1.x, pos1.y, pos2.x, pos2.y)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Desenha uma ligação curva entre dois pontos.
         *
         * @param g2  Graphics2D para desenhar.
         * @param pos1 Ponto inicial.
         * @param pos2 Ponto final.
         */
        private void desenharLigacaoCurva(Graphics2D g2, Point pos1, Point pos2) {
            int controleX = (pos1.x + pos2.x) / 2;
            int controleY = Math.min(pos1.y, pos2.y) - 50; // Eleva o ponto de controle para a curva
            QuadCurve2D curva = new QuadCurve2D.Float(pos1.x, pos1.y, controleX, controleY, pos2.x, pos2.y);
            g2.draw(curva);
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
            SimulacaoManualGUI gui = new SimulacaoManualGUI(mapa.getDivisoes(), mapa.getEntradasSaidas(), mapa.getLigacoes(), mapa, toCruz);
            gui.setVisible(true);
        });
    }
}

