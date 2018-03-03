/*
  Code to handle the creation of the gamescreen and UI.

  Authors Team11:  Jack Geraghty - 16384181
                   Conor Beenham - 16350851
                   Alen Thomas   - 16333003
 */

package com.team11.cluedo.ui;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.components.Autocomplete;
import com.team11.cluedo.components.InputData;

import com.team11.cluedo.board.Board;
import com.team11.cluedo.players.Players;
import com.team11.cluedo.suspects.Suspects;
import com.team11.cluedo.ui.components.MoveOverlay;
import com.team11.cluedo.ui.components.NotesPanel;
import com.team11.cluedo.ui.components.PlayerHandLayout;
import com.team11.cluedo.ui.components.PlayerLayout;
import com.team11.cluedo.ui.panel.BackgroundPanel;
import com.team11.cluedo.weapons.Weapons;
import com.team11.cluedo.cards.Cards;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.io.*;

public class GameScreen implements Screen {
    private static final String COMMIT_ACTION = "commit";

    private JFrame frame;
    private BoardUI boardPanel;
    private PlayerLayout playerPanel;
    private PlayerHandLayout playerHandPanel;
    private NotesPanel notesPanel;

    private MoveOverlay moveOverlay;

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
        this.moveOverlay = new MoveOverlay();
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

        this.boardPanel = new BoardUI(this.gameSuspects, this.gameWeapons, new BoardComponent(),  moveOverlay);

        this.playerPanel = setupPlayerPanel();
        this.playerHandPanel = setupCardPanel();
        this.notesPanel = new NotesPanel(gamePlayers);

        JPanel infoPanel = setupInfoPanel();
        this.boardPanel = new BoardUI(this.gameSuspects, this.gameWeapons, new BoardComponent(), this.moveOverlay);


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
        this.playerHandPanel.reDraw(currentPlayer);
        this.notesPanel.reDraw(currentPlayer);
        this.boardPanel.repaint();
        this.frame.repaint();
    }

    private JPanel setupCommandPanel() {
        JPanel commandPanel = new JPanel(new BorderLayout());
        Font f = new Font("Orange Kid",Font.BOLD, (int)(25*resolution.getScalePercentage()));

        commandInput = new JTextField(15);
        commandInput.setFont(f);
        commandInput.setBackground(Color.WHITE);
        commandInput.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        commandInput.setForeground(Color.DARK_GRAY);

        ////////////////////////////////////////////////////////////////////////////
        /*
        Code for auto-completing commands
        Only add or remove commands to the commandData arrayList in InputData class
        Don't change any other code in this section
        */
        commandInput.setFocusTraversalKeysEnabled(false);
        InputData inputData = new InputData();
        Autocomplete autoComplete = new Autocomplete(commandInput, inputData.getCommandData());
        commandInput.getDocument().addDocumentListener(autoComplete);
        commandInput.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        commandInput.getActionMap().put(COMMIT_ACTION, autoComplete.new CommitAction());
        ////////////////////////////////////////////////////////////////////////////////

        commandPanel.add(commandInput, BorderLayout.CENTER);
        commandPanel.setOpaque(false);

        return commandPanel;
    }

    private JTextArea setupHelpPanel() {
        JTextArea helpOutput = new JTextArea(28, 25);
        int fontSize = (int)(18 * resolution.getScalePercentage());
        helpOutput.setFont(new Font("Orange Kid",Font.BOLD, fontSize));
        helpOutput.setEditable(false); infoOutput.setLineWrap(true);
        helpOutput.setBackground(Color.DARK_GRAY);
        helpOutput.setForeground(Color.WHITE);
        helpOutput.setBorder(null);

        try {
            InputStream in = getClass().getResourceAsStream("helpPanel.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null)   {

                helpOutput.append(strLine + "\n");
            }
            in.close();

        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }

        return helpOutput;
    }

    private JPanel setupInfoPanel() {
        UIManager.put("TabbedPane.selected", gameAssets.getDarkerGrey());
        UIManager.put("TabbedPane.contentAreaColor", gameAssets.getDarkerGrey());

        int fontSize = (int)(25 * resolution.getScalePercentage());
        Font f = new Font("Orange Kid", Font.BOLD, fontSize);

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
        infoOutput = new JTextArea(22, 25);
        infoOutput.setFont(f);
        infoOutput.setEditable(false); infoOutput.setLineWrap(true);
        infoOutput.setBackground(Color.DARK_GRAY);
        infoOutput.setForeground(Color.WHITE);
        infoOutput.setBorder(null);

        JScrollPane[] scrollPane = new JScrollPane[] {new JScrollPane(infoOutput), new JScrollPane(setupHelpPanel()),
                new JScrollPane(playerHandPanel), new JScrollPane(notesPanel)};

        for (JScrollPane pane : scrollPane) {
            pane.setBorder(null);
        }

        infoTabs.addTab("Game Log", null, scrollPane[0], "Game Log - Forget what's happened so far?");
        infoTabs.addTab("Help Panel", null, scrollPane[1], "Help Panel - List of all commands");
        infoTabs.addTab("Current Cards", null, scrollPane[2], "The Current Cards you're holding");
        infoTabs.addTab("Check List", null, scrollPane[3], "Check List for who has what cards");

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.add(infoTabs, BorderLayout.CENTER);
        infoPanel.add(setupCommandPanel(), BorderLayout.SOUTH);
        return infoPanel;


    }

    private PlayerHandLayout setupCardPanel() {
        return new PlayerHandLayout(gameCards, gamePlayers, resolution);
    }

    private PlayerLayout setupPlayerPanel() {
        PlayerLayout playerPanel = new PlayerLayout(gamePlayers, resolution, 0);
        playerPanel.setOpaque(false);
        TitledBorder border = new TitledBorder(new LineBorder(Color.BLACK,3), "Players");
        border.setTitleFont(new Font("Orange Kid",Font.BOLD, (int)(20*resolution.getScalePercentage())));
        border.setTitleColor(Color.WHITE);
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

    public void setMoveOverlay(MoveOverlay moveOverlay){
        this.moveOverlay = moveOverlay;
    }

    public MoveOverlay getMoveOverlay() {
        return this.moveOverlay;
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
        Suspects gameSuspects;
        Weapons gameWeapons;
        BoardComponent boardComponent;
        MoveOverlay moveOverlay;

        public BoardUI(Suspects players, Weapons weapons, BoardComponent boardImage, MoveOverlay moveOverlay ) {
            this.gameSuspects = players;

            this.gameWeapons = weapons;
            this.boardComponent = boardImage;
            this.moveOverlay = moveOverlay;

            this.add(this.boardComponent, new Integer(1));
            this.add(this.gameSuspects, new Integer(2));
            this.add(this.gameWeapons, new Integer(3));
            this.add(this.moveOverlay, new Integer(4));

            ImageIcon board = new ImageIcon(gameAssets.getBoardImage());
            Dimension imageSize = new Dimension((int)(board.getIconWidth()*resolution.getScalePercentage()), (int)(board.getIconHeight()*resolution.getScalePercentage()));
            this.setPreferredSize(imageSize);
        }

        @Override
        public void paint(Graphics g) {
            boardComponent.paintComponent(g);
            gameSuspects.paintComponent(g);
            gameWeapons.paintComponent(g);
            moveOverlay.paintComponent(g);
        }
    }
}
