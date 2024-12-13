package org.example.ui;

import org.example.Main;
import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;
import org.example.api.implementation.interfaces.*;
import org.example.api.implementation.models.*;
import org.example.api.implementation.services.CombateServiceImpl;
import org.example.api.implementation.simulation.SimulacaoManualImpl;
import org.example.api.implementation.utils.ExportarResultados;
import org.example.api.implementation.utils.ImportJsonImpl;
import org.example.collections.implementation.ArrayOrderedList;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;

import static org.example.Main.*;
import static org.example.api.implementation.simulation.SimulacaoAutomaticaImpl.*;

public class SimulacaoManualGUI extends JFrame {

    //#region Constantes e Atributos

    private ArrayUnorderedList<IDivisao> caminhoParaKit;
    private ArrayUnorderedList<IDivisao> divisoes;
    private ArrayUnorderedList<Ligacao> ligacoes;
    private ArrayUnorderedList<IDivisao> caminhoPercorridoToCruz;
    private IMapa mapa;
    private ToCruz toCruz;
    private ArrayUnorderedList<Point> posicoesDivisoes;
    private Image toCruzImage;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private ICombateService combateService;
    private JButton iniciarButton;
    private JButton moverButton;
    private JButton usarButton;
    private JButton atacarButton;
    private JButton resgatarButton;
    private JButton apanharButton;
    private JButton sairButton;
    public static IMissao missao;
    private ExportarResultados exportador;

    //#endregion

    /**
     * Construtor e Inicializacao
     *
     * @param divisoes
     * @param entradasSaidas
     * @param ligacoes
     * @param mapa
     * @param toCruz
     */
    public SimulacaoManualGUI(ArrayUnorderedList<IDivisao> divisoes, ArrayUnorderedList<IDivisao> entradasSaidas,
            ArrayUnorderedList<Ligacao> ligacoes, IMapa mapa, ToCruz toCruz) {
        this.divisoes = divisoes;
        this.mapa = mapa;
        this.ligacoes = ligacoes;
        this.toCruz = toCruz;
        this.posicoesDivisoes = new ArrayUnorderedList<>();
        this.combateService = new CombateServiceImpl();
        this.exportador = new ExportarResultados();
        this.caminhoPercorridoToCruz = new ArrayUnorderedList<>();
        this.caminhoParaKit = new ArrayUnorderedList<>();

        configurarJanela();
        carregarImagens();
        gerarPosicoesDivisoes();
        inicializarComponentes();
    }



    //#region Funcoes de Controlo do Jogo

