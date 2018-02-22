/*
  Code to handle the creation of the gamescreen and UI.

  Authors Team11:  Jack Geraghty - 16384181
                   Conor Beenham - 16350851
                   Alen Thomas   - 16333003
 */


package com.team11.cluedo.ui;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.board.Board;
import com.team11.cluedo.players.Players;
import com.team11.cluedo.suspects.Suspects;
import com.team11.cluedo.ui.panel.BackgroundPanel;
import com.team11.cluedo.weapons.Weapons;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.io.IOException;

public class GameScreen implements Screen {
    private JFrame frame;
    private BoardUI boardPanel;
    private PlayerCardLayout playerPanel;

    private JTextArea infoOutput, helpOutput;
    private String input;
    private JTextField commandInput;

    private Board gameBoard;
    private Suspects gameSuspects;
    private Weapons gameWeapons;
    private Players gamePlayers;
    private Assets gameAssets;

    private Resolution resolution;

    public GameScreen(Board gameBoard, Suspects gameSuspects, Weapons gameWeapons, Players gamePlayers, Assets gameAssets, Resolution resolution) throws IOException{
        this.gameBoard = gameBoard;
        this.gameSuspects = gameSuspects;
        this.gameWeapons = gameWeapons;
        this.gamePlayers = gamePlayers;
        this.gameAssets = gameAssets;
        this.resolution = resolution;
    }

    @Override
    public void createScreen(String name) {
        this.frame = new JFrame(name);
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void setupScreen(int state) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        ImageIcon backgroundTile = this.gameAssets.getBackgroundTile();
        Image backgroundImage = backgroundTile.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage, BackgroundPanel.TILED);

        this.playerPanel = setupPlayerPanel();
        contentPanel.add(playerPanel, BorderLayout.WEST);

        JTabbedPane infoPanel = setupInfoPanel1();
        contentPanel.add(infoPanel, BorderLayout.EAST);

        this.boardPanel = new BoardUI(this.gameSuspects, this.gameWeapons, new BoardComponent());
        contentPanel.add(boardPanel, BorderLayout.CENTER);

        backgroundPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(backgroundPanel, BorderLayout.CENTER);
        this.frame.getContentPane().add(mainPanel);

