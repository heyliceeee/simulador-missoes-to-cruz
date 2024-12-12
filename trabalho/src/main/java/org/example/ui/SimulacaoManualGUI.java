package org.example.ui;

import org.example.Main;
import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;
import org.example.api.implementation.interfaces.*;
import org.example.api.implementation.models.*;
import org.example.api.implementation.services.CombateServiceImpl;
import org.example.api.implementation.utils.ImportJsonImpl;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;

import static org.example.api.implementation.simulation.SimulacaoAutomaticaImpl.*;

public class SimulacaoManualGUI extends JFrame {

    private ArrayUnorderedList<IDivisao> divisoes;
    private ArrayUnorderedList<IDivisao> entradasSaidas;
    private ArrayUnorderedList<Ligacao> ligacoes;
    private IMapa mapa;
    private ToCruz toCruz;
    private ArrayUnorderedList<Point> posicoesDivisoes;
    private Image toCruzImage;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private ICombateService combateService;
    private JButton moverButton;
    private JButton usarButton;
    private JButton atacarButton;
    private JButton resgatarButton;
    private JButton apanharButton;



    public SimulacaoManualGUI(ArrayUnorderedList<IDivisao> divisoes, ArrayUnorderedList<IDivisao> entradasSaidas,
            ArrayUnorderedList<Ligacao> ligacoes, IMapa mapa, ToCruz toCruz) {
        this.divisoes = divisoes;
        this.mapa = mapa;
        this.ligacoes = ligacoes;
        this.entradasSaidas = entradasSaidas;
        this.toCruz = toCruz;
        this.posicoesDivisoes = new ArrayUnorderedList<>();
        this.combateService = new CombateServiceImpl();

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
        atualizarEstadoBotoes();
        interagirComAlvo(toCruz.getPosicaoAtual());
    }



    /**
     * Interage com o alvo se estiver na mesma divisao.
     * Se houver inimigos, avisa o jogador que deve elimina-los antes.
     *
     * @param divisao A divisao onde o To Cruz esta.
     */
    private void interagirComAlvo(IDivisao divisao) {
        if (mapa.getAlvo() != null && mapa.getAlvo().getDivisao().equals(divisao)) {
            if (divisao.getInimigosPresentes().size() > 0) {
                System.out.println("O alvo esta nesta sala, mas ha inimigos! Elimine-os primeiro.");
            } else {
                System.out.println("O alvo foi resgatado com sucesso!");
                mapa.removerAlvo();
                toCruz.setAlvoConcluido(true);
            }
        }
    }