    private IItem encontrarKitMaisProximo(ArrayUnorderedList<IItem> kits) throws ElementNotFoundException {
        IItem kitMaisProximo = null;
        int menorDistancia = Integer.MAX_VALUE;

        for (int i = 0; i < kits.size(); i++) {
            IItem kit = kits.getElementAt(i);
            int distancia = mapa.calcularMelhorCaminho(toCruz.getPosicaoAtual(), kit.getDivisao()).size();
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                kitMaisProximo = kit;
            }
        }
        return kitMaisProximo;
    }

    /**
     * Alterar a posicao atual, tanto na logica como no UI, do To Cruz
     *
     * @param mapaPanel
     */
    private void moverToCruz(MapaPanel mapaPanel) throws ElementNotFoundException {
        String destino = JOptionPane.showInputDialog(this, "Introduza o nome da divisao:");
        IDivisao novaDivisao = getDivisaoPorNome(destino);

        if (novaDivisao != null && podeMover(toCruz.getPosicaoAtual(), novaDivisao)) {
            toCruz.moverPara(novaDivisao);
            caminhoPercorridoToCruz.addToRear(novaDivisao);
            mapa.moverInimigos(toCruz, combateService); //mover inimigo antes do To Cruz agir
            mapaPanel.repaint(); // Atualizar o desenho
            atualizarEstadoBotoes(); // Atualizar estado dos botoes
        } else {
            JOptionPane.showMessageDialog(this,
                    "Movimento invalido (nao existe essa divisao ou nao ha ligacao direta)!");
        }
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
     * ativar ou desativar botao de acordo com um determinado cenario de jogo
     */
    private void atualizarEstadoBotoes() {
        IDivisao divisaoAtual = toCruz.getPosicaoAtual();

        if (divisaoAtual != null) {
            boolean temInimigos = divisaoAtual.getInimigosPresentes().size() > 0;
            boolean temItens = divisaoAtual.getItensPresentes().size() > 0;
            boolean temAlvo = mapa.getAlvo() != null && mapa.getAlvo().getDivisao().equals(divisaoAtual);

            // Botao "iniciar"
            iniciarButton.setEnabled(false);

            // Botao "mochila"
            usarButton.setEnabled(true);

            // Botao "Mover" e desativado se houver inimigos
            moverButton.setEnabled(!temInimigos);

            // Botao "Atacar" e desativado se nao houver inimigos
            atacarButton.setEnabled(temInimigos);

            // Botao "Resgatar" e ativado se nao houver inimigos e houver alvo
            resgatarButton.setEnabled(!temInimigos && temAlvo);

            // Botao "Apanhar" e ativado se houver itens na divisao
            apanharButton.setEnabled(temItens);

            // Botao "Sair"
            sairButton.setEnabled(true);
        } else {
            // Caso nao haja divisao atual, desativa os botoes
            iniciarButton.setEnabled(true);
            usarButton.setEnabled(false);
            moverButton.setEnabled(false);
            atacarButton.setEnabled(false);
            resgatarButton.setEnabled(false);
            apanharButton.setEnabled(false);
            sairButton.setEnabled(false);
        }
    }

    /**
     * Alterar a posicao atual, tanto na logica como no UI, do To Cruz
     *
     * @param mapaPanel
     */
    private void moverEntradaSaidaToCruz(MapaPanel mapaPanel) {
        String destino = JOptionPane.showInputDialog(this, "Introduza o nome da divisao do tipo entrada/saida:");
        IDivisao novaDivisao = getDivisaoPorNome(destino);

        if (novaDivisao != null && novaDivisao.isEntradaSaida()) {
            toCruz.moverPara(novaDivisao);
            caminhoPercorridoToCruz.addToRear(novaDivisao);
            mapaPanel.repaint(); // Atualizar o desenho
            atualizarEstadoBotoes(); // Atualizar estado dos botoes
        } else {
            JOptionPane.showMessageDialog(this,
                    "Movimento invalido (nao existe essa divisao ou nao e um tipo entrada/saida)!");
        }
    }

    /**
     * Verifica se existe uma ligacao direta entre a divisao origem e divisao
     * destino
     * 
     * @param origem
     * @param destino
     * @return
     */
    private boolean podeMover(IDivisao origem, IDivisao destino) {
        for (Ligacao ligacao : ligacoes) {
            if (ligacao.conecta(origem, destino)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Combate direto entre To Cruz e inimigos na divisao atual
     *
     * @param mapaPanel
     */
    private void realizarAtaque(MapaPanel mapaPanel) {
        IDivisao divisaoAtual = toCruz.getPosicaoAtual();

        if (divisaoAtual != null && divisaoAtual.getInimigosPresentes().size() > 0) {
            try {
                combateService.resolverCombate(toCruz, divisaoAtual);

                if (toCruz.getVida() <= 0) {
                    JOptionPane.showMessageDialog(this, "Missao fracassada. To Cruz foi derrotado.");

                    IResultadoSimulacao resultado = new ResultadoSimulacaoImpl("MANUAL-002",
                            caminhoPercorridoToCruz.first().getNomeDivisao(),
                            caminhoPercorridoToCruz.last().getNomeDivisao(), "FALHA", toCruz.getVida(),
                            filtrarListaDivisao(caminhoPercorridoToCruz), filtrarLista(mapa.getEntradasSaidasNomes()),
                            missao.getCodMissao(), missao.getVersao());

                    // Exportar o relatorio combinado
                    exportador.exportarRelatorioSimulacoes(resultado, resultado, mapa, "relatorio_simulacoes.json");

                    logger.info("Relatorio de simulacoes exportado com sucesso.");
                    logger.info("Programa finalizado com sucesso.");

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
    }

    /**
     * To Cruz resgata o alvo
     *
     * @param mapaPanel
     */
    private void resgatar(MapaPanel mapaPanel) {
        IDivisao divisaoAtual = toCruz.getPosicaoAtual();

        if (mapa.getAlvo() != null && mapa.getAlvo().getDivisao().equals(divisaoAtual)) {
            JOptionPane.showMessageDialog(this, "Alvo resgatado com sucesso!");
            mapa.removerAlvo();
            toCruz.setAlvoConcluido(true);
            mapaPanel.repaint();
            atualizarEstadoBotoes();
        }
    }

    /**
     * To Cruz apanha itens como kit de vida e colete
     *
     * @param mapaPanel
     */
    private void apanharItens(MapaPanel mapaPanel) {
        IDivisao divisaoAtual = toCruz.getPosicaoAtual();

        if (divisaoAtual != null) {
            ArrayUnorderedList<IItem> itens = divisaoAtual.getItensPresentes();

            if (toCruz.getInventario().size() >= 5) {
                JOptionPane.showMessageDialog(this, "Mochila cheia! Nao e possivel carregar mais kits de vida.");
                return;
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
    }

    /**
     * To Cruz decide finalizar a missao
     */
    private void finalizarMissao() {
        IDivisao divisaoAtual = toCruz.getPosicaoAtual();

        if (toCruz.getVida() > 0 && divisaoAtual.isEntradaSaida() && toCruz.isAlvoConcluido()) {
            atualizarEstadoBotoes();
            JOptionPane.showMessageDialog(this, "Missao concluida com sucesso!");

            IResultadoSimulacao resultado = new ResultadoSimulacaoImpl("MANUAL-002",
                    caminhoPercorridoToCruz.first().getNomeDivisao(), caminhoPercorridoToCruz.last().getNomeDivisao(),
                    "SUCESSO", toCruz.getVida(), filtrarListaDivisao(caminhoPercorridoToCruz),
                    filtrarLista(mapa.getEntradasSaidasNomes()), missao.getCodMissao(), missao.getVersao());

            // Exportar o relatorio combinado
            exportador.exportarRelatorioSimulacoes(resultado, resultado, mapa, "relatorio_simulacoes.json");

            logger.info("Relatorio de simulacoes exportado com sucesso.");
            logger.info("Programa finalizado com sucesso.");

            System.exit(0);
        } else if (!divisaoAtual.isEntradaSaida()) {
            JOptionPane.showMessageDialog(this, "Nao e uma divisao de saida");
        } else if (divisaoAtual.isEntradaSaida() && !toCruz.isAlvoConcluido()) {
            JOptionPane.showMessageDialog(this, "Missao fracassada. To Cruz abandonou o alvo.");

            IResultadoSimulacao resultado = new ResultadoSimulacaoImpl("MANUAL-002",
                    caminhoPercorridoToCruz.first().getNomeDivisao(), caminhoPercorridoToCruz.last().getNomeDivisao(),
                    "FALHA", toCruz.getVida(), filtrarListaDivisao(caminhoPercorridoToCruz),
                    filtrarLista(mapa.getEntradasSaidasNomes()), missao.getCodMissao(), missao.getVersao());

            // Exportar o relatorio combinado
            exportador.exportarRelatorioSimulacoes(resultado, resultado, mapa, "relatorio_simulacoes.json");

            logger.info("Relatorio de simulacoes exportado com sucesso.");
            logger.info("Programa finalizado com sucesso.");

            System.exit(0);
        }
    }

    //#endregion

    //#region Funcoes da UI

    /**
     * Inicializar os componentes da janela
     */
    private void inicializarComponentes() {
        // Adicionar o painel de mapa
        MapaPanel mapaPanel = new MapaPanel();
        add(mapaPanel, BorderLayout.CENTER);

        // Adicionar controlos
        JPanel controlePanel = new JPanel();
        inicializarBotoes(controlePanel, mapaPanel);

        add(controlePanel, BorderLayout.SOUTH);
        atualizarEstadoBotoes();
        interagirComAlvo(toCruz.getPosicaoAtual());
    }

    /**
     * Configuracao da janela da simulacao manual
     */
    private void configurarJanela() {
        setTitle("Simulacao - To Cruz");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * criar botoes de acao
     * 
     * @param controlePanel
     * @param mapaPanel     mapa do edificio
     */
    private void inicializarBotoes(JPanel controlePanel, MapaPanel mapaPanel) {
        iniciarButton = new JButton("Iniciar");
        iniciarButton.addActionListener(e -> moverEntradaSaidaToCruz(mapaPanel));

        moverButton = new JButton("Mover");
        moverButton.addActionListener(e -> {
            try {
                moverToCruz(mapaPanel);
            } catch (ElementNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

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
        atacarButton.addActionListener(e -> realizarAtaque(mapaPanel));

        resgatarButton = new JButton("Resgatar");
        resgatarButton.addActionListener(e -> resgatar(mapaPanel));

        apanharButton = new JButton("Apanhar");
        apanharButton.addActionListener(e -> apanharItens(mapaPanel));

        sairButton = new JButton("Sair");
        sairButton.addActionListener(e -> finalizarMissao());

        controlePanel.add(iniciarButton);
        controlePanel.add(moverButton);
        controlePanel.add(usarButton);
        controlePanel.add(atacarButton);
        controlePanel.add(resgatarButton);
        controlePanel.add(apanharButton);
        controlePanel.add(sairButton);

        atualizarEstadoBotoes();
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

    //#endregion

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

               //mostrar o melhor caminho para o alvo
                ArrayUnorderedList<IDivisao> caminhoParaAlvo = mapa.calcularMelhorCaminho(posicaoAtual,
                        mapa.getAlvo().getDivisao());

                //mostrar o melhor caminho para o kit/colete mais proximo
                try {
                    caminhoParaKit = mapa.calcularMelhorCaminho(posicaoAtual,
                            encontrarKitMaisProximo(mapa.getItens()).getDivisao());
                } catch (ElementNotFoundException e) {
                    throw new RuntimeException(e);
                }

                //ver se ha ligacao da posicaoAtual para os objetivos

                for (Ligacao ligacao : ligacoes) {
                    if (ligacao.conecta(posicaoAtual, ligacao.getOutraDivisao(posicaoAtual))) {
                        Point pos1 = posicoesDivisoes.getElementAt(divisoes.indexOf(posicaoAtual));
                        //Point pos2 = posicoesDivisoes.getElementAt(divisoes.indexOf(caminhoParaAlvo.getElementAt(1)));
                        Point pos2 = posicoesDivisoes.getElementAt(divisoes.indexOf(ligacao.getOutraDivisao(posicaoAtual)));

                        boolean intersecta = verificaInterseccao(pos1, pos2);
                        g2.setColor(Color.red);

                        if (intersecta) {
                            desenharLigacaoCurva(g2, pos1, pos2);
                        } else {
                            g2.drawLine(pos1.x, pos1.y, pos2.x, pos2.y);
                        }

                //Point pos3 = posicoesDivisoes.getElementAt(divisoes.indexOf(posicaoAtual));
                //Point pos4 = posicoesDivisoes.getElementAt(divisoes.indexOf(caminhoParaKit.getElementAt(1)));

                //boolean intersecta1 = verificaInterseccao(pos3, pos4);
                //g2.setColor(Color.blue);

                //if (intersecta1) {
                //    desenharLigacaoCurva(g2, pos3, pos4);
                //} else {
                //    g2.drawLine(pos3.x, pos3.y, pos4.x, pos4.y);
                //}
                    }
                }
            }

            // Desenhar divisoes e assinalar informacoes
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

            // Assinalar informacoes da divisao atual e das divisoes adjacentes
            if (toCruz.getPosicaoAtual() != null && toCruzImage != null) {
                // Desenhar To Cruz na posicao atual
                Point toCruzPos = posicoesDivisoes.getElementAt(divisoes.indexOf(toCruz.getPosicaoAtual()));
                g2.drawImage(toCruzImage, toCruzPos.x - 15, toCruzPos.y - 5, 30, 30, this);

                // Obter divisao atual e divisoes adjacentes
                IDivisao divisaoAtual = toCruz.getPosicaoAtual();

                // Assinalar inimigos
                ArrayUnorderedList<IInimigo> inimigos = divisaoAtual.getInimigosPresentes();
                int offsetY = 40; // Deslocamento vertical inicial

                if (inimigos.size() > 0) {
                    g2.setColor(Color.RED);
                    g2.drawString(inimigos.size() + " " + skull, toCruzPos.x - 20, toCruzPos.y + offsetY);
                }

                // Assinalar itens
                ArrayUnorderedList<IItem> itens = divisaoAtual.getItensPresentes();
                int countKitVida = 0;
                int countColete = 0;

                for (IItem item : itens) {
                    if (item.getTipo().equalsIgnoreCase("kit de vida")) {
                        countKitVida++;
                    } else if (item.getTipo().equalsIgnoreCase("colete")) {
                        countColete++;
                    }
                }

                if (countKitVida > 0) {
                    g2.setColor(Color.darkGray);
                    g2.drawString(countKitVida + "x" + pill, toCruzPos.x - 30, toCruzPos.y + offsetY + 15);
                }

                if (countColete > 0) {
                    g2.setColor(Color.darkGray);
                    g2.drawString(countColete + "x" + vest, toCruzPos.x, toCruzPos.y + offsetY + 15);
                }

                // Assinalar alvos na divisao atual
                if (mapa.getAlvo() != null && mapa.getAlvo().getDivisao().equals(divisaoAtual)) {
                    offsetY += 10; // Espaco entre itens e alvos
                    g2.setColor(Color.MAGENTA);
                    g2.drawString(target, toCruzPos.x - 30, toCruzPos.y + offsetY + 10);
                }

                // Iterar sobre as divisoes com ligacao direta a divisao atual, para assinalar
                // itens, inimigos e alvos
                for (Ligacao ligacao : ligacoes) {
                    if (ligacao.conecta(divisaoAtual, ligacao.getOutraDivisao(divisaoAtual))) {
                        IDivisao divisaoConetadaAtual = ligacao.getDivisao1() == divisaoAtual ? ligacao.getDivisao2()
                                : ligacao.getDivisao1();

                        Point divisaoConetadaPos = posicoesDivisoes
                                .getElementAt(divisoes.indexOf(divisaoConetadaAtual));

                        // Assinalar inimigos
                        ArrayUnorderedList<IInimigo> inimigos1 = divisaoConetadaAtual.getInimigosPresentes();
                        offsetY = 40; // Deslocamento vertical inicial

                        if (inimigos1.size() > 0) {
                            g2.setColor(Color.RED);
                            g2.drawString(inimigos1.size() + " " + skull, divisaoConetadaPos.x - 20,
                                    divisaoConetadaPos.y + offsetY);
                        }

                        // Assinalar itens
                        ArrayUnorderedList<IItem> itens1 = divisaoConetadaAtual.getItensPresentes();
                        int countKitVida1 = 0;
                        int countColete1 = 0;

                        for (IItem item : itens1) {
                            if (item.getTipo().equalsIgnoreCase("kit de vida")) {
                                countKitVida1++;
                            } else if (item.getTipo().equalsIgnoreCase("colete")) {
                                countColete1++;
                            }
                        }

                        if (countKitVida1 > 0) {
                            g2.setColor(Color.darkGray);
                            g2.drawString(countKitVida1 + "x" + pill, divisaoConetadaPos.x - 30,
                                    divisaoConetadaPos.y + offsetY + 15);
                        }

                        if (countColete1 > 0) {
                            g2.setColor(Color.darkGray);
                            g2.drawString(countColete1 + "x" + vest, divisaoConetadaPos.x,
                                    divisaoConetadaPos.y + offsetY + 15);
                        }

                        // Assinalar alvos na divisao atual
                        if (mapa.getAlvo() != null && mapa.getAlvo().getDivisao().equals(divisaoConetadaAtual)) {
                            offsetY += 10; // Espaco entre itens e alvos
                            g2.setColor(Color.MAGENTA);
                            g2.drawString(target, divisaoConetadaPos.x - 30, divisaoConetadaPos.y + offsetY + 25);
                        }
                    }
                }
            }

            // Adicionar legenda
            desenharLegenda(g2);

            // Exibir informacoes no canto superior esquerdo
            g2.setColor(Color.RED);
            g2.drawString(toCruz.getVida() + " " + life, 10, 20);

            int qtdKits = 0;
            int qtdColetes = 0;

            // Itera sobre os elementos da stack
            for (int i = 0; i < toCruz.getInventario().size(); i++) {
                IItem item = toCruz.getInventario().peek(); // Obtem o item no topo sem remover
                toCruz.getInventario().pop(); // Remove o item temporariamente
                if (item.getTipo().equals("kit de vida")) {
                    qtdKits++;
                } else if (item.getTipo().equals("colete")) {
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
        IImportJson jsonUtils = new ImportJsonImpl(mapa);
        String caminhoJson = "mapa_v7.json";

        // mapa carregado apartir do JSON
        try {
            // jsonUtils.carregarMapa(caminhoJson);
            // logger.info("Mapa carregado com sucesso e pronto para uso!");

            missao = jsonUtils.carregarMissao(caminhoJson);
            logger.info("Missao carregada: {} - Versao {}", missao.getCodMissao(), missao.getVersao());

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
        // IDivisao divisaoInicial = mapa.getDivisoes().getElementAt(0); // Primeira
        // divisao
        // toCruz.moverPara(divisaoInicial);

        SwingUtilities.invokeLater(() -> {
            SimulacaoManualGUI gui = new SimulacaoManualGUI(mapa.getDivisoes(), mapa.getEntradasSaidas(),
                    mapa.getLigacoes(), mapa, toCruz);
            gui.setVisible(true);
        });
    }
}