        this.frame.pack();
    }

    @Override
    public void displayScreen() {
        this.frame.setLocation(resolution.getScreenSize().width/2 - frame.getSize().width/2, resolution.getScreenSize().height/2 - (frame.getSize().height/2));
        this.frame.setVisible(true);
    }

    @Override
    public void closeScreen() {
        this.frame.removeAll();
        this.frame.dispose();
    }

    @Override
    public void reDraw(int currentPlayer) {
        this.playerPanel.repaint();
        this.boardPanel.repaint();
    }

    private JPanel setupCommandPanel() {
        JPanel commandPanel = new JPanel(new BorderLayout());
        Font f = new Font("Calibri",Font.BOLD, (int)(30*resolution.getScalePercentage()));
        commandInput = new JTextField(15);
        commandInput.setFont(f);
        commandInput.setBackground(Color.WHITE);
        commandInput.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        commandInput.setForeground(Color.DARK_GRAY);

        commandPanel.add(commandInput, BorderLayout.CENTER);
        commandPanel.setOpaque(false);

        return commandPanel;
    }

    private JTabbedPane setupInfoPanel1() {
        UIManager.put("TabbedPane.selected", gameAssets.getDarkerGrey());
        UIManager.put("TabbedPane.contentAreaColor", gameAssets.getDarkerGrey());

        JTabbedPane infoPanel = new JTabbedPane();

        infoPanel.setUI(new BasicTabbedPaneUI(){
            @Override
            protected void installDefaults() {
                super.installDefaults();
                Color alpha = new Color(0,0,0,0);
                highlight = Color.BLACK;
                lightHighlight = alpha;
                shadow = alpha;
                darkShadow = alpha;
                focus = alpha;
            }
        });
        infoPanel.setBackground(Color.BLACK);
        infoPanel.setForeground(Color.WHITE);
        Font f = new Font("Calibri",Font.BOLD, (int)(20*resolution.getScalePercentage()));
        infoOutput = new JTextArea(28, 20);
        infoOutput.setFont(f);
        infoOutput.setEditable(false); infoOutput.setLineWrap(true);
        infoOutput.setBackground(Color.DARK_GRAY);
        infoOutput.setForeground(Color.WHITE);
        infoOutput.setBorder(null);

        helpOutput = new JTextArea(28, 20);
        helpOutput.setFont(f);
        helpOutput.setEditable(false); infoOutput.setLineWrap(true);
        helpOutput.setBackground(Color.DARK_GRAY);
        helpOutput.setForeground(Color.WHITE);
        helpOutput.setBorder(null);

        JScrollPane[] scrollPane = new JScrollPane[] {new JScrollPane(infoOutput), new JScrollPane(helpOutput),
        new JScrollPane(setupCardPanel()), new JScrollPane()};

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane[0], BorderLayout.CENTER);
        panel.add(setupCommandPanel(), BorderLayout.SOUTH);

        for (JScrollPane pane : scrollPane) {
            pane.setBorder(null);
        }
        infoPanel.addTab("Game Log", null, panel, "Game Log - Forget what's happened so far?");
        infoPanel.addTab("Help Panel", null, scrollPane[1], "Help Panel - List of all commands");
        infoPanel.addTab("Current Cards", null, scrollPane[2], "The Current Cards you're holding");
        infoPanel.addTab("Check List", null, scrollPane[3], "Check List for who has what cards");
        return infoPanel;
    }

    private JPanel setupCardPanel() {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.DARK_GRAY);
        return cardPanel;
    }

    private PlayerCardLayout setupPlayerPanel() {
        PlayerCardLayout playerPanel = new PlayerCardLayout(gamePlayers, resolution);
        playerPanel.setOpaque(false);
        TitledBorder border = new TitledBorder(new LineBorder(Color.BLACK,3), "Players");
        border.setTitleFont(new Font("Calibri",Font.BOLD, (int)(20*resolution.getScalePercentage())));
        border.setTitleColor(Color.BLACK);
        playerPanel.setBorder(border);

        return playerPanel;
    }

    //  Getters //

    public JTextArea getInfoOutput() {
        return infoOutput;
    }

    public JTextField getCommandInput() {
        return commandInput;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public Suspects getGameSuspects() {
        return gameSuspects;
    }

    public Weapons getGameWeapons() {
        return gameWeapons;
    }

    public Players getGamePlayers() {
        return gamePlayers;
    }

    public void setGameSuspects(Suspects gameSuspects) {
        this.gameSuspects = gameSuspects;
    }

    public class BoardComponent extends JComponent {
        @Override
        public void paintComponent(Graphics g) {
            Image boardImage = gameAssets.getBoardImage();
            ImageIcon board = new ImageIcon(boardImage);
            g.drawImage(boardImage, 0, 0,(int)(board.getIconWidth()*resolution.getScalePercentage()),
                    (int)(board.getIconHeight()*resolution.getScalePercentage()), this);
        }
    }

    public class BoardUI extends JLayeredPane {
        Suspects gamePlayers;
        Weapons gameWeapons;
        BoardComponent boardComponent;

        public BoardUI(Suspects players, Weapons weapons, BoardComponent boardImage) {
            this.gamePlayers = players;
            this.gameWeapons = weapons;
            this.boardComponent = boardImage;

            this.add(this.boardComponent, new Integer(1));
            this.add(this.gamePlayers, new Integer(2));
            this.add(this.gameWeapons, new Integer(3));

            ImageIcon board = new ImageIcon(gameAssets.getBoardImage());
            Dimension imageSize = new Dimension((int)(board.getIconWidth()*resolution.getScalePercentage()), (int)(board.getIconHeight()*resolution.getScalePercentage()));
            this.setPreferredSize(imageSize);
        }

        @Override
        public void paint(Graphics g) {
            boardComponent.paintComponent(g);
            gamePlayers.paintComponent(g);
            gameWeapons.paintComponent(g);
        }
    }
}
