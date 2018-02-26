/*
  Code to handle the creation of the gamescreen and UI.

  Authors Team11:  Jack Geraghty - 16384181
                   Conor Beenham - 16350851
                   Alen Thomas   - 16333003
 */


package com.team11.cluedo.ui;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.board.Board;
import com.team11.cluedo.cards.Cards;
import com.team11.cluedo.players.Players;
import com.team11.cluedo.suspects.Suspects;
import com.team11.cluedo.ui.panel.BackgroundPanel;
import com.team11.cluedo.weapons.Weapons;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.io.*;

public class GameScreen implements Screen {
    private JFrame frame;
    private BoardUI boardPanel;
    private PlayerCardLayout playerPanel;

    private JTextArea infoOutput;
    private JTabbedPane infoTabs;
    private JTextField commandInput;

    private Board gameBoard;
    private Suspects gameSuspects;
    private Weapons gameWeapons;
    private Players gamePlayers;
    private Assets gameAssets;
    private Cards gameCards;

    private Resolution resolution;

    public GameScreen(Board gameBoard, Suspects gameSuspects, Weapons gameWeapons, Players gamePlayers, Assets gameAssets, Resolution resolution) throws IOException{
        this.gameBoard = gameBoard;
        this.gameSuspects = gameSuspects;
        this.gameWeapons = gameWeapons;
        this.gamePlayers = gamePlayers;
        this.gameAssets = gameAssets;
        this.resolution = resolution;
        this.gameCards = new Cards();
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

        this.playerPanel = setupPlayerPanel(0);
        JPanel infoPanel = setupInfoPanel1();
        this.boardPanel = new BoardUI(this.gameSuspects, this.gameWeapons, new BoardComponent());

        contentPanel.add(playerPanel, BorderLayout.WEST);
        contentPanel.add(infoPanel, BorderLayout.EAST);
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
        System.exit(0);
    }

    @Override
    public void reDraw(int currentPlayer) {
        this.playerPanel.reDraw(currentPlayer);
        this.boardPanel.repaint();
        this.frame.repaint();
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

    private JPanel setupInfoPanel1() {
        UIManager.put("TabbedPane.selected", gameAssets.getDarkerGrey());
        UIManager.put("TabbedPane.contentAreaColor", gameAssets.getDarkerGrey());

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        this.infoTabs = new JTabbedPane();

        infoTabs.setUI(new BasicTabbedPaneUI(){
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
        infoTabs.setBackground(Color.BLACK);
        infoTabs.setForeground(Color.WHITE);
        Font f = new Font("Calibri",Font.BOLD, (int)(20*resolution.getScalePercentage()));
        infoOutput = new JTextArea(28, 25);
        infoOutput.setFont(f);
        infoOutput.setEditable(false); infoOutput.setLineWrap(true);
        infoOutput.setBackground(Color.DARK_GRAY);
        infoOutput.setForeground(Color.WHITE);
        infoOutput.setBorder(null);

        JTextArea helpOutput = new JTextArea(28, 25);
        f = new Font("Calibri",Font.BOLD, (int)(17*resolution.getScalePercentage()));
        helpOutput.setFont(f);
        helpOutput.setEditable(false); infoOutput.setLineWrap(true);
        helpOutput.setBackground(Color.DARK_GRAY);
        helpOutput.setForeground(Color.WHITE);
        helpOutput.setBorder(null);

        try {
            //Find the boardInfo.txt and open it
            InputStream in = getClass().getResourceAsStream("helpPanel.txt");

            //Open a buffered reader to read each line
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null)   {

                helpOutput.append(strLine + "\n");
            }
            in.close();

        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

        JScrollPane[] scrollPane = new JScrollPane[] {new JScrollPane(infoOutput), new JScrollPane(helpOutput),
        new JScrollPane(setupCardPanel()), new JScrollPane()};
        for (JScrollPane pane : scrollPane) {
            pane.setBorder(null);
        }
        infoTabs.addTab("Game Log", null, scrollPane[0], "Game Log - Forget what's happened so far?");
        infoTabs.addTab("Help Panel", null, scrollPane[1], "Help Panel - List of all commands");
        infoTabs.addTab("Current Cards", null, scrollPane[2], "The Current Cards you're holding");
        infoTabs.addTab("Check List", null, scrollPane[3], "Check List for who has what cards");

        infoPanel.add(infoTabs, BorderLayout.CENTER);
        infoPanel.add(setupCommandPanel(), BorderLayout.SOUTH);
        return infoPanel;


    }

    private JPanel setupCardPanel() {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.DARK_GRAY);
        return cardPanel;
    }

    private PlayerCardLayout setupPlayerPanel(int currentPlayer) {
        PlayerCardLayout playerPanel = new PlayerCardLayout(gamePlayers, resolution, currentPlayer);
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

    public Cards getGameCards() {
        return gameCards;
    }

    public void setTab(int i) {
        infoTabs.setSelectedIndex(i);
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