    /**
     * criar botoes de acao
     * 
     * @param controlePanel
     * @param mapaPanel     mapa do edificio
     */
    private void inicializarControlos(JPanel controlePanel, MapaPanel mapaPanel) {
        moverButton = new JButton("Mover");
        moverButton.addActionListener(e -> moverToCruz(mapaPanel));

        usarButton = new JButton("Mochila");
        usarButton.addActionListener(e -> {
            if (!toCruz.getInventario().isEmpty()) {
                toCruz.usarKitDeVida();
                mapaPanel.repaint();
                atualizarEstadoBotoes();
            } else {
                JOptionPane.showMessageDialog(this, "Inventario vazio! Nao ha kits para usar.");
            }
        });

        atacarButton = new JButton("Atacar");
        atacarButton.addActionListener(e -> {
            IDivisao divisaoAtual = toCruz.getPosicaoAtual();

            if (divisaoAtual != null && divisaoAtual.getInimigosPresentes().size() > 0) {
                try {
                    combateService.resolverCombate(toCruz, divisaoAtual);

                    if(toCruz.getVida() <= 0){
                        JOptionPane.showMessageDialog(this, "Missao fracassada. To Cruz foi derrotado.");
                        System.exit(0);
                    }

                } catch (ElementNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                mapaPanel.repaint();
                atualizarEstadoBotoes();
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum inimigo para atacar nesta divisao.");
            }
        });

        resgatarButton = new JButton("Resgatar");
        resgatarButton.addActionListener(e -> {
            IDivisao divisaoAtual = toCruz.getPosicaoAtual();
            if (mapa.getAlvo() != null && mapa.getAlvo().getDivisao().equals(divisaoAtual)) {
                JOptionPane.showMessageDialog(this, "Alvo resgatado com sucesso!");
                mapa.removerAlvo();
                toCruz.setAlvoConcluido(true);
                mapaPanel.repaint();
                atualizarEstadoBotoes();
            }
        });

        apanharButton = new JButton("Apanhar");
        apanharButton.addActionListener(e -> {
            IDivisao divisaoAtual = toCruz.getPosicaoAtual();
            if (divisaoAtual != null) {
                ArrayUnorderedList<IItem> itens = divisaoAtual.getItensPresentes();

                if(toCruz.getInventario().size() >= 5){
                    JOptionPane.showMessageDialog(this, "Mochila cheia! Nao e possivel carregar mais kits de vida.");
                }

                while (itens.size() > 0) {
                    IItem item = itens.getElementAt(0);
                    toCruz.adicionarAoInventario(item);
                    try {
                        divisaoAtual.removerItem(item);
                    } catch (ElementNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                JOptionPane.showMessageDialog(this, "Itens apanhados com sucesso!");
                mapaPanel.repaint();
                atualizarEstadoBotoes();
            }
        });

        JButton sairButton = new JButton("Sair");
        sairButton.addActionListener(e -> {
            IDivisao divisaoAtual = toCruz.getPosicaoAtual();

            if (toCruz.getVida() > 0 && divisaoAtual.isEntradaSaida() && toCruz.isAlvoConcluido()) {
                //atualizarEstadoBotoes();
                JOptionPane.showMessageDialog(this, "Missao concluida com sucesso!");
                System.exit(0);
            }
            else if (!divisaoAtual.isEntradaSaida()) {
                JOptionPane.showMessageDialog(this, "Nao e uma divisao de saida");
            }
            else if(divisaoAtual.isEntradaSaida() && !toCruz.isAlvoConcluido()){
                JOptionPane.showMessageDialog(this, "Missao fracassada. To Cruz abandonou o alvo.");
                System.exit(0);
            }
        });

        controlePanel.add(moverButton);
        controlePanel.add(usarButton);
        controlePanel.add(atacarButton);
        controlePanel.add(resgatarButton);
        controlePanel.add(apanharButton);

        atualizarEstadoBotoes();
    }

    /**
     * ativar ou desativar botao de acordo com um determinado cenario de jogo
     */
    private void atualizarEstadoBotoes() {
        IDivisao divisaoAtual = toCruz.getPosicaoAtual();

        if (divisaoAtual != null) {
            boolean temInimigos = divisaoAtual.getInimigosPresentes().size() > 0;
            boolean temItens = divisaoAtual.getItensPresentes().size() > 0;
            boolean temAlvo = mapa.getAlvo() != null && mapa.getAlvo().getDivisao().equals(divisaoAtual);
            //boolean terminouMissao = divisaoAtual.isEntradaSaida() && toCruz.getVida() > 0 && toCruz.isAlvoConcluido();

            // Botao "Mover" e desativado se houver inimigos
            moverButton.setEnabled(!temInimigos);

            // Botao "Atacar" e desativado se nao houver inimigos
            atacarButton.setEnabled(temInimigos);

            // Botao "Resgatar" e ativado se nao houver inimigos e houver alvo
            resgatarButton.setEnabled(!temInimigos && temAlvo);

            // Botao "Apanhar" e ativado se houver itens na divisao
            apanharButton.setEnabled(temItens);

        } else {
            // Caso nao haja divisao atual, desativa os botoes
            moverButton.setEnabled(false);
            atacarButton.setEnabled(false);
            resgatarButton.setEnabled(false);
            apanharButton.setEnabled(false);
        }
    }

    /**
     * carregar a foto do to cruz
     */
    private void carregarImagens() {
        try {
            // Caminho para a imagem do To Cruz
            toCruzImage = new ImageIcon(getClass().getResource("/images/to_cruz.jpg")).getImage();
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem do To Cruz: " + e.getMessage());
        }
    }

    /**
     * criar divisoes no mapa
     */
    private void gerarPosicoesDivisoes() {
        // Gerar posicoes com maior espacamento
        int xBase = 100, yBase = 100, offsetX = 150, offsetY = 100;

        for (int i = 0; i < divisoes.size(); i++) {
            int x = xBase + (i % 5) * offsetX; // Maior espacamento horizontal
            int y = yBase + (i / 5) * offsetY; // Maior espacamento vertical
            posicoesDivisoes.addToRear(new Point(x, y));
        }
    }

    /**
     * Alterar a posicao atual, tanto na logica como no UI, do To Cruz
     * 
     * @param mapaPanel
     */
    private void moverToCruz(MapaPanel mapaPanel) {
        String destino = JOptionPane.showInputDialog(this, "Digite o nome da divisao:");
        IDivisao novaDivisao = getDivisaoPorNome(destino);

        if (novaDivisao != null && podeMover(toCruz.getPosicaoAtual(), novaDivisao)) {
            toCruz.moverPara(novaDivisao);
            mapaPanel.repaint(); // Atualizar o desenho
            atualizarEstadoBotoes(); // Atualizar estado dos botoes
        } else {
            JOptionPane.showMessageDialog(this,
                    "Movimento invalido (nao existe essa divisao ou nao ha ligacao direta)!");
        }
    }

    /**
     * obter a Divisao pelo o seu nome
     * 
     * @param nome nome da divisao
     * @return a Divisao
     */
    private IDivisao getDivisaoPorNome(String nome) {
        for (IDivisao divisao : divisoes) {
            if (divisao.getNomeDivisao().equals(nome)) {
                return divisao;
            }
        }
        return null;
    }

    private boolean podeMover(IDivisao origem, IDivisao destino) {
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

            // Configura suavizacao para melhorar o visual
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Desenhar conexoes apenas para a divisao atual
            g2.setColor(Color.LIGHT_GRAY);
            if (toCruz.getPosicaoAtual() != null && toCruzImage != null) {
                IDivisao posicaoAtual = toCruz.getPosicaoAtual();
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

            // Desenhar divisoes
            for (int i = 0; i < divisoes.size(); i++) {
                IDivisao divisao = divisoes.getElementAt(i);
                Point pos = posicoesDivisoes.getElementAt(i);

                // Verificar se a divisao e entrada/saida
                if (divisao.isEntradaSaida()) {
                    g2.setColor(Color.GREEN); // Entrada/saida: verde
                } else {
                    g2.setColor(Color.BLUE); // Normal: azul
                }

                g2.fillOval(pos.x - 10, pos.y - 10, 20, 20);
                g2.setColor(Color.BLACK);
                g2.drawString(divisao.getNomeDivisao(), pos.x - 30, pos.y - 15);
            }

            // Desenhar To Cruz
            if (toCruz.getPosicaoAtual() != null && toCruzImage != null) {
                Point toCruzPos = posicoesDivisoes.getElementAt(divisoes.indexOf(toCruz.getPosicaoAtual()));
                g2.drawImage(toCruzImage, toCruzPos.x - 15, toCruzPos.y - 5, 30, 30, this);

                // Assinalar inimigos na divisao atual
                IDivisao divisaoAtual = toCruz.getPosicaoAtual();
                if (divisaoAtual != null) {
                    ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
                    int offsetY = 40; // Deslocamento vertical para evitar sobreposicao
                    g2.setColor(Color.RED);
                    g2.drawString(inimigos.size()+" " + skull , toCruzPos.x - 20, toCruzPos.y + offsetY);

                    // Assinalar itens na divisao atual
                    ArrayUnorderedList<IItem> itens = divisaoAtual.getItensPresentes();
                    offsetY += 10; // Espaco entre inimigos e itens
                    int countKitVida = 0;
                    int countColete = 0;

                    for (int i = 0; i < itens.size(); i++) {
                        if(itens.getElementAt(i).getTipo().equalsIgnoreCase("kit de vida")){
                            countKitVida++;
                        }
                        else if(itens.getElementAt(i).getTipo().equalsIgnoreCase("colete")){
                            countColete++;
                        }
                    }

                    g2.setColor(Color.darkGray);
                    g2.drawString(countKitVida + "x" + pill, toCruzPos.x - 30, toCruzPos.y + offsetY + 5);

                    g2.setColor(Color.darkGray);
                    g2.drawString(countColete + "x" + vest, toCruzPos.x, toCruzPos.y + offsetY + 5);

                    // Assinalar alvos na divisao atual
                    if (mapa.getAlvo() != null && mapa.getAlvo().getDivisao().equals(divisaoAtual)) {
                        offsetY += 10; // Espaco entre itens e alvos
                        g2.setColor(Color.MAGENTA);
                        g2.drawString(target , toCruzPos.x - 30, toCruzPos.y + offsetY + 10);
                    }
                }
            }

            // Adicionar legenda
            desenharLegenda(g2);

            // Exibir informacoes no canto superior esquerdo
            g2.setColor(Color.RED);
            g2.drawString( toCruz.getVida() + " " + life, 10, 20);

            int qtdKits = 0;
            int qtdColetes = 0;

            // Itera sobre os elementos da stack
            for (int i = 0; i < toCruz.getInventario().size(); i++) {
                IItem item = toCruz.getInventario().peek(); // Obtem o item no topo sem remover
                toCruz.getInventario().pop(); // Remove o item temporariamente
                if (item.getTipo().equalsIgnoreCase("kit de vida")) {
                    qtdKits++;
                } else if (item.getTipo().equalsIgnoreCase("colete")) {
                    qtdColetes++;
                }
                toCruz.getInventario().push(item); // Reinsere o item no inventario
            }

            g2.setColor(Color.darkGray);
            g2.drawString(pill + " " + qtdKits + "x", 10, 40);

            g2.setColor(Color.darkGray);
            g2.drawString(vest + "  " + qtdColetes + "x", 10, 60);
        }

        /**
         * Desenha uma legenda na interface grafica.
         *
         * @param g2 Graphics2D para desenhar.
         */
        private void desenharLegenda(Graphics2D g2) {
            int x = getWidth() - 150; // Posicao X da legenda
            int y = getHeight() - 50; // Posicao Y da legenda

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
         * Verifica se a linha entre dois pontos intersecta outras divisoes.
         *
         * @param pos1 Ponto inicial da linha.
         * @param pos2 Ponto final da linha.
         * @return true se intersecta, false caso contrario.
         */
        private boolean verificaInterseccao(Point pos1, Point pos2) {
            for (int i = 0; i < posicoesDivisoes.size(); i++) {
                Point divisao = posicoesDivisoes.getElementAt(i);
                Rectangle divisaoArea = new Rectangle(divisao.x - 10, divisao.y - 10, 20, 20);
                if (divisaoArea.intersectsLine(pos1.x, pos1.y, pos2.x, pos2.y)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Desenha uma ligacao curva entre dois pontos.
         *
         * @param g2   Graphics2D para desenhar.
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
        IMapa mapa = new MapaImpl();
        ImportJsonImpl jsonUtils = new ImportJsonImpl(mapa);
        String caminhoJson = "mapa_v1.json";

        // mapa carregado apartir do JSON
        try {
            jsonUtils.carregarMapa(caminhoJson);
            logger.info("Mapa carregado com sucesso e pronto para uso!");

            // Verificar se o alvo foi carregado corretamente
            IAlvo alvo = mapa.getAlvo();
            if (alvo != null) {
                logger.info("Alvo carregado do JSON: Divisao - {}, Tipo - {}", alvo.getDivisao().getNomeDivisao(),
                        alvo.getTipo());
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
            logger.error("Erro de referencia de divisao: {}", e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Erro inesperado: {}", e.getMessage());
            e.printStackTrace();
            return;
        }

        if (mapa.getDivisoes().isEmpty()) {
            logger.error("Mapa nao possui divisoes carregadas. Encerrando o programa.");
            return;
        }

        // criar o to cruz e definir a sua posicao inicial
        ToCruz toCruz = new ToCruz("To Cruz", 100); // Nome e vida inicial
        IDivisao divisaoInicial = mapa.getDivisoes().getElementAt(0); // Primeira divisao
        toCruz.moverPara(divisaoInicial);

        SwingUtilities.invokeLater(() -> {
            SimulacaoManualGUI gui = new SimulacaoManualGUI(mapa.getDivisoes(), mapa.getEntradasSaidas(),
                    mapa.getLigacoes(), mapa, toCruz);
            gui.setVisible(true);
        });
    }
}
